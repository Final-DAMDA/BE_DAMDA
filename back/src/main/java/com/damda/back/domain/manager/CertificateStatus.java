package com.damda.back.domain.manager;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table
public class CertificateStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "manager_id")
    private Manager manager;

    private boolean firstGradeOffline;

    private boolean secondGradeOffline;

    private boolean firstGradeOnline;

    private boolean secondGradeOnline;

    // TODO: none 넣어야할지, 넣을거면 setter 별도로 구현필요
    // @Column
    // private boolean none;

}
