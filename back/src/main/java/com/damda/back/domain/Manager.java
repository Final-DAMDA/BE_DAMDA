package com.damda.back.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.DayOfWeek;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Table(name = "manager_tb")
public class Manager {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    @Column(nullable = false)
    private Member userId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek activityDate;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Area activityArea;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CertificateStatus certificateStatus;

    @Column(nullable = false)
    private boolean vehicle;

    @Column(nullable = false)
    private boolean fieldExperience;

    @Column(nullable = false)
    private boolean serviceRule;
    
}

enum Area {}

enum CertificateStatus {}
