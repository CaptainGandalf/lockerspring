package com.locker.locker.dtos;

import lombok.Data;

@Data
public class UserDto {

    private Long id;

    private String name;

    private String lastName;

    private String email;

    private String password;

    private String idNowPicture;

}
