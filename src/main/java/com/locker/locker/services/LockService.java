package com.locker.locker.services;

import com.locker.locker.entities.Lock;
import com.locker.locker.entities.User;
import com.locker.locker.repositories.LockRepository;
import com.locker.locker.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

@RequiredArgsConstructor
public class LockService {

    private final LockRepository lockRepository;

    public List<Lock> findAll(){
        return lockRepository.findAll();
    }

    public Optional<Lock> findById(Long id){
        return lockRepository.findById(id);
    }

    public Optional<List<Lock>> findByUser(User user) { return lockRepository.findByUser(user);}

    public Lock save(Lock lock){
        return lockRepository.save(lock);
    }

    public void deleteById(Long id){
        lockRepository.deleteById(id);
    }

}
