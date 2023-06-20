package com.damda.back.repository.custom;

import com.damda.back.data.response.UserResponseDTO;
import com.damda.back.domain.Member;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface MemberCustomRepository {
    public Optional<Member> findByPhoneNumber(String phoneNumber);

    public Optional<Member> findByIdWhereActive(Integer id);
    public boolean existCode(String code);


    public Optional<Member> findByAdmin(String username);

    public List<UserResponseDTO> findByMemberListWithCode();


}
