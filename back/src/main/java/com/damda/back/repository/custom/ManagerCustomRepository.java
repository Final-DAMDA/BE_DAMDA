package com.damda.back.repository.custom;

import com.damda.back.domain.Member;
import com.damda.back.domain.area.Area;
import com.damda.back.domain.manager.AreaManager;
import com.damda.back.domain.manager.Manager;
import com.damda.back.domain.manager.ManagerStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ManagerCustomRepository {

    public List<Manager> managerWithArea(String addressFront);

    public List<Manager> managers(List<Long> ids);

    public Page<Manager> managerList(ManagerStatusEnum managerStatusEnum, Pageable pageable);

    String findManagerName(Integer memberId);
    Optional<Manager> findManager(Integer memberId);

    Optional<Manager> findMangerWithAreaManger(Long memberId);

    public Area findByAreaManager(String dist);


    public List<AreaManager> areaList(List<AreaManager.AreaManagerKey> areaManagers);

    public List<AreaManager> areaList2(List<AreaManager> areaManagers);


}
