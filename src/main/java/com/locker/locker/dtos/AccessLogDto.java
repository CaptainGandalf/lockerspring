package com.locker.locker.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class AccessLogDto {

    private Long id;

    private Long ownerId;

    private String ownerEmail;

    private Long lockId;

    private String lockAddress;

    private String lockDoor;

    private String lockStatus;

    private Long userId;

    private String userEmail;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date changedAt;

}