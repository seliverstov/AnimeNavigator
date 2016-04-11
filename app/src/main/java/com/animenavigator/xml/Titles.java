package com.animenavigator.xml;

import org.simpleframework.xml.*;

import java.util.List;

/**
 * Created by a.g.seliverstov on 09.03.2016.
 */

@Root(name = "report")
public class Titles {
    @Attribute(required = false)
    public String skipped;

    @Attribute(required = false)
    public String listed;

    @Element(required = false)
    @Path("args")
    public String type;

    @Element(required = false)
    @Path("args")
    public String name;

    @Element(required = false)
    @Path("args")
    public String search;

    @ElementList(inline = true)
    public List<Item> items;

    @Override
    public String toString() {
        return items.size()+" items, skipped " + skipped + ", listed " + listed + ", type = " + type + ", name = " + name + ", search = " + search;
    }

    @Root(name = "item")
    public static class Item{
        @Element
        public String id;

        @Element
        public String gid;

        @Element
        public String type;

        @Element
        public String name;

        @Element
        public String precision;

        @Element(required = false)
        public String vintage;

        @Override
        public String toString() {
            return id+", "+gid+", "+type+", "+name+", "+precision+", "+vintage;
        }
    }
}
