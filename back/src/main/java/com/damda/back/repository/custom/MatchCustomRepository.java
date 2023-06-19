package com.damda.back.repository.custom;

import com.damda.back.domain.Match;
import com.damda.back.domain.manager.Manager;

import java.util.List;
import java.util.Optional;

public interface MatchCustomRepository {
    public List<Manager> matchingManagerInfo(Long formId);
    Optional<Match> matchFindByReservationAndMember(Long reservationId, Long managerId);
    List<Match> matchList(Long reservationId);
    List<String> matchListFindManager(Long reservationId);

    public List<Match> matches(Long formId);
}
