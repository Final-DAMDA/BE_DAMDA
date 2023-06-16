package com.damda.back.data.request;


import lombok.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddCategoryRequestDTO {

        private List<String> data = new ArrayList<>();
}
