package com.helion.admin.catalog.application;

import com.github.javafaker.Faker;
import com.helion.admin.catalog.domain.castmember.CastMemberType;

public final class Fixture {

    private final static Faker FAKER = new Faker();

    public static String name(){
        return FAKER.name().fullName();
    }

    public static final class CastMember {

        public static CastMemberType type() {
            return FAKER.options()
                    .option(CastMemberType.ACTOR, CastMemberType.DIRECTOR);
        }
    }
}
