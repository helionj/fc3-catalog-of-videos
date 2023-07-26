package com.helion.admin.catalog.domain;

import com.helion.admin.catalog.domain.event.DomainEvent;
import com.helion.admin.catalog.domain.utils.IdUtils;
import com.helion.admin.catalog.domain.utils.InstantUtils;
import com.helion.admin.catalog.domain.validation.ValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class EntityTest extends UnitTest{

    @Test
    public void givenNullAsEvents_whenInstantiate_shouldBeOk(){

        final List<DomainEvent> events = null;
        final var anEntity = new DummyEntity(new DummyID(), events);

        Assertions.assertNotNull(anEntity.getDomainEvents());
        Assertions.assertTrue(anEntity.getDomainEvents().isEmpty());
    }
    @Test
    public void givenDomainEvents_whenPassInConstructor_shouldCreateADefensiveClone(){

        final List<DomainEvent> events = new ArrayList<>();
        events.add(new DummyEvent());
        final var anEntity = new DummyEntity(new DummyID(), events);

        Assertions.assertNotNull(anEntity.getDomainEvents());
        Assertions.assertEquals(1, anEntity.getDomainEvents().size());
        Assertions.assertThrows(RuntimeException.class, () -> {
            final var actualEvents = anEntity.getDomainEvents();
            actualEvents.add(new DummyEvent());
        });
    }
    @Test
    public void givenEmptyDomainEvents_whenCallsRegisterEvent_shouldAddEventToList(){

        final var expectedEvents = 1;
        final var anEntity = new DummyEntity(new DummyID(), new ArrayList<>());

        anEntity.registerEvent(new DummyEvent());

        Assertions.assertNotNull(anEntity.getDomainEvents());
        Assertions.assertEquals(expectedEvents, anEntity.getDomainEvents().size());

    }
    @Test
    public void givenAFewDomainEvents_whenCallsPublisherEvents_shouldPublisherAndClearTheList(){

        final var expectedEvents = 0;
        final var anEntity = new DummyEntity(new DummyID(), new ArrayList<>());
        final var counter = new AtomicInteger(0);
        anEntity.registerEvent(new DummyEvent());
        anEntity.registerEvent(new DummyEvent());
        Assertions.assertEquals(2, anEntity.getDomainEvents().size());

        anEntity.publishDomainEvents(event -> {
            counter.incrementAndGet();
        });

        Assertions.assertNotNull(anEntity.getDomainEvents());
        Assertions.assertEquals(expectedEvents, anEntity.getDomainEvents().size());
        Assertions.assertEquals(2,counter.get());

    }

    public static class DummyEvent implements DomainEvent{

        @Override
        public Instant occurredOn() {
            return InstantUtils.now();
        }
    }

    public static class DummyID extends Identifier {

        private final String id;

        public DummyID(){
            this.id = IdUtils.uuid();
        }
        @Override
        public String getValue() {
            return this.id;
        }
    }

    public static class DummyEntity extends Entity<DummyID> {

        public DummyEntity(){
            this(new DummyID(), null);
        }

        protected DummyEntity(DummyID dummyID, List<DomainEvent> domainEvents) {
            super(dummyID, domainEvents);
        }

        @Override
        public void validate(ValidationHandler handler) {

        }
    }
}
