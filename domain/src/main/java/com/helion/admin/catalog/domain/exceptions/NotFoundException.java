package com.helion.admin.catalog.domain.exceptions;

import com.helion.admin.catalog.domain.AgregateRoot;
import com.helion.admin.catalog.domain.Identifier;
import com.helion.admin.catalog.domain.validation.Error;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class NotFoundException extends DomainException{

    protected NotFoundException(final String aMessage, final List<Error> anErrors){
        super(aMessage, anErrors);
    }

    public static NotFoundException with(
            final Class<? extends AgregateRoot<?>> anAgregate,
            final Identifier id
    ){
        final var anError = "%s with ID %s was not found".formatted(

                anAgregate.getSimpleName(),
                id.getValue()
        );
        return new NotFoundException(anError, Collections.emptyList());
    }
}
