package com.locker.locker.entities;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
public class Key {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //DISABLED ENABLED
    @Column(nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name = "lock_id")
    private Lock lock;

    @ManyToOne
    @JoinColumn(name = "userIssuedBy_id")
    private User issuedBy;

    @ManyToOne
    @JoinColumn(name = "userIssuedFor_id")
    private User issuedFor;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime expiresAt;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

}
