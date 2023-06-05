package com.damda.back.domain.area;

public interface AreaEnumExt extends AreaEnum {

    public <T extends Enum<T> & AreaEnum> T getParentCode();

}
