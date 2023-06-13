package com.damda.back.repository.custom;


import com.damda.back.domain.area.Area;
import com.damda.back.domain.manager.AreaManager;

import com.damda.back.domain.manager.Manager;

import java.util.List;

public interface ManagerCustomRepository {

    public List<Manager> managerWithArea(String addressFront);

    public List<Manager> managers(List<Long> ids);

    List<Manager> managerList();

}
