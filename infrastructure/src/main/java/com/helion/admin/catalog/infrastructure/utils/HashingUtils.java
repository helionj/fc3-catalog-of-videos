package com.helion.admin.catalog.infrastructure.utils;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

public class HashingUtils {

    public static final HashFunction CHECKSUM = Hashing.crc32();
    private HashingUtils(){}

    public static String checksum(final byte[] content ){
        return CHECKSUM.hashBytes(content).toString();
    }
}
