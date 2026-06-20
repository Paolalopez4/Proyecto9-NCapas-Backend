package com.grupo9.auto_repair_shop.util;

import org.mapstruct.factory.Mappers;

public final class MapperUtil {

    private MapperUtil() {}

    public static <T> T getMapper(
            Class<T> mapperClass
    ) {
        return Mappers.getMapper(mapperClass);
    }
}