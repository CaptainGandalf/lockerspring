package com.locker.locker.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class KeyDto {

    private Long id;

    private String status;

    private Long issuedById;

    private Long issuedForId;

    private Long lockId;

    private LocalDateTime expiresAt;

    private String idNowPicture;

}
