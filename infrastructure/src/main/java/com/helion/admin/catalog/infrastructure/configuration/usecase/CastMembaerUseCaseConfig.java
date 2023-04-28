package com.helion.admin.catalog.infrastructure.configuration.usecase;

import com.helion.admin.catalog.application.castmember.create.CreateCastMemberUseCase;
import com.helion.admin.catalog.application.castmember.create.DefaultCreateCastMemberUseCase;
import com.helion.admin.catalog.application.castmember.delete.DefaultDeleteCastMemberUseCase;
import com.helion.admin.catalog.application.castmember.delete.DeleteCastMemberUseCase;
import com.helion.admin.catalog.application.castmember.retrieve.get.DefaultGetCastMemberByIdUseCase;
import com.helion.admin.catalog.application.castmember.retrieve.get.GetCastMemberByIdUseCase;
import com.helion.admin.catalog.application.castmember.retrieve.list.DefaultListCastMemberUseCase;
import com.helion.admin.catalog.application.castmember.retrieve.list.ListCastMemberUseCase;
import com.helion.admin.catalog.application.castmember.update.DefaultUpdateCastMemberUseCase;
import com.helion.admin.catalog.application.castmember.update.UpdateCastMemberUseCase;
import com.helion.admin.catalog.domain.castmember.CastMemberGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class CastMembaerUseCaseConfig {

    private final CastMemberGateway castMemberGateway;

    public CastMembaerUseCaseConfig(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Bean
    public UpdateCastMemberUseCase updateCastMemberUseCase(){
        return new DefaultUpdateCastMemberUseCase(this.castMemberGateway);
    }

    @Bean
    public CreateCastMemberUseCase createCastMemberUseCase(){
        return new DefaultCreateCastMemberUseCase(this.castMemberGateway);
    }
    @Bean
    public DeleteCastMemberUseCase deleteCastMemberUseCase(){
        return new DefaultDeleteCastMemberUseCase(this.castMemberGateway);
    }
    @Bean
    public GetCastMemberByIdUseCase getCastMemberByIdUseCase(){
        return new DefaultGetCastMemberByIdUseCase(this.castMemberGateway);
    }
    @Bean
    public ListCastMemberUseCase listCastMemberUseCase(){
        return new DefaultListCastMemberUseCase(this.castMemberGateway);
    }
}
