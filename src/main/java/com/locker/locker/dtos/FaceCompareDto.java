package com.locker.locker.dtos;

import lombok.Data;

import java.io.Serializable;

@Data
public class FaceCompareDto implements Serializable {

    private String base64EncodedDocumentFaceImage;

    private String base64EncodedFrontalFaceImage;

}
