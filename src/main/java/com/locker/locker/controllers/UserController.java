package com.locker.locker.controllers;

import com.locker.locker.dtos.*;
import com.locker.locker.entities.User;
import com.locker.locker.services.IdnowService;
import com.locker.locker.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    IdnowService idnowService;

    @Autowired
    ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<UserDto>> findAll() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(users.stream().map(user -> convertToDto(user)).collect(Collectors.toList()));
    }

    @PostMapping
    public ResponseEntity<UserCreateStatusDto> create(@Valid @RequestBody UserCreateDto userDto) {
        UserCreateStatusDto response = new UserCreateStatusDto();
        log.info(userDto.getIdNowPicture());
        String message = verifyImage(userDto.getIdNowPicture());

        response.setMessage(message);
        if ("OK".equals(message)) {
            User user = convertToEntity(userDto);
            User userCreated = userService.save(user);
            response.setResult(convertToDto(userCreated));
            response.setStatus("success");
        } else {
            response.setStatus("failed");
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable Long id, @Valid @RequestBody UserDto userDto) {
        if (!userService.findById(id).isPresent()) {
            log.error("Id " + id + " does not exist");
            return ResponseEntity.badRequest().build();
        }
        User user = convertToEntity(userDto);
        User userCreated = userService.save(user);
        return ResponseEntity.ok(convertToDto(userCreated));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable Long id) {
        if (!userService.findById(id).isPresent()) {
            log.error("Id " + id + " does not exist");
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(convertToDto(userService.findById(id).get()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        if (!userService.findById(id).isPresent()) {
            log.error("Id " + id + " does not exist");
            ResponseEntity.badRequest().build();
        }
        userService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    private UserDto convertToDto(User user) {
        UserDto userDto = modelMapper.map(user, UserDto.class);
        return userDto;
    }

    private User convertToEntity(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        return user;
    }

    private User convertToEntity(UserCreateDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        return user;
    }

    private String verifyImage(String base64EncodedImage) {
        if (!base64EncodedImage.equals("test")) {
            try {
                FaceVerifyStatusDto idStatus = idnowService.verifyFaceImage(base64EncodedImage);
                if (idStatus == null || idStatus.getFeedback() == null || !idStatus.getFeedback().getSuccess()) {
                    log.error("ID image verification failed");
                    return "ID image verification failed";
                } else if (!idStatus.getFeedback().getExposureOk() || !idStatus.getFeedback().getImageSharp() || !idStatus.getFeedback().getFocusOk()) {
                    log.error("ID image quality poor");
                    return "ID image quality poor";
                } else if (!idStatus.getFeedback().getFullFrontalOk()) {
                    log.error("ID image should contain full frontal portrait");
                    return "ID image should contain full frontal portrait";
                }
            } catch (HttpServerErrorException e) {
                log.error("Error on ID image verification", e);
                return "ID image verification failed";
            }
        }
        return "OK";
    }
}
