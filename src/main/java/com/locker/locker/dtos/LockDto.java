package com.locker.locker.dtos;

import lombok.Data;

@Data
public class LockDto {

    private Long id;

    private String address;

    private String door;

    private String status;

    private Long userid;

}
