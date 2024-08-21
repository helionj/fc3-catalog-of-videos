package com.helion.catalog.domain.castmember;

import com.helion.catalog.domain.UnitTest;
import com.helion.catalog.domain.exceptions.DomainException;
import com.helion.catalog.domain.utils.InstantUtils;
import com.helion.catalog.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class CastMemberTest extends UnitTest {

    @Test
    public void givenAValidParamsWhenCallWith_thenInstantiateAnewCastMember() {
        final var expectedId = UUID.randomUUID().toString();
        final var expectedName = "Pedro Ator";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedDates = InstantUtils.now();

        final var actualMember = CastMember.with(expectedId,
               expectedName, expectedType, expectedDates, expectedDates);

        Assertions.assertNotNull(actualMember);
        Assertions.assertEquals(expectedId, actualMember.id());
        Assertions.assertEquals(expectedName, actualMember.name());
        Assertions.assertEquals(expectedType, actualMember.type());
        Assertions.assertEquals(expectedDates, actualMember.createdAt());
        Assertions.assertEquals(expectedDates, actualMember.updatedAt());

    }
    @Test
    public void givenAValidParamsWhenCallWithCastMember_thenInstantiateAnewCastMember() {

        final var expectedId = UUID.randomUUID().toString();
        final var expectedName = "Pedro Ator";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedDates = InstantUtils.now();

        final var aCastMember = CastMember.with(expectedId,
                expectedName, expectedType, expectedDates, expectedDates);
        final var actualMember = CastMember.with(aCastMember);

        Assertions.assertNotNull(actualMember);
        Assertions.assertEquals(aCastMember.id(), actualMember.id());
        Assertions.assertEquals(aCastMember.name(), actualMember.name());
        Assertions.assertEquals(aCastMember.type(), actualMember.type());
        Assertions.assertEquals(aCastMember.createdAt(), actualMember.createdAt());
        Assertions.assertEquals(aCastMember.updatedAt(), actualMember.updatedAt());

    }

    @Test
    public void givenAnInvalidNullNameWhenCallWithAndValidate_thenShouldReturnAnException() {
        final var expectedId = UUID.randomUUID().toString();
        final String expectedName = null;
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";


        final var expectedDates = InstantUtils.now();
        final var actualMember = CastMember.with(expectedId,
               expectedName, expectedType, expectedDates, expectedDates);

        final var actualException = Assertions.assertThrows(DomainException.class, () -> actualMember.validate(new ThrowsValidationHandler()));
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());


    }


    @Test
    public void givenAnInvalidEmptyNameWhenCallWithAndValidate_thenShouldReturnAnException() {
        final String expectedName = " ";
        final var expectedType = CastMemberType.ACTOR;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var expectedId = UUID.randomUUID().toString();
        final var expectedDates = InstantUtils.now();
        final var actualMember = CastMember.with(expectedId,
                expectedName, expectedType,expectedDates, expectedDates);

        final var actualException = Assertions.assertThrows(DomainException.class, () -> actualMember.validate(new ThrowsValidationHandler()));
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());


    }

    @Test
    public void givenAnInvalidNullIdWhenCallWithAndValidate_thenShouldReturnAnException() {
        final String expectedId = null;

        final var expectedName = "Paulo Ator";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'id' should not be empty";


        final var expectedDates = InstantUtils.now();

        final var actualMember = CastMember.with(expectedId,
                expectedName, expectedType, expectedDates, expectedDates);


        final var actualException = Assertions.assertThrows(DomainException.class, () -> actualMember.validate(new ThrowsValidationHandler()));
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());


    }


    @Test
    public void givenAnInvalidEmptyIdWhenCallWithAndValidate_thenShouldReturnAnException() {
        final var expectedId = " ";
        final var expectedName = "Paulo Ator";
        final var expectedType = CastMemberType.ACTOR;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'id' should not be empty";


        final var expectedDates = InstantUtils.now();
        final var actualMember = CastMember.with(expectedId,
                expectedName, expectedType, expectedDates, expectedDates);

        final var actualException = Assertions.assertThrows(DomainException.class, () -> actualMember.validate(new ThrowsValidationHandler()));
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());


    }

    @Test
    public void givenAnInvalidNullTypeWhenCallWithAndValidate_thenShouldReturnAnException() {
        final var expectedId = UUID.randomUUID().toString();
        final var expectedName = "Paulo Ator";
        final CastMemberType expectedType = null;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'type' should not be null";


        final var expectedDates = InstantUtils.now();
        final var actualMember = CastMember.with(expectedId,
                expectedName, expectedType, expectedDates, expectedDates);

        final var actualException = Assertions.assertThrows(DomainException.class, () -> actualMember.validate(new ThrowsValidationHandler()));
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());


    }





}
