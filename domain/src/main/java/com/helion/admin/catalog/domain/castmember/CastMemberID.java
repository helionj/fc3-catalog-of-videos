package com.helion.admin.catalog.domain.castmember;

import com.helion.admin.catalog.domain.Identifier;
import com.helion.admin.catalog.domain.utils.IdUtils;

import java.util.Objects;

public class CastMemberID extends Identifier {

    private final String value;

    public CastMemberID(String value) {
        this.value = Objects.requireNonNull(value);
    }

    public static CastMemberID unique(){
        return CastMemberID.from(IdUtils.uuid());
    }

    public static CastMemberID from(final String anId) {
        return new CastMemberID(anId);
    }


    @Override
    public String getValue() {
        return this.value;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CastMemberID that = (CastMemberID) o;
        return getValue().equals(that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
