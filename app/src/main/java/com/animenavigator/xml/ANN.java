package com.animenavigator.xml;

import org.simpleframework.xml.*;

/**
 * Created by a.g.seliverstov on 10.03.2016.
 */
@Root
public class ANN {
    @Element(required = false)
    public Manga manga;

    @Element(required = false)
    public Anime anime;

    @Override
    public String toString() {
        if (manga!=null){
            return manga.toString();
        }
        if (anime!=null){
            return anime.toString();
        }
        return super.toString();
    }
}
