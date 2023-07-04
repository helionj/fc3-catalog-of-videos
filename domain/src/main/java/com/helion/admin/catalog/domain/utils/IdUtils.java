package com.helion.admin.catalog.domain.utils;

import java.util.Locale;
import java.util.UUID;

public final class IdUtils {

    private IdUtils(){}

    public static final String uuid(){
        return UUID.randomUUID().toString().toLowerCase(Locale.ROOT).replace("-", "");
    }
}
