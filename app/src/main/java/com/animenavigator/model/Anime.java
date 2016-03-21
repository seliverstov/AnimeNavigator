package com.animenavigator.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by a.g.seliverstov on 21.03.2016.
 */
public class Anime {
    public Integer _id;
    public String title;
    public String posterUrl;

    public Anime(){}

    public Anime(Integer _id, String title, String posterUrl){
        this._id = _id;
        this.title = title;
        this.posterUrl = posterUrl;
    }

    public static List<Anime> createAnimeList(){
        List<Anime> animeList = new ArrayList<>();
        animeList.add(new Anime(2298,"Berserk","http://cdn.animenewsnetwork.com/thumbnails/fit200x200/encyc/A2298-11.jpg"));
        animeList.add(new Anime(1511,"Monster","http://cdn.animenewsnetwork.com/thumbnails/fit200x200/encyc/A1511-11.jpg"));
        animeList.add(new Anime(1264,"Nausicaä of the Valley of the Wind","http://cdn.animenewsnetwork.com/thumbnails/fit200x200/encyc/A1264-61.jpg"));
        animeList.add(new Anime(6121,"Vinland Saga","http://cdn.animenewsnetwork.com/thumbnails/fit200x200/encyc/A6121-11.jpg"));
        animeList.add(new Anime(11770,"Steins;Gate","http://cdn.animenewsnetwork.com/thumbnails/fit200x200/encyc/A11770-1864351140.1370764886.jpg"));
        animeList.add(new Anime(10216,"Fullmetal Alchemist: Brotherhood","http://cdn.animenewsnetwork.com/thumbnails/fit200x200/encyc/A10216-220.jpg"));
        animeList.add(new Anime(9701,"Clannad After Story","http://cdn.animenewsnetwork.com/thumbnails/fit200x200/encyc/A9701-6.jpg"));
        animeList.add(new Anime(3605,"Yokohama Kaidashi Kikou","http://cdn.animenewsnetwork.com/thumbnails/fit200x200/encyc/A3605-25.1409849568.jpg"));
        animeList.add(new Anime(3424,"20th Century Boys","http://cdn.animenewsnetwork.com/thumbnails/fit200x200/encyc/A3424-13.jpg"));
        animeList.add(new Anime(210,"Rurouni Kenshin: Trust & Betrayal","http://cdn.animenewsnetwork.com/thumbnails/fit200x200/encyc/A210-762993323.1447170085.jpg"));
        animeList.add(new Anime(2565,"Fullmetal Alchemist","http://cdn.animenewsnetwork.com/thumbnails/fit200x200/encyc/A2565-9.jpg"));
        animeList.add(new Anime(11,"Akira","http://cdn.animenewsnetwork.com/thumbnails/fit200x200/encyc/A11-1915187482.1444294671.jpg"));
        animeList.add(new Anime(9173,"Code Geass: Lelouch of the Rebellion R2","http://cdn.animenewsnetwork.com/thumbnails/fit200x200/encyc/A9173-48.jpg"));
        animeList.add(new Anime(15895,"Mushishi: The Next Chapter","http://cdn.animenewsnetwork.com/thumbnails/fit200x200/encyc/A15895-2834457359.1394934609.jpg"));
        animeList.add(new Anime(2966,"Yotsuba&!","http://cdn.animenewsnetwork.com/thumbnails/fit200x200/encyc/A2966-24.jpg"));
        animeList.add(new Anime(377,"Spirited Away","http://cdn.animenewsnetwork.com/thumbnails/fit200x200/encyc/A377-13.jpg"));
        animeList.add(new Anime(1595,"Vagabond","http://cdn.animenewsnetwork.com/thumbnails/fit200x200/encyc/A1595-3.jpg"));
        animeList.add(new Anime(13,"Cowboy Bebop","http://cdn.animenewsnetwork.com/thumbnails/fit200x200/encyc/A13-15.jpg"));
        animeList.add(new Anime(5564,"Steel Ball Run","http://cdn.animenewsnetwork.com/thumbnails/fit200x200/encyc/A5564-18.jpg"));
        animeList.add(new Anime(11120,"Disappearance of Haruhi Suzumiya","http://cdn.animenewsnetwork.com/thumbnails/fit200x200/encyc/A11120-13.1393515270.jpg"));
        return animeList;
    }
}
