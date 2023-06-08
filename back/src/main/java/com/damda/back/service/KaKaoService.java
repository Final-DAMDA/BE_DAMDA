package com.damda.back.service;

import com.damda.back.data.response.MemberResponseDTO;
import com.damda.back.data.response.TokenWithImageDTO;

public interface KaKaoService {
    public TokenWithImageDTO loginProcessing(String code);

}
