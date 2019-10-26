package com.locker.locker.dtos;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserIdnowResultDto extends FaceCompareResultDto implements Serializable {

    private String status;

    private String message;

}
