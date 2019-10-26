package com.locker.locker.services;

import com.locker.locker.entities.AccessLog;
import com.locker.locker.entities.Key;
import com.locker.locker.entities.Lock;
import com.locker.locker.entities.User;
import com.locker.locker.repositories.AccessLogRepository;
import com.locker.locker.repositories.KeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

@RequiredArgsConstructor
public class AccessLogService {

    private final AccessLogRepository accessLogRepository;

    public List<AccessLog> findAll(){
        return accessLogRepository.findAll();
    }

    public Optional<AccessLog> findById(Long id){
        return accessLogRepository.findById(id);
    }

    public Optional<List<AccessLog>> findByUser(User user) { return accessLogRepository.findByUser(user);}

    public Optional<List<AccessLog>> findByLock(Lock lock) { return accessLogRepository.findByLock(lock);}

    public AccessLog save(AccessLog accessLog){
        return accessLogRepository.save(accessLog);
    }

}
