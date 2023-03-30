package com.helion.admin.catalog.application.category.update;

import com.helion.admin.catalog.application.UseCaseTest;
import com.helion.admin.catalog.domain.category.Category;
import com.helion.admin.catalog.domain.category.CategoryGateway;
import com.helion.admin.catalog.domain.category.CategoryID;
import com.helion.admin.catalog.domain.exceptions.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

public class UpdateCategoryUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultUpdateCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(categoryGateway);
    }

    @Test
    public void givenValidCommand_whenCallsUpdateCategory_shouldReturnCategoryID(){
        final var aCategory = Category.newCategory("Movies", null, true);

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedId = aCategory.getId();

        final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);


        when(categoryGateway.findById(eq(expectedId))).thenReturn(Optional.of(aCategory.clone()));

        when(categoryGateway.update(any())).thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(categoryGateway, times(1)).findById(eq(expectedId));
        Mockito.verify(categoryGateway, times(1)).update(argThat(aUpdatedCategory -> {
            return Objects.equals(expectedName, aUpdatedCategory.getName())
                    && Objects.equals(expectedId, aUpdatedCategory.getId())
                    && Objects.equals(expectedDescription, aUpdatedCategory.getDescription())
                    && Objects.equals(expectedIsActive, aUpdatedCategory.isActive())
                    && Objects.equals(aCategory.getCreatedAt(), aUpdatedCategory.getCreatedAt())
                    && aCategory.getCreatedAt().isBefore(aUpdatedCategory.getUpdatedAt())
                    && Objects.isNull(aUpdatedCategory.getDeletedAt());
        }));

    }
    @Test
    public void givenInValidName_whenCallsUpdateCategory_shouldDomainException(){
        final var aCategory = Category.newCategory("Movies", null, true);
        final String  expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;
        final var expectedId = aCategory.getId();

        final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        when(categoryGateway.findById(eq(expectedId))).thenReturn(Optional.of(aCategory.clone()));

        final var notification = useCase.execute(aCommand).getLeft();
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());
        Mockito.verify(categoryGateway, times(0)).update(any());
    }

    @Test
    public void givenValidCommand_whenCallsUpdateCategoryWithInactiveCategory_shouldReturnInactiveCategoryID(){
        final var aCategory = Category.newCategory("Movies", null, true);

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;
        final var expectedId = aCategory.getId();

        final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);


        when(categoryGateway.findById(eq(expectedId))).thenReturn(Optional.of(aCategory.clone()));

        when(categoryGateway.update(any())).thenAnswer(returnsFirstArg());

        Assertions.assertTrue(aCategory.isActive());
        Assertions.assertNull(aCategory.getDeletedAt());

        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(categoryGateway, times(1)).findById(eq(expectedId));
        Mockito.verify(categoryGateway, times(1)).update(argThat(aUpdatedCategory -> {
            return Objects.equals(expectedName, aUpdatedCategory.getName())
                    && Objects.equals(expectedId, aUpdatedCategory.getId())
                    && Objects.equals(expectedDescription, aUpdatedCategory.getDescription())
                    && Objects.equals(expectedIsActive, aUpdatedCategory.isActive())
                    && Objects.equals(aCategory.getCreatedAt(), aUpdatedCategory.getCreatedAt())
                    && aCategory.getCreatedAt().isBefore(aUpdatedCategory.getUpdatedAt())
                    && Objects.nonNull(aUpdatedCategory.getDeletedAt());
        }));

    }

    @Test
    public void givenValidCommand_whenGatewayThrowsRandomException_shouldReturnAnException(){
        final var aCategory = Category.newCategory("Movies", null, true);
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "Gateway Error";
        final var expectedErrorCount = 1;
        final var expectedId = aCategory.getId();

        final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        when(categoryGateway.findById(eq(expectedId))).thenReturn(Optional.of(aCategory.clone()));
        when(categoryGateway.update(any())).thenThrow(new IllegalStateException("Gateway Error"));


        final var notification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());
        Mockito.verify(categoryGateway, times(1)).update(argThat(aUpdatedCategory -> {
            return Objects.equals(expectedName, aUpdatedCategory.getName())
                    && Objects.equals(expectedId, aUpdatedCategory.getId())
                    && Objects.equals(expectedDescription, aUpdatedCategory.getDescription())
                    && Objects.equals(expectedIsActive, aUpdatedCategory.isActive())
                    && Objects.equals(aCategory.getCreatedAt(), aUpdatedCategory.getCreatedAt())
                    && aCategory.getCreatedAt().isBefore(aUpdatedCategory.getUpdatedAt())
                    && Objects.isNull(aUpdatedCategory.getDeletedAt());
        }));
    }

    @Test
    public void givenValidCommandWithInvalidId_whenCallsUpdateCategory_shouldReturnNotFoundException(){

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;
        final var expectedId = "123";
        final var expectedErrorMessage = "Category with ID %s was not found".formatted(expectedId);
        final var expectedErrorCount = 1;

        final var aCommand = UpdateCategoryCommand.with(expectedId, expectedName, expectedDescription, expectedIsActive);


        when(categoryGateway.findById(eq(CategoryID.from(expectedId)))).thenReturn(Optional.empty());



        final var actualException = Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(aCommand));
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(categoryGateway, times(1)).findById(eq(CategoryID.from(expectedId)));
        Mockito.verify(categoryGateway, times(0)).update(any());

    }
}
