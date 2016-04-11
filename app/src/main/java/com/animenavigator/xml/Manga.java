package com.animenavigator.xml;

import org.simpleframework.xml.*;

import java.util.Collection;


/**
 * Created by a.g.seliverstov on 10.03.2016.
 */
@Root
public class Manga {
    @Attribute(name = "id")
    public int id;

    @Attribute
    public long gid;

    @Attribute
    public String type;

    @Attribute
    public String name;

    @Attribute
    public String precision;

    public String vintage;

    @Attribute(name = "generated-on")
    public String generatedOn;

    @Attribute(name = "nb_votes", required = false)
    @Path("ratings")
    public int votes;

    @Attribute(name = "weighted_score", required = false)
    @Path("ratings")
    public float weightedScore;

    @Attribute(name = "bayesian_score", required = false)
    @Path("ratings")
    public float bayesianScore;

    @ElementListUnion({
            @ElementList(entry = "related-prev", inline = true, required = false, type = Related.class),
            @ElementList(entry = "related-next", inline = true, required = false, type = Related.class)
    })
    public Collection<Related> related;

    @ElementList(entry = "info", inline = true, required = false, type = Info.class)
    public Collection<Info> info;

    @ElementList(entry = "review", inline = true, required = false, type = Review.class)
    public Collection<Review> reviews;

    @ElementList(entry = "release", inline = true, required = false, type = Release.class)
    public Collection<Release> releases;

    @ElementList(entry = "news", inline = true, required = false, type = News.class)
    public Collection<News> news;

    @ElementList(entry = "staff", inline = true, required = false, type = Staff.class)
    public Collection<Staff> staff;

    @ElementList(entry = "credit", inline = true, required = false, type = Credit.class)
    public Collection<Credit> credits;


    @Override
    public String toString() {
        return id+", "+type+", "+name;
    }

    @Root()
    public static class Related {
        @Attribute(name = "id")
        public int annId;

        @Attribute
        public String rel;

    }

    @Root(strict = false)
    public static class Info {
        @Attribute(required = false)
        public long gid;

        @Attribute
        public String type;

        @Text(required = false)
        public String value;

        @Attribute(required = false)
        public String lang;

        @Attribute(required = false)
        public String href;

        @Attribute(required = false)
        public String src;

        @Attribute(required = false)
        public int width;

        @Attribute(required = false)
        public int height;

    }

    @Root
    public static class Staff {
        @Attribute
        public long gid;

        @Element
        public String task;

        @Text
        @Path("person")
        public String person;

        @Attribute(name="id")
        @Path("person")
        public int personId;
    }

    @Root
    public static class News {
        @Attribute(name = "datetime")
        public String date;

        @Attribute(name = "href")
        public String href;

        @Text
        public String text;
    }

    @Root
    public static class Release {
        @Attribute(name = "date")
        public String date;

        @Attribute(name = "href")
        public String href;

        @Text
        public String text;
    }

    @Root
    public static class Review {
        @Attribute(name = "href")
        public String href;

        @Text
        public String text;
    }

    @Root
    public static class Credit {
        @Attribute
        public long gid;

        @Element
        public String task;

        @Text
        @Path("company")
        public String company;

        @Attribute(name="id")
        @Path("company")
        public int companyId;
    }
}
