package com.damda.back.repository.custom;

import com.damda.back.domain.manager.Manager;

import java.util.List;

public interface MatchCustomRepository {
    public List<Manager> matchingManagerInfo(Long formId);
}
