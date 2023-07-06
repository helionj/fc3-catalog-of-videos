package com.helion.admin.catalog.domain;

import com.helion.admin.catalog.domain.event.DomainEvent;
import com.helion.admin.catalog.domain.event.DomainEventPublisher;
import com.helion.admin.catalog.domain.validation.ValidationHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class Entity<ID extends Identifier> {

    protected final ID id;

    private final List<DomainEvent> domainEvents;


    protected Entity(final ID id, final List<DomainEvent> domainEvents) {
        this.domainEvents = new ArrayList<>(domainEvents == null ? Collections.emptyList() : domainEvents);
        Objects.requireNonNull(id, " 'id' should not be null");
        this.id = id;
    }

    public ID getId() {
        return id;
    }

    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    public void publishDomainEvents(final DomainEventPublisher publisher){
        if (publisher == null){
            return;
        }
        getDomainEvents().forEach(publisher::publishEvent);
        this.domainEvents.clear();
    }

    public void registerEvent(final DomainEvent event){
        if (event != null) {
            this.domainEvents.add(event);
        }

    }

    public abstract void validate(ValidationHandler handler);

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Entity<?> entity = (Entity<?>) o;
        return getId().equals(entity.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
