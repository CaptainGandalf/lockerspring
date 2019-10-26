package com.locker.locker.dtos;

import lombok.Data;

import java.io.Serializable;

@Data
public class FaceVerifyStatusDto implements Serializable {

    private String faceType;

    private FaceFeedbackDto feedback;

}
