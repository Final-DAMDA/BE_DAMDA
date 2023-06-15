package com.damda.back.service;

import com.damda.back.data.request.AdminLoginRequestDTO;

public interface AdminService {
    public String loginProcessing(AdminLoginRequestDTO dto);
}
