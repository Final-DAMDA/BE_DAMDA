package com.damda.back.data.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddCategoryRequestDTO {

        @Builder.Default
        private HashMap<String,Integer> data = new HashMap<>();
}
