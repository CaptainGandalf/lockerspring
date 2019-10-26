package com.locker.locker.dtos;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Data
public class FaceFeedbackDto implements Serializable {

    private Boolean exposureOk;

    private Boolean fullFrontalOk;

    private Boolean poseOk;

    private Boolean nonOccluded;

    private Boolean faceShadowOk;

    private Boolean focusOk;

    private Boolean mouthClosed;

    private Boolean noHotspots;

    private Boolean expressionNeutral;

    private Boolean eyesOpen;

    private Boolean colourOk;

    private Boolean noReflection;

    private Boolean noGlasses;

    private Boolean gazeFrontal;

    private Boolean noRedEyes;

    private Double yaw;

    private Boolean selfieOk;

    private Boolean lookLeftOk;

    private Boolean lookRightOk;

    private Boolean faceFound;

    private Boolean imageSharp;

    private Boolean success;

}
