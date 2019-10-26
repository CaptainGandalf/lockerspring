package com.locker.locker.controllers;

import com.locker.locker.dtos.AuthenticateDto;
import com.locker.locker.dtos.UserAuthDto;
import com.locker.locker.entities.User;
import com.locker.locker.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/authenticate")
public class AuthenticateController {

    @Autowired
    UserService userService;


    @PostMapping
    public ResponseEntity<AuthenticateDto> authenticate(@Valid @RequestBody UserAuthDto credentials){
        if(!userService.findByEmail(credentials.getEmail()).isPresent()){
            log.error("User with email " + credentials.getEmail() + " does not exist");
            return ResponseEntity.badRequest().build();
        }

        User user = userService.findByEmail(credentials.getEmail()).get();

        if (userService.checkPasswords(credentials.getEmail(), credentials.getPassword())){
            AuthenticateDto authDto = new AuthenticateDto();
            authDto.setUserId(user.getId());
            return ResponseEntity.ok(authDto);
        }else {
            log.error("passwords dont match");
            return ResponseEntity.badRequest().build();
        }
    }
}
