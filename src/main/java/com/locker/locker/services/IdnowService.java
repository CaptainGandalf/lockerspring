package com.locker.locker.services;

import com.locker.locker.dtos.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class IdnowService {

    @Autowired
    RestTemplate restTemplate;

    @Value("${spring.idnow.url}")
    String url;

    public String verifyFaceImage(String base64EncodedImage) {
        // FIXME: check bellow for test purpose only
        if (!"test".equals(base64EncodedImage)) {
            try {
                FaceVerifyStatusDto idStatus = this.verifyFaceImage(base64EncodedImage, new String[] { "FACE" });
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
