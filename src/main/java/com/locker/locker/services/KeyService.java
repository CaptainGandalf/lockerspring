package com.locker.locker.services;

import com.locker.locker.entities.Key;
import com.locker.locker.entities.Lock;
import com.locker.locker.entities.User;
import com.locker.locker.repositories.KeyRepository;
import com.locker.locker.repositories.LockRepository;
import com.locker.locker.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service

@RequiredArgsConstructor
public class KeyService {

    private final KeyRepository keyRepository;

    public List<Key> findAll(){
        return keyRepository.findAll();
    }

    public Optional<Key> findById(Long id){
        return keyRepository.findById(id);
    }

    public Optional<List<Key>> findByIssuer(User user) { return keyRepository.findByIssuedBy(user);}

    public Optional<List<Key>> findByIssuedFor(User user) { return keyRepository.findByIssuedFor(user);}

    public Optional<List<Key>> findByLock(Lock lock) { return keyRepository.findAllByLock(lock); }


    public Key save(Key key){
        return keyRepository.save(key);
    }

    public void deleteById(Long id){
        keyRepository.deleteById(id);
    }
}
