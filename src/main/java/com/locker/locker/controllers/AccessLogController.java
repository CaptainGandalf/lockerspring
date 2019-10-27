package com.locker.locker.controllers;

import com.locker.locker.dtos.AccessLogDto;
import com.locker.locker.dtos.LockDto;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/accessLogs")
public class AccessLogController {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AccessLogService accessLogService;

    @Autowired
    UserService userService;

    @Autowired
    LockService lockService;

    @GetMapping
    public ResponseEntity<List<AccessLogDto>> findAll() {
        List<AccessLog> accessLogs = accessLogService.findAll();
        return ResponseEntity.ok(accessLogs.stream().map( accessLog -> convertToDto(accessLog)).collect(Collectors.toList()));
    }

    @GetMapping("/getUserLogs/{userId}")
    public ResponseEntity<List<AccessLogDto>> findUserLogs(@PathVariable Long userId){
        if(!userService.findById(userId).isPresent()){
            log.error("User " + userId + " does not exist");
            return ResponseEntity.badRequest().build();
        }
        List<AccessLog> logs = accessLogService.findByUser(userService.findById(userId).get()).get();
        return ResponseEntity.ok(logs.stream().map( log -> convertToDto(log)).collect(Collectors.toList()));
    }

    @GetMapping("/getOwnerLogs/{userId}")
    public ResponseEntity<List<AccessLogDto>> getOwnerLogs(@PathVariable Long userId){
        if(!userService.findById(userId).isPresent()){
            log.error("User " + userId + " does not exist");
            return ResponseEntity.badRequest().build();
        }
        List<AccessLog> logs = new ArrayList<>();
        for(Lock l : lockService.findByUser(userService.findById(userId).get()).get()){
            logs.addAll(accessLogService.findByLock(l).get());
        }
        return ResponseEntity.ok(logs.stream().map( log -> convertToDto(log)).collect(Collectors.toList()));
    }

    @GetMapping("/getLockLogs/{lockId}")
    public ResponseEntity<List<AccessLogDto>> findLockLogs(@PathVariable Long lockId){
        if(!lockService.findById(lockId).isPresent()){
            log.error("Lock " + lockId + " does not exist");
            return ResponseEntity.badRequest().build();
        }
        List<AccessLog> logs = accessLogService.findByLock(lockService.findById(lockId).get()).get();
        return ResponseEntity.ok(logs.stream().map( log -> convertToDto(log)).collect(Collectors.toList()));
    }

    private AccessLogDto convertToDto(AccessLog accessLog){
        Lock lock = lockService.findById(accessLog.getLock().getId()).get();
        User user = userService.findById(accessLog.getUser().getId()).get();
        User owner = userService.findById(lock.getUser().getId()).get();
        AccessLogDto accessLogDto = modelMapper.map(accessLog, AccessLogDto.class);
        accessLogDto.setOwnerId(owner.getId());
        accessLogDto.setOwnerEmail(owner.getEmail());
        accessLogDto.setLockId(lock.getId());
        accessLogDto.setLockAddress(lock.getAddress());
        accessLogDto.setLockDoor(lock.getDoor());
        accessLogDto.setUserId(user.getId());
        accessLogDto.setUserEmail(user.getEmail());
        return accessLogDto;
    }
}
