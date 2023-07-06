package com.helion.admin.catalog.domain;

import com.helion.admin.catalog.domain.event.DomainEvent;

import java.util.Collections;
import java.util.List;

public abstract class AgregateRoot<ID extends Identifier> extends Entity<ID> {

    protected AgregateRoot(final ID id) {
        this(id, Collections.emptyList());
    }

    protected AgregateRoot(final ID id, final List<DomainEvent> domainEvents) {
        super(id, domainEvents);
    }
}
