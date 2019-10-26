package com.locker.locker.dtos;

import lombok.Data;

import java.io.Serializable;

@Data
public class FaceCompareResultDto implements Serializable {

    private Double result;

    private Boolean accepted;

    private Double normalized_score;

    private Double threshold;

    private String version;

}
