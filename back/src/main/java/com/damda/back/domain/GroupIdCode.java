package com.damda.back.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

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


        public void connectSubmitFor(ReservationSubmitForm submitForm){
                this.submitForm = submitForm;
        }

        public List<String> nullCheckList(){
                List<String> strList = List.of(this.memberGroupId,this.managerGroupId,this.beforeAfterGroupId)
                        .stream().filter(s -> s != null).collect(Collectors.toList());

                return strList;
        }
}
