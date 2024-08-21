package com.helion.catalog.domain;


import com.helion.catalog.domain.castmember.CastMember;
import com.helion.catalog.domain.castmember.CastMemberType;
import com.helion.catalog.domain.category.Category;
import com.helion.catalog.domain.genre.Genre;
import com.helion.catalog.domain.utils.IdUtils;
import com.helion.catalog.domain.utils.InstantUtils;
import com.helion.catalog.domain.video.Rating;
import com.helion.catalog.domain.video.Video;
import net.datafaker.Faker;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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
    public static final class Categories{
        public static final Category aulas () {
            return Category.with(
                    UUID.randomUUID().toString().replace("-", ""),
                    "Aulas",
                    "Some Description",
                    true,
                    InstantUtils.now(),
                    InstantUtils.now(),
                    null);
        }

        public static final Category lives () {
            return Category.with(
                    UUID.randomUUID().toString().replace("-", ""),
                    "Lives",
                    "Some Description",
                    true,
                    InstantUtils.now(),
                    InstantUtils.now(),
                    null);
        }

        public static final Category talks () {
            return Category.with(
                    UUID.randomUUID().toString().replace("-", ""),
                    "Talks",
                    "content",
                    true,
                    InstantUtils.now(),
                    InstantUtils.now(),
                    InstantUtils.now());
        }


    }

    public static final class CastMembers {

        public static final CastMember actor(){

            return CastMember.with(
                    UUID.randomUUID().toString().replace("-", ""),
                    "Paulo Actor",
                    CastMemberType.ACTOR,
                    InstantUtils.now(),
                    InstantUtils.now()
            );
        }

        public static final CastMember director(){

            return CastMember.with(
                    UUID.randomUUID().toString().replace("-", ""),
                    "José Director",
                    CastMemberType.DIRECTOR,
                    InstantUtils.now(),
                    InstantUtils.now()
            );
        }

        public static CastMember actor2() {

            return CastMember.with(
                    UUID.randomUUID().toString().replace("-", ""),
                    "Mauro Actor",
                    CastMemberType.ACTOR,
                    InstantUtils.now(),
                    InstantUtils.now()
            );
        }
    }

    public static final class Genres {

        public static Genre tech() {
            return Genre.with(IdUtils.uniqueId(), "Technology",  Set.of("c456"), true, InstantUtils.now(), InstantUtils.now(), null);
        }

        public static Genre business() {
            return Genre.with(IdUtils.uniqueId(), "Business", new HashSet<>(), false, InstantUtils.now(), InstantUtils.now(), InstantUtils.now());
        }

        public static Genre marketing() {
            return Genre.with(IdUtils.uniqueId(), "Marketing", Set.of("c123"), true, InstantUtils.now(), InstantUtils.now(), null);
        }
    }

    public static final class Videos {

        public static Video systemDesign() {
            return Video.with(
                    IdUtils.uniqueId(),
                    "System Design no Mercado Livre na prática",
                    "O video mais assistido",
                    2022,
                    Fixture.duration(),
                    Rating.AGE_16.getName(),
                    true,
                    true,
                    InstantUtils.now().toString(),
                    InstantUtils.now().toString(),
                    "http://banner",
                    "http://thumb",
                    "http://thumb-half",
                    "http://trailer",
                    "http://video",
                    Set.of("aulas"),
                    Set.of("systemdesign"),
                    Set.of("luiz")

            );
        }

        public static Video java21() {
            return Video.with(
                    IdUtils.uniqueId(),
                    "Java 21",
                    "Java FTW",
                    2023,
                    Fixture.duration(),
                    Rating.AGE_10.getName(),
                    true,
                    true,
                    InstantUtils.now().toString(),
                    InstantUtils.now().toString(),
                    "http://banner",
                    "http://thumb",
                    "http://thumb-half",
                    "http://trailer",
                    "http://video",
                    Set.of("lives"),
                    Set.of("java"),
                    Set.of("gabriel")

            );
        }
        public static Video golang() {
            return Video.with(
                    IdUtils.uniqueId(),
                    "Golang 1.22",
                    "Um video de introdução a linguagem Go",
                    2024,
                    Fixture.duration(),
                    Rating.L.getName(),
                    true,
                    true,
                    InstantUtils.now().toString(),
                    InstantUtils.now().toString(),
                    "http://banner",
                    "http://thumb",
                    "http://thumb-half",
                    "http://trailer",
                    "http://video",
                    Set.of("meeting"),
                    Set.of("golang"),
                    Set.of("wesley")

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



    }

}
