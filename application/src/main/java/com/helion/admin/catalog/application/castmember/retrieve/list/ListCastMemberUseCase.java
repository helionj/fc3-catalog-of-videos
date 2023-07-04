package com.helion.admin.catalog.application.castmember.retrieve.list;

import com.helion.admin.catalog.application.UseCase;
import com.helion.admin.catalog.domain.category.pagination.Pagination;
import com.helion.admin.catalog.domain.category.pagination.SearchQuery;

public abstract class ListCastMemberUseCase
        extends UseCase<SearchQuery, Pagination<CastMemberListOutput>> {}
