package com.helion.admin.catalog.domain.castmember;

import com.helion.admin.catalog.domain.validation.Error;
import com.helion.admin.catalog.domain.validation.ValidationHandler;
import com.helion.admin.catalog.domain.validation.Validator;

import java.util.Objects;

public class CastMemberValidator extends Validator {

    public static final int NAME_MAX_LENGTH = 255;
    public static final int NAME_MIN_LENGTH = 3;

    private final CastMember castMember;

    public CastMemberValidator(final CastMember castMember, final ValidationHandler aHandler) {
        super(aHandler);
        this.castMember = Objects.requireNonNull(castMember);
    }

    @Override
    public void validate() {
        checkNameConstraints();
        checkTypeConstraints();
    }

    private void checkNameConstraints() {
        final var name = this.castMember.getName();
        if (name == null) {
            this.validationHandler().append(new Error("'name' should not be null"));
            return;
        }
        if (name.isBlank()) {
            this.validationHandler().append(new Error("'name' should not be empty"));
            return;
        }


        final var length = name.trim().length();
        if (length < NAME_MIN_LENGTH || length > NAME_MAX_LENGTH) {
            this.validationHandler().append(new Error("'name' must between 3 and 255 characters"));
        }
    }

    private void checkTypeConstraints(){
        if(this.castMember.getType() == null){
            this.validationHandler().append(new Error("'type' should not be null"));
            return;
        }
    }
}
