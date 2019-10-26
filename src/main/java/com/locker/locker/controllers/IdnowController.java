package com.locker.locker.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.locker.locker.dtos.FaceCompareResultDto;
import com.locker.locker.dtos.FaceVerifyStatusDto;
import com.locker.locker.services.IdnowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/idnow")
public class IdnowController {

    @Autowired
    IdnowService idnowService;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping(path = "/verifyFaceImage")
    @ResponseBody
    public ResponseEntity<FaceVerifyStatusDto> verifyFaceImage(@Valid @NotBlank @RequestBody Map<String, String> body) {
        String base64EncodedImage = body.get("base64EncodedImage");
        String[] faceTypes = new String [] {"FACE"};
        FaceVerifyStatusDto response = idnowService.verifyFaceImage(base64EncodedImage, faceTypes);
        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/faceComparison")
    @ResponseBody
    public ResponseEntity<FaceCompareResultDto> faceComparison(@Valid @NotBlank @RequestBody Map<String, String> body) {
        String base64EncodedDocumentFaceImage = body.get("base64EncodedDocumentFaceImage") ;
        String base64EncodedFrontalFaceImage = body.get("base64EncodedFrontalFaceImage");
        FaceCompareResultDto response = idnowService.faceComparison(base64EncodedDocumentFaceImage,base64EncodedFrontalFaceImage);
        return ResponseEntity.ok(response);
    }
}
