package com.helion.admin.catalog;

import com.helion.admin.catalog.infrastructure.castmember.persistence.CastMemberRepository;
import com.helion.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import com.helion.admin.catalog.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.ApplicationContext;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;
import java.util.List;

public class MySQLCleanUpExtension implements BeforeEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) throws Exception{
        final var appContext = SpringExtension.getApplicationContext(context);

        cleanUp(List.of(
                appContext.getBean(GenreRepository.class),
                appContext.getBean(CategoryRepository.class),
                appContext.getBean(CastMemberRepository.class)
        ));


    }

    private void cleanUp(final Collection<CrudRepository> repositories){
        repositories.forEach(CrudRepository::deleteAll);
    }
}
