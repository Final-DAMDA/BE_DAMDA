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
    private Integer id;

    @OneToOne
    @JoinColumn(name = "manager_id")
    private Manager manager;

    @Column
    private boolean firstGradeOffline;

    @Column
    private boolean secondGradeOffline;

    @Column
    private boolean firstGradeOnline;

    @Column
    private boolean secondGradeOnline;

    // TODO: none 넣어야할지, 넣을거면 setter 별도로 구현필요
    // @Column
    // private boolean none;

}
