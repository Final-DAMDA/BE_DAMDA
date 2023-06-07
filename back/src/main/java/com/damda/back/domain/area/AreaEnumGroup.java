package com.damda.back.domain.area;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

public class AreaEnumGroup {
    public static <T extends Enum<T> & AreaEnumExt, E extends Enum<E> & AreaEnum> List<T> getEnumByGroup(Class<T> enumClass, E parentCode) {
        return EnumSet.allOf(enumClass)
                .stream()
                .filter(type -> type.getParentCode().equals(parentCode))
                .collect(Collectors.toList());
    }

    public static <E extends Enum<E> & AreaEnum> List<E> getParentEnum(Class<E> enumClass) {
        return EnumSet.allOf(enumClass)
                .stream()
                .collect(Collectors.toList());
    }

}
