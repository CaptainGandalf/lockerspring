package com.locker.locker.services;

import com.locker.locker.dtos.FaceCompareDto;
import com.locker.locker.dtos.FaceCompareResultDto;
import com.locker.locker.dtos.FaceVerifyStatusDto;
import com.locker.locker.dtos.FaceVerifyDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class IdnowService {

    @Autowired
    RestTemplate restTemplate;

    @Value("${spring.idnow.url}")
    String url;

    public FaceVerifyStatusDto verifyFaceImage(String base64EncodedImage){
        return this.verifyFaceImage(base64EncodedImage, new String[] { "FACE" });
    }

    public FaceVerifyStatusDto verifyFaceImage(String base64EncodedImage, String[] faceTypes){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        FaceVerifyDto faceImageDto = new FaceVerifyDto();
        faceImageDto.setBase64EncodedImage(base64EncodedImage);
        faceImageDto.setFaceTypes(faceTypes);
        HttpEntity<FaceVerifyDto> entity = new HttpEntity<>(faceImageDto, headers);
        return restTemplate.postForObject(url + "/api/v1/verify_face_image?notMsgPacked=true", entity, FaceVerifyStatusDto.class);
    }

    public FaceCompareResultDto faceComparison(String base64EncodedDocumentFaceImage, String base64EncodedFrontalFaceImage){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        FaceCompareDto faceImageDto = new FaceCompareDto();
        faceImageDto.setBase64EncodedDocumentFaceImage(base64EncodedDocumentFaceImage);
        faceImageDto.setBase64EncodedFrontalFaceImage(base64EncodedFrontalFaceImage);
        HttpEntity<FaceCompareDto> entity = new HttpEntity<>(faceImageDto, headers);
        return restTemplate.postForObject(url + "/api/v1/face_comparison?notMsgPacked=true", entity, FaceCompareResultDto.class);
    }

    @Bean
    public RestTemplate rest() {
        return new RestTemplate();
    }
}
