package com.locker.locker.dtos;

import lombok.Data;

@Data
public class UserCreateStatusDto {

    private UserDto result;

    private String status;

    private String message;

}
