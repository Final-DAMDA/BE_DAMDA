package com.damda.back.domain;


import com.damda.back.data.common.QuestionIdentify;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ReservationAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String answer;

    @Enumerated(EnumType.STRING)
    private QuestionIdentify questionIdentify;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id")
    private ReservationSubmitForm reservationSubmitForm;

    @Transient
    private Long formId;

    public void changeForm(ReservationSubmitForm reservationSubmitForm) {this.reservationSubmitForm = reservationSubmitForm;}
}
