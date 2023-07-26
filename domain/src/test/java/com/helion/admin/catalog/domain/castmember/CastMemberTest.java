package com.helion.admin.catalog.domain.castmember;

import com.helion.admin.catalog.domain.UnitTest;
import com.helion.admin.catalog.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CastMemberTest extends UnitTest {

    @Test
    public void givenAValidParamsWhenCallNewCastMember_thenInstantiateAnewCastMember() {
        final var expectedName = "Vin Diesel";
        final var expectedType = CastMemberType.ACTOR;
        final var actualCastMember = CastMember.newMember(expectedName, expectedType);

        Assertions.assertNotNull(actualCastMember);
        Assertions.assertNotNull(actualCastMember.getId());
        Assertions.assertEquals(expectedName, actualCastMember.getName());
        Assertions.assertEquals(expectedType, actualCastMember.getType());
        Assertions.assertNotNull(actualCastMember.getCreatedAt());
        Assertions.assertNotNull(actualCastMember.getUpdatedAt());

    }
    @Test
    public void givenAnInValidNullName_whenCallNewCastMemberAndValidate_shouldReceiveAnError() {
        final String expectedName = null;
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";


        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            CastMember.newMember(expectedName, expectedType);
        });

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAnEmptyName_whenCallNewGenreAndValidate_shouldReceiveAnError() {
        final var expectedName = "";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";


        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            CastMember.newMember(expectedName, expectedType);
        });

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidNameGreaterThan255_whenCallNewMemberAndValidate_shouldReceiveAnError() {
        final var expectedName = """
                É importante questionar o quanto o desafiador cenário globalizado afeta positivamente a 
                correta previsão do remanejamento dos quadros funcionais. Todas estas questões, devidamente 
                ponderadas, levantam dúvidas sobre se a revolução dos costumes representa uma abertura ponderada
                """;
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must between 3 and 255 characters";


        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            CastMember.newMember(expectedName, expectedType);
        });

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInValidNullType_whenCallNewCastMember_shouldReceiveAnError() {

        final String expectedName = "Vin Diesel";
        final CastMemberType expectedType = null;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'type' should not be null";


        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            CastMember.newMember(expectedName, expectedType);
        });

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    }

    @Test
    public void givenAValidCastMember_whenCallUpdate_shouldReceiveAUpdated(){

        final var expectedName = "Vin Diesel";
        final var expectedType = CastMemberType.ACTOR;
        final var actualCastMember = CastMember.newMember("vinny", CastMemberType.DIRECTOR);

        Assertions.assertNotNull(actualCastMember);
        Assertions.assertNotNull(actualCastMember.getId());

        final var actualId = actualCastMember.getId();
        final var actualCreatedAt = actualCastMember.getCreatedAt();
        final var actualUpdatedAt = actualCastMember.getUpdatedAt();

        actualCastMember.update(expectedName, expectedType);

        Assertions.assertNotNull(actualCastMember);
        Assertions.assertEquals(actualId, actualCastMember.getId());
        Assertions.assertEquals(expectedName, actualCastMember.getName());
        Assertions.assertEquals(expectedType, actualCastMember.getType());
        Assertions.assertEquals(actualCreatedAt, actualCastMember.getCreatedAt());
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualCastMember.getUpdatedAt()));

    }

    @Test
    public void givenAValidCastMember_whenCallUpdateWithNullName_shouldReceiveANotification(){

        final String expectedName = null;
        final var expectedType = CastMemberType.ACTOR;
        final var actualCastMember = CastMember.newMember("vinny", CastMemberType.DIRECTOR);
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        Assertions.assertNotNull(actualCastMember);
        Assertions.assertNotNull(actualCastMember.getId());

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            actualCastMember.update(expectedName, expectedType);
        });

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    }
    @Test
    public void givenAValidCastMember_whenCallUpdateWithEmptyName_shouldReceiveANotification(){

        final var expectedName = " ";
        final var expectedType = CastMemberType.ACTOR;
        final var actualCastMember = CastMember.newMember("vinny", CastMemberType.DIRECTOR);
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        Assertions.assertNotNull(actualCastMember);
        Assertions.assertNotNull(actualCastMember.getId());

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            actualCastMember.update(expectedName, expectedType);
        });

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    }

    @Test
    public void givenAValidCastMember_whenCallUpdateWithInvalidNameGreaterThan255_shouldReceiveANotification(){

        final var expectedName = """
                É importante questionar o quanto o desafiador cenário globalizado afeta positivamente a 
                correta previsão do remanejamento dos quadros funcionais. Todas estas questões, devidamente 
                ponderadas, levantam dúvidas sobre se a revolução dos costumes representa uma abertura ponderada
                """;
        final var expectedType = CastMemberType.ACTOR;
        final var actualCastMember = CastMember.newMember("vinny", CastMemberType.DIRECTOR);
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must between 3 and 255 characters";

        Assertions.assertNotNull(actualCastMember);
        Assertions.assertNotNull(actualCastMember.getId());

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            actualCastMember.update(expectedName, expectedType);
        });

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    }


    @Test
    public void givenAValidCastMember_whenCallUpdateWithNullType_shouldReceiveANotification(){

        final var expectedName = "Vin Diesel";
        final CastMemberType expectedType = null;
        final var actualCastMember = CastMember.newMember("vinny", CastMemberType.DIRECTOR);
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'type' should not be null";

        Assertions.assertNotNull(actualCastMember);
        Assertions.assertNotNull(actualCastMember.getId());

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            actualCastMember.update(expectedName, expectedType);
        });

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    }



}
