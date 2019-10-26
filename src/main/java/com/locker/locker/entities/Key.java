package com.locker.locker.entities;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.jws.soap.SOAPBinding;
import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
@Data
public class Key {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String address;

    private String door;

    @ManyToOne
    @JoinColumn(name = "lock_id")
    private Lock lock;

    @ManyToOne
    @JoinColumn(name = "userIssuedBy_id")
    private User issuedBy;

    @ManyToOne
    @JoinColumn(name = "userIssuedFor_id")
    private User issuedFor;


    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

}
