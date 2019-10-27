package com.locker.locker.controllers;

import com.locker.locker.dtos.*;
import com.locker.locker.entities.User;
import com.locker.locker.services.IdnowService;
import com.locker.locker.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/idnow")
public class IdnowController {

    @Autowired
    IdnowService idnowService;

    @Autowired
    UserService userService;

    @Autowired
    ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<UserIdnowResultDto> idnow(@Valid @RequestBody UserIdnowDto userIdnow) {
        User user;
        if (userIdnow.getUserId() != null && userService.findById(userIdnow.getUserId()).isPresent()) {
            user = userService.findById(userIdnow.getUserId()).get();
        } else if (userIdnow.getEmail() != null && userService.findByEmail(userIdnow.getEmail()).isPresent()) {
            user = userService.findByEmail(userIdnow.getEmail()).get();
        } else {
            log.error("User with email " + userIdnow.getEmail() + " or id " + userIdnow.getUserId() + " was not found");
            return ResponseEntity.badRequest().build();
        }
        UserIdnowResultDto result = this.verifyIdnow(user.getIdNowPicture(), userIdnow.getIdNowPicture());
        return ResponseEntity.ok(result);
    }

    public UserIdnowResultDto verifyIdnow(String base64EncodedDocumentFaceImage, String base64EncodedFrontalFaceImage) {
        String imageStatus = idnowService.verifyFaceImage(base64EncodedFrontalFaceImage);
        UserIdnowResultDto result = new UserIdnowResultDto();
        if (!"OK".equals(imageStatus)) {
            result.setAccepted(false);
            result.setMessage(imageStatus);
            result.setStatus("failed");
        } else if (base64EncodedFrontalFaceImage.equals(base64EncodedDocumentFaceImage)) {
            // Skipping comparison if image string matches (trusting that DB contains correct data)
            result.setAccepted(true);
            result.setResult(1.0);
            result.setNormalized_score(100.0);
            result.setMessage("User is allowed");
            result.setStatus("success");
        } else {
            try {
                FaceCompareResultDto compare = idnowService.faceComparison(base64EncodedDocumentFaceImage, base64EncodedFrontalFaceImage);
                modelMapper.map(compare, result);
            } catch (HttpStatusCodeException e) {
                result.setAccepted(false);
                log.error("Error on face comparison", e);
            }
            result.setMessage(result.getAccepted() ? "User is allowed" : "User is not allowed");
            result.setStatus(result.getAccepted() ? "success" : "failed");
        }
        log.info("ID Now returned status " + result.getStatus() + " with message " + result.getMessage());
        return result;
    }

//    @PostMapping(path = "/verifyFaceImage")
//    @ResponseBody
//    public ResponseEntity<FaceVerifyStatusDto> verifyFaceImage(@Valid @NotBlank @RequestBody Map<String, String> body) {
//        String base64EncodedImage = body.get("base64EncodedImage");
//        FaceVerifyStatusDto response = idnowService.verifyFaceImage(base64EncodedImage);
//        return ResponseEntity.ok(response);
//    }
//
//    @PostMapping(path = "/faceComparison")
//    @ResponseBody
//    public ResponseEntity<FaceCompareResultDto> faceComparison(@Valid @NotBlank @RequestBody Map<String, String> body) {
//        String base64EncodedDocumentFaceImage = body.get("base64EncodedDocumentFaceImage") ;
//        String base64EncodedFrontalFaceImage = body.get("base64EncodedFrontalFaceImage");
//        FaceCompareResultDto response = idnowService.faceComparison(base64EncodedDocumentFaceImage,base64EncodedFrontalFaceImage);
//        return ResponseEntity.ok(response);
//    }
}
