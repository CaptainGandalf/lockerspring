package com.locker.locker.controllers;

import com.locker.locker.dtos.KeyDto;
import com.locker.locker.dtos.LockDto;
import com.locker.locker.dtos.GenericError;
import com.locker.locker.entities.Key;
import com.locker.locker.entities.Lock;
import com.locker.locker.services.KeyService;
import com.locker.locker.services.LockService;
import com.locker.locker.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/keys")
public class KeyController {

    public static final String ERROR = "ERROR";

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    LockService lockService;

    @Autowired
    UserService userService;

    @Autowired
    KeyService keyService;

    @GetMapping
    public ResponseEntity<List<KeyDto>> findAll() {
        List<Key> keys = keyService.findAll();
        return ResponseEntity.ok(keys.stream().map( key -> convertToDto(key)).collect(Collectors.toList()));
    }

    @PostMapping
    public ResponseEntity<KeyDto> create(@Valid @RequestBody KeyDto keyToSave){
        if(!userService.findById(keyToSave.getIssuedById()).isPresent() || !userService.findById(keyToSave.getIssuedForId()).isPresent()){
            ResponseEntity.ok(createError("User with id:"+keyToSave.getIssuedById()+" or " +keyToSave.getIssuedForId()+ " was not found"));
        } else if(!lockService.findById(keyToSave.getLockId()).isPresent()){
            ResponseEntity.ok(createError("Lock with id:"+keyToSave.getLockId()+" was not found"));
        }
        Key key = keyService.save(convertToEntity(keyToSave));
        return ResponseEntity.ok(convertToDto(key));
    }

    @PutMapping("/{id}")
    public ResponseEntity<KeyDto> update(@Valid @RequestBody KeyDto keyDto){
        if(!keyService.findById(keyDto.getId()).isPresent()){
            log.error("Key " + keyDto.getId() + " does not exist");
            return ResponseEntity.badRequest().build();
        }
        Key key = convertToEntity(keyDto);
        keyService.save(key);
        return ResponseEntity.ok(convertToDto(key));
    }

    @GetMapping("/{id}")
    public ResponseEntity<KeyDto> findById(@PathVariable Long id){
        if(!keyService.findById(id).isPresent()){
            log.error("Kay id " + id + " does not exist");
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(convertToDto(keyService.findById(id).get()));
    }

    @GetMapping("/getLocksIssuedForUser/{userId}")
    public ResponseEntity<List<KeyDto>> findKeysIssuedForUSer(@PathVariable Long userId){
        if(!userService.findById(userId).isPresent()){
            log.error("User " + userId + " does not exist");
            return ResponseEntity.badRequest().build();
        }
        List<Key> keys = keyService.findByIssuedFor(userService.findById(userId).get()).get();
        return ResponseEntity.ok(keys.stream().map( key -> convertToDto(key)).collect(Collectors.toList()));
    }

    @GetMapping("/getLocksIssuedByUser/{userId}")
    public ResponseEntity<List<KeyDto>> findKeysIssuedByUSer(@PathVariable Long userId){
        if(!userService.findById(userId).isPresent()){
            log.error("User " + userId + " does not exist");
            return ResponseEntity.badRequest().build();
        }
        List<Key> keys = keyService.findByIssuer(userService.findById(userId).get()).get();
        return ResponseEntity.ok(keys.stream().map( key -> convertToDto(key)).collect(Collectors.toList()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id){
        if(!lockService.findById(id).isPresent()){
            log.error("Lock id" + id + " does not exist");
            ResponseEntity.badRequest().build();
        }
        lockService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    private  Key convertToEntity(KeyDto keyDto){
        Key key = modelMapper.map(keyDto, Key.class);
        key.setIssuedBy(userService.findById(keyDto.getIssuedById()).get());
        key.setIssuedFor(userService.findById(keyDto.getIssuedForId()).get());
        key.setLock(lockService.findById(keyDto.getLockId()).get());
        return key;
    }

    private  KeyDto convertToDto(Key key){
        KeyDto keyDto = modelMapper.map(key, KeyDto.class);
        keyDto.setIssuedById(key.getIssuedBy().getId());
        keyDto.setIssuedForId(key.getIssuedFor().getId());
        keyDto.setLockId(key.getLock().getId());
        return keyDto;
    }

    private GenericError createError(String message){
        GenericError genericError = new GenericError();
        genericError.setStatus(ERROR);
        genericError.setMessage(message);
        return genericError;
    }

}
