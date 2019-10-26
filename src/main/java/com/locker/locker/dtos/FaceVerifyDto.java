package com.locker.locker.dtos;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Data
public class FaceVerifyDto implements Serializable {

    private String base64EncodedImage;

    private String[] faceTypes;

}
