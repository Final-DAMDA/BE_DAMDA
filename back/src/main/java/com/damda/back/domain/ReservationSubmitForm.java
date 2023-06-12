package com.damda.back.domain;


import com.damda.back.data.common.PayMentStatus;
import com.damda.back.data.common.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationSubmitForm extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Enumerated(EnumType.STRING)
    private ReservationStatus status;


    private Integer totalPrice;

    private boolean deleted;

    @Enumerated(EnumType.STRING)
    private PayMentStatus payMentStatus;

    @Builder.Default
    @BatchSize(size = 200)
    @OneToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "reservationSubmitForm")
    private List<ReservationAnswer> reservationAnswerList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder.Default
    @BatchSize(size = 100)
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "reservationForm")
    private List<Match> matches = new ArrayList<>();
    public void addAnswer(ReservationAnswer answer){
        answer.changeForm(this);
        this.reservationAnswerList.add(answer);
    }


    public void changeStatus(ReservationStatus status){
        this.status =status;

    }



}
