package com.helion.catalog.domain.utils;

import java.util.UUID;

public final class IdUtils {

    private IdUtils(){}

    public final static String uniqueId(){
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }
}
