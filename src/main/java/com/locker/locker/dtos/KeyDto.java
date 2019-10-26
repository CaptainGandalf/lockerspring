package com.locker.locker.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class KeyDto {

    private Long id;

    private String status;

    private Long issuedById;

    private String issuedForEmail;

    private Long lockId;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expiresAt;

}
