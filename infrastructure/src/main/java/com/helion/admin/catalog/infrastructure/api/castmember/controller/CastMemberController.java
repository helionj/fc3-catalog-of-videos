package com.helion.admin.catalog.infrastructure.api.castmember.controller;

import com.helion.admin.catalog.application.castmember.create.CreateCastMemberCommand;
import com.helion.admin.catalog.application.castmember.create.CreateCastMemberUseCase;
import com.helion.admin.catalog.application.castmember.delete.DeleteCastMemberUseCase;
import com.helion.admin.catalog.application.castmember.retrieve.get.GetCastMemberByIdUseCase;
import com.helion.admin.catalog.application.castmember.retrieve.list.ListCastMemberUseCase;
import com.helion.admin.catalog.application.castmember.update.UpdateCastMemberCommand;
import com.helion.admin.catalog.application.castmember.update.UpdateCastMemberUseCase;
import com.helion.admin.catalog.domain.category.pagination.Pagination;
import com.helion.admin.catalog.domain.category.pagination.SearchQuery;
import com.helion.admin.catalog.infrastructure.api.castmember.CastMemberAPI;
import com.helion.admin.catalog.infrastructure.castmember.models.CastMemberListResponse;
import com.helion.admin.catalog.infrastructure.castmember.models.CastMemberResponse;
import com.helion.admin.catalog.infrastructure.castmember.models.CreateCastMemberRequest;
import com.helion.admin.catalog.infrastructure.castmember.models.UpdateCastMemberRequest;
import com.helion.admin.catalog.infrastructure.castmember.presenters.CastMemberApiPresenter;
import com.helion.admin.catalog.infrastructure.genre.presenters.GenreApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class CastMemberController implements CastMemberAPI {

    private final GetCastMemberByIdUseCase getCastMemberByIdUseCase;

    private final CreateCastMemberUseCase createCastMemberUseCase;

    private final UpdateCastMemberUseCase updateCastMemberUseCase;

    private final DeleteCastMemberUseCase deleteCastMemberUseCase;

    private final ListCastMemberUseCase listCastMemberUseCase;

    public CastMemberController(final GetCastMemberByIdUseCase getCastMemberByIdUseCase,
                                final CreateCastMemberUseCase createCastMemberUseCase,
                                final UpdateCastMemberUseCase updateCastMemberUseCase,
                                final DeleteCastMemberUseCase deleteCastMemberUseCase,
                                final ListCastMemberUseCase listCastMemberUseCase) {
        this.getCastMemberByIdUseCase = Objects.requireNonNull(getCastMemberByIdUseCase);
        this.createCastMemberUseCase = Objects.requireNonNull(createCastMemberUseCase);
        this.updateCastMemberUseCase = Objects.requireNonNull(updateCastMemberUseCase);
        this.deleteCastMemberUseCase = Objects.requireNonNull(deleteCastMemberUseCase);
        this.listCastMemberUseCase = Objects.requireNonNull(listCastMemberUseCase);
    }

    @Override
    public ResponseEntity<?> createCastMember(CreateCastMemberRequest input) {
       final var aCommand = CreateCastMemberCommand.with(input.name(), input.type());

       final var output = this.createCastMemberUseCase.execute(aCommand);
       return ResponseEntity.created(URI.create("/cast_members/"+output.id())).body(output);
    }

    @Override
    public Pagination<CastMemberListResponse> listCastMembers(String search, int page, int perPage, String sort, String direction) {
        return listCastMemberUseCase.execute(new SearchQuery(page, perPage, search, sort, direction))
                .map(CastMemberApiPresenter::present);
    }

    @Override
    public CastMemberResponse getById(String id) {
        return CastMemberApiPresenter.present(this.getCastMemberByIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> updateById(String id, UpdateCastMemberRequest input) {
        final var aCommand = UpdateCastMemberCommand.with(
                id,
                input.name(),
                input.type()
        );
        final var output = this.updateCastMemberUseCase.execute(aCommand);
        return ResponseEntity.ok().body(output);
    }

    @Override
    public void deleteById(String id) {
        this.deleteCastMemberUseCase.execute(id);
    }
}
