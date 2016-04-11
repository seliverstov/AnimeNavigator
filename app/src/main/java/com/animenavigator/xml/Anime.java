package com.animenavigator.xml;

import org.simpleframework.xml.*;

import java.util.Collection;

/**
 * Created by a.g.seliverstov on 10.03.2016.
 */
@Root
public class Anime extends Manga{

    @ElementList(entry = "episode", inline = true, required = false, type = Episode.class)
    public Collection<Episode> episodes;

    @ElementList(entry = "cast", inline = true, required = false, type = Cast.class)
    public Collection<Cast> casts;

    public static class Episode {
        @Attribute
        public String num;

        @ElementList(entry = "title", inline = true, type = Title.class)
        public Collection<Title> titles;

        public static class Title {
            @Text
            public String title;

            @Attribute
            public long gid;

            @Attribute
            public String lang;

            @Attribute(required = false)
            public String part;
        }

    }

    public static class Cast {
        @Attribute
        public long gid;

        @Attribute
        public String lang;

        @Element
        public String role;

        @Text
        @Path("person")
        public String person;

        @Attribute(name="id")
        @Path("person")
        public int personId;
    }

}
