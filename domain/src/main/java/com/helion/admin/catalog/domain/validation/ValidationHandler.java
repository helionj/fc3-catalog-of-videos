package com.helion.admin.catalog.domain.validation;

import java.util.List;

public interface ValidationHandler {

    ValidationHandler append(Error anError);

    ValidationHandler append(ValidationHandler anHandler);

    ValidationHandler validate(Validation aValidation);

    List<Error> getErrors();

    default Error firstError() {
        if(getErrors() != null && !getErrors().isEmpty()){
            return getErrors().get(0);
        }else {
            return null;
        }
    }
    default boolean hasErrors(){
        return getErrors() != null  && !getErrors().isEmpty();
    }
    interface Validation {
        void validate();
    }
}
