package com.helion.catalog.domain.utils;


import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.sql.Timestamp;

public final class InstantUtils {

    public static final int UNIX_PRECISION = 1_000;
    public InstantUtils(){}
    

    public static Instant now(){
        return Instant.now().truncatedTo(ChronoUnit.MILLIS);
    }

    public static Instant fromTimestamp(final Long timestamp) {
        if (timestamp == null) {
            return null;
        }

        return new Timestamp(timestamp / UNIX_PRECISION).toInstant();
    }
}
