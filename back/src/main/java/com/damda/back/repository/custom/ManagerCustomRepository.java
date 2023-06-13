package com.damda.back.repository.custom;

import com.damda.back.domain.area.Area;
import com.damda.back.domain.manager.AreaManager;
import com.damda.back.domain.manager.Manager;
import com.damda.back.domain.manager.ManagerStatusEnum;

import java.util.List;

public interface ManagerCustomRepository {

    public List<Manager> managerWithArea(String addressFront);

    public List<Manager> managers(List<Long> ids);

    public List<Manager> managerList(ManagerStatusEnum managerStatusEnum);

}
