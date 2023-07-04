package com.helion.admin.catalog.domain;

import com.github.javafaker.Faker;
import com.helion.admin.catalog.domain.castmember.CastMember;
import com.helion.admin.catalog.domain.castmember.CastMemberType;
import com.helion.admin.catalog.domain.category.Category;
import com.helion.admin.catalog.domain.genre.Genre;
import com.helion.admin.catalog.domain.utils.IdUtils;
import com.helion.admin.catalog.domain.video.*;
import com.helion.admin.catalog.domain.resource.Resource;

import java.time.Year;
import java.util.Set;

import static io.vavr.API.*;

public final class Fixture {

    private final static Faker FAKER = new Faker();

    public static String name(){
        return FAKER.name().fullName();
    }

    public static int year(){
        return FAKER.random().nextInt(2020,2030);
    }

    public static boolean bool(){
        return FAKER.bool().bool();
    }
    public static double duration(){
        return FAKER.options().option(120.0, 45.0, 25.5, 15.5, 90.0);
    }

    public static String title(){
        return FAKER.options()
                .option("System Design no Mercado Livre na prática",
                        "Não cometa esses errors ao trabalhar com microserviços",
                        "Testes de Mutação. Voce não testa o seu software corretamente");
    }

    public static String checksum() {
        return "03fe62de";
    }

    public static Video video() {
        return Video.newVideo(
                Fixture.title(),
                Videos.description(),
                Year.of(Fixture.year()),
                Fixture.duration(),
                Fixture.bool(),
                Fixture.bool(),
                Videos.rating(),
                Set.of(Categories.aulas().getId()),
                Set.of(Genres.tech().getId()),
                Set.of(CastMembers.wesley().getId(), CastMembers.helion().getId())
        );
    }

    public static final class Categories{
        private static final Category AULAS = Category.newCategory("Aulas", "Some Description", true);

        private static final Category LIVES = Category.newCategory("Lives", "Some Description", true);
        public static Category aulas(){
            return AULAS.clone();
        }

        public static Category lives() {return LIVES.clone();}
    }

    public static final class Genres{
        private static final Genre TECH = Genre.newGenre("Technology", true);
        private static final Genre BUSINESS = Genre.newGenre("Business", true);

        public static Genre tech(){
            return Genre.with(TECH);
        }

        public static Genre business() { return Genre.with(BUSINESS);}

    }

    public static final class CastMembers {

        private static final CastMember WESLEY =
                CastMember.newMember("Wesley Fullcycle", CastMemberType.ACTOR);
        private static final CastMember HELION =
                CastMember.newMember("Helion Porto", CastMemberType.ACTOR);
        public static CastMemberType type() {
            return FAKER.options()
                    .option(CastMemberType.ACTOR, CastMemberType.DIRECTOR);
        }

        public static CastMember wesley() {
            return CastMember.with(WESLEY);
        }

        public static CastMember helion() {
            return CastMember.with(HELION);
        }
    }

    public static final class Videos {

        public static Video systemDesign() {
            return Video.newVideo(
                    Fixture.title(),
                    description(),
                    Year.of(Fixture.year()),
                    Fixture.duration(),
                    Fixture.bool(),
                    Fixture.bool(),
                    rating(),
                    Set.of(Categories.aulas().getId()),
                    Set.of(Genres.tech().getId()),
                    Set.of(CastMembers.wesley().getId(), CastMembers.helion().getId())
            );
        }
        public static String description(){
            return FAKER.options()
                    .option("""
            Lets design the high-level architecture of youtube - similar to 
            how we'd tackle this in a system design interview.
            """,
            """
            Nesse vídeo você entenderá o que aconteceu na Amazon Prime Video que os fizeram sair dos microsserviços.
            Link do artigo:
            https://www.primevideotech.com/video-...
            """);
        }

        public static Rating rating(){

            return FAKER.options().option(Rating.values());
        }
        public static VideoMediaType mediaType() {
            return FAKER.options().option(VideoMediaType.values());
        }


        public static Resource resource(final VideoMediaType type){
            final String contentType = Match(type).of(
                    Case($(List(VideoMediaType.VIDEO, VideoMediaType.TRAILER)::contains), "video/mp4"),
                            Case($(), "image/jpg")
                    );

            final byte[] content = "Conteudo".getBytes();
            final String checksum = IdUtils.uuid();

            return Resource.with(checksum, content, contentType, type.name().toLowerCase());
        }

        public static AudioVideoMedia audioVideo(final VideoMediaType type) {
            final var checksum = Fixture.checksum();
            return AudioVideoMedia.with(
                    checksum,
                    type.name().toLowerCase(),
                    "/videos/" + checksum
            );
        }

        public static ImageMedia image(final VideoMediaType type) {
            final var checksum = Fixture.checksum();
            return ImageMedia.with(
                    checksum,
                    type.name().toLowerCase(),
                    "/images/" + checksum
            );
        }


    }
}
