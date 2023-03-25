package com.helion.admin.catalog.domain.genre;

import com.helion.admin.catalog.domain.validation.Error;
import com.helion.admin.catalog.domain.validation.ValidationHandler;
import com.helion.admin.catalog.domain.validation.Validator;

public class GenreValidator extends Validator{

    public static final int NAME_MAX_LENGTH = 255;
    public static final int NAME_MIN_LENGTH = 3;
    private final Genre genre;

    public GenreValidator(final Genre genre, final ValidationHandler aHandler) {
        super(aHandler);
        this.genre = genre;
    }

    @Override
    public void validate() {
        checkNameConstraints();
    }

    private void checkNameConstraints() {
        final var name = this.genre.getName();
        if(name == null){
            this.validationHandler().append(new Error("'name' should not be null"));
            return;
        }
        if(name.isBlank()){
            this.validationHandler().append(new Error("'name' should not be empty"));
            return;
        }

        final var length = name.trim().length();
        if(length < NAME_MIN_LENGTH || length> NAME_MAX_LENGTH){
            this.validationHandler().append(new Error("'name' must between 3 and 255 characters"));
        }
    }
}
