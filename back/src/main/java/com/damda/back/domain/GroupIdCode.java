package com.damda.back.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GroupIdCode {

        @Id
        @Column(name = "form_id")
        private Long formId;

        @OneToOne
        @MapsId
        @JoinColumn(name = "form_id", referencedColumnName = "id")
        private ReservationSubmitForm submitForm;

        private String memberGroupId;

        private String managerGroupId;

        private String beforeAfterGroupId;

}
