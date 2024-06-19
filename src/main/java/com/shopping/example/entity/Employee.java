package com.shopping.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;
import java.util.List;
@Entity
@Table(name = "employees")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    @Id
    private Long id;
    @Column
    private String name;
    @Column
    private String phone;
    @Column
    private String gender;
    @Column
    @Temporal(TemporalType.DATE)
    private Date birthday;
    @Column
    private String city;
    @Column
    private String district;
    @Column
    private String ward;
    @Column
    private String addressDetail;
    @Column
    private String avatar;
    @Column
    private String status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date createdAt;

    @OneToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;






}
