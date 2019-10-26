package com.locker.locker.services;

import com.locker.locker.entities.User;
import com.locker.locker.repositories.UserRepository;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

@RequiredArgsConstructor
public class UserService {
    @Autowired
    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id){
        return userRepository.findById(id);
    }

    public Optional<User> findByEmail(String email) { return userRepository.findByEmail(email);}

    //TODO: check if user is owner of the lock he gives access to
    public User save(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public void deleteById(Long id){
        userRepository.deleteById(id);
    }

    public Boolean checkPasswords(String email, String pass){
        return passwordEncoder.matches(pass, userRepository.findByEmail(email).get().getPassword());
    }


}
