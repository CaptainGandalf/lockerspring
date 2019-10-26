package com.locker.locker.controllers;

import com.locker.locker.dtos.LockDto;
import com.locker.locker.dtos.GenericError;
import com.locker.locker.dtos.LockStateDto;
import com.locker.locker.entities.AccessLog;
import com.locker.locker.entities.Lock;
import com.locker.locker.entities.User;
import com.locker.locker.services.AccessLogService;
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
@RequestMapping("/locks")
public class LockController {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    LockService lockService;

    @Autowired
    UserService userService;

    @Autowired
    AccessLogService accessLogService;

    @GetMapping
    public ResponseEntity<List<LockDto>> findAll() {
        List<Lock> locks = lockService.findAll();
        return ResponseEntity.ok(locks.stream().map( lock -> convertToDto(lock)).collect(Collectors.toList()));
    }

    @PostMapping
    public ResponseEntity<LockDto> create(@Valid @RequestBody LockDto lockToSave){
        if(!userService.findById(lockToSave.getUserid()).isPresent()){
            GenericError genericError = new GenericError();
            genericError.setMessage("User with id:"+lockToSave.getUserid()+" was not found");
            ResponseEntity.ok(genericError);
        }
        Lock lock = lockService.save(convertToEntity(lockToSave));
        return ResponseEntity.ok(convertToDto(lock));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LockDto> update(@Valid @RequestBody LockDto lockDto){
        if(!lockService.findById(lockDto.getId()).isPresent()){
            log.error("Lock " + lockDto.getId() + " does not exist");
            return ResponseEntity.badRequest().build();
        }
        Lock lock = convertToEntity(lockDto);
        lockService.save(lock);
        return ResponseEntity.ok(convertToDto(lock));
    }

    @PostMapping("/updateState")
    public ResponseEntity<LockDto> updateState(@Valid @RequestBody LockStateDto lockStateDto){
        if(!userService.findById(lockStateDto.getUserid()).isPresent()){
            log.error("User " + lockStateDto.getUserid() + " does not exist");
            return ResponseEntity.badRequest().build();
        }
        if(!lockService.findById(lockStateDto.getId()).isPresent()) {
            log.error("Lock " + lockStateDto.getId() + " does not exist");
            return ResponseEntity.badRequest().build();
        }
        User user = userService.findById(lockStateDto.getUserid()).get();
        Lock lock = lockService.findById(lockStateDto.getId()).get();
        lock.setStatus(lockStateDto.getStatus());
        lockService.save(lock);

        AccessLog log = new AccessLog();
        log.setUser(user);
        log.setLock(lock);
        accessLogService.save(log);
        return ResponseEntity.ok(convertToDto(lock));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LockDto> findById(@PathVariable Long id){
        if(!lockService.findById(id).isPresent()){
            log.error("Lock id " + id + " does not exist");
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(convertToDto(lockService.findById(id).get()));
    }

    @GetMapping("/getUserLocks/{userId}")
    public ResponseEntity<List<LockDto>> findUserLocks(@PathVariable Long userId){
        if(!userService.findById(userId).isPresent()){
            log.error("User " + userId + " does not exist");
            return ResponseEntity.badRequest().build();
        }
        List<Lock> locks = lockService.findByUser(userService.findById(userId).get()).get();
        return ResponseEntity.ok(locks.stream().map( lock -> convertToDto(lock)).collect(Collectors.toList()));
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

    private  Lock convertToEntity(LockDto lockDto, Long id){
        Lock lock = modelMapper.map(lockDto, Lock.class);
        lock.setId(id);
        return lock;
    }

    private  Lock convertToEntity(LockDto lockDto){
        Lock lock = modelMapper.map(lockDto, Lock.class);
        lock.setUser(userService.findById(lockDto.getUserid()).get());
        return lock;
    }

    private  LockDto convertToDto(Lock lock){
        LockDto lockDto = modelMapper.map(lock, LockDto.class);
        lockDto.setUserid(lock.getUser().getId());
        return lockDto;
    }

}
