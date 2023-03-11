package com.helion.admin.catalog.infrastructure.utils;

import org.springframework.data.jpa.domain.Specification;

public class SpecificationUtils {

    private SpecificationUtils(){};

    public static <T> Specification<T> like(final String prop, final String term){
        return (root, query, cb) ->
                cb.like(cb.upper(root.get(prop)), like(term.toUpperCase()));
    }

    private static String like(String term) {
        return "%" + term + "%";
    }
}
