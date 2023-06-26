package com.damda.back.data.request;

import com.damda.back.domain.manager.ManagerStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManagerStatusUpdateRequestDTO {
    private ManagerStatusEnum currManagerStatus;
}
