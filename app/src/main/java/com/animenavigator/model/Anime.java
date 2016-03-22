package com.animenavigator.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by a.g.seliverstov on 21.03.2016.
 */
public class Anime {
    public static final String[] GENRES = new String[]{"Genre: science fiction", "Genre: tournament", "Genre: comedy", "Genre: romance", "Genre: drama", "Genre: horror",
            "Genre: mystery", "Genre: supernatural", "Genre: action", "Genre: magic", "Genre: erotica", "Genre: fantasy", "Genre: adventure", "Genre: slice of life",
            "Genre: psychological", "Genre: thriller"};

    public static final String[] THEMES = new String[]{"Theme: baseball", "Theme: historical", "Theme: samurai", "Theme: music", "Theme: ninja", "Theme: yaoi",
            "Theme: demons", "Theme: dog girls", "Theme: military", "Theme: idols", "Theme: mecha", "Theme: real robot", "Theme: war",
            "Theme: ecchi", "Theme: fanservice", "Theme: robots", "Theme: yuri", "Theme: espers", "Theme: vampires", "Theme: ballet",
            "Theme: politics", "Theme: sports", "Theme: basketball", "Theme: future", "Theme: witches", "Theme: cross dressing", "Theme: harem",
            "Theme: cyborg", "Theme: steampunk", "Theme: aliens", "Theme: gods", "Theme: otaku", "Theme: time travel", "Theme: parallel universe",
            "Theme: developing powers", "Theme: fighting", "Theme: pirates", "Theme: superpowers", "Theme: zombies", "Theme: school life",
            "Theme: special abilities", "Theme: tennis", "Theme: twins", "Theme: folklore", "Theme: ghosts", "Theme: monsters", "Theme: magical girl",
            "Theme: post-apocalyptic", "Theme: racing", "Theme: survival game", "Theme: virtual reality", "Theme: café", "Theme: gothic lolita",
            "Theme: High School", "Theme: bishoujo", "Theme: gore", "Theme: coming of age", "Theme: androids", "Theme: moe", "Theme: crossdressing",
            "Theme: maid", "Theme: Androids", "Theme: Future", "Theme: Technology", "Theme: Gods", "Theme: vampire", "Theme: shounen-ai", "Theme: religion",
            "Theme: Mecha", "Theme: cyborgs", "Theme: mythology", "Theme: sentai", "Theme: technology", "Theme: psychics", "Theme: unrequited love",
            "Theme: spirits", "Theme: online computer gaming", "Theme: superhero", "Theme: crime", "Theme: tragedy", "Theme: Moe", "Theme: School",
            "Theme: School Life", "Theme: Yuri", "Theme: delinquents", "Theme: yakuza", "Theme: love triangle", "Theme: robot girl", "Theme: parody",
            "Theme: police", "Theme: martial arts", "Theme: swordplay", "Theme: artificial intelligence", "Theme: space", "Theme: Parody",
            "Theme: gender switch", "Theme: incest", "Theme: college", "Theme: family", "Theme: Yaoi", "Theme: bishounen", "Theme: Bishounen",
            "Theme: detective", "Theme: magical creatures", "Theme: school", "Theme: card games", "Theme: conspiracy", "Theme: Shounen-ai",
            "Theme: Otaku", "Theme: death", "Theme: revenge", "Theme: girls with guns", "Theme: mutants", "Theme: Girls with guns",
            "Theme: talking animals", "Theme: detectives", "Theme: duels", "Theme: World War II", "Theme: shoujo-ai", "Theme: cafe", "Theme: Historical",
            "Theme: Samurai", "Theme: magical boy", "Theme: mafia", "Theme: high school", "Theme: Idols", "Theme: Music", "Theme: assassins", "Theme: immortality",
            "Theme: terrorists", "Theme: hikikomori", "Theme: catgirls", "Theme: shinigami", "Theme: goddesses", "Theme: angels", "Theme: parenting",
            "Theme: male harem", "Theme: maids", "Theme: dark comedy", "Theme: secret agents", "Theme: dragons", "Theme: traveling", "Theme: Male Harem",
            "Theme: Military", "Theme: Real Robot", "Theme: space navy", "Theme: Coming of age", "Theme: gangs", "Theme: social-networking", "Theme: Developing Powers",
            "Theme: Ecchi", "Theme: Harem", "Theme: War", "Theme: Time Travel", "Theme: tsundere", "Theme: Demons", "Theme: bounty hunters", "Theme: Martial Arts",
            "Theme: auto racing", "Theme: Survival game", "Theme: terrorism", "Theme: High school", "Theme: love polygon", "Theme: Dreams", "Theme: living dolls",
            "Theme: dolls", "Theme: alchemy", "Theme: School life", "Theme: growing up", "Theme: amnesia", "Theme: Tragedy", "Theme: NEET", "Theme: human weapons",
            "Theme: treasure hunting", "Theme: Superpowers", "Theme: Angels", "Theme: dreams", "Theme: wizards", "Theme: Sports", "Theme: alien", "Theme: Police",
            "Theme: Yakuza", "Theme: Monsters", "Theme: Fanservice", "Theme: Cross dressing", "Theme: multiple personality", "Theme: monster", "Theme: Aliens",
            "Theme: fairy tales", "Theme: mermaids", "Theme: super robot", "Theme: Death", "Theme: cyberpunk", "Theme: Love Triangle", "Theme: gothic",
            "Theme: hentai", "Theme: golf", "Theme: alternate history", "Theme: Bishojo", "Theme: magical girlfriend", "Theme: lolicon", "Theme: Vampire",
            "Theme: student teacher relationship", "Theme: priests and priestesses", "Theme: girls with weapons", "Theme: succubus", "Theme: Alien",
            "Theme: Mind Control", "Theme: economics", "Theme: Fighting", "Theme: Goddesses", "Theme: Priests and priestesses", "Theme: Robot girl", "Theme: Western",
            "Theme: Incest", "Theme: Crime", "Theme: robotics", "Theme: microbiology", "Theme: Love triangle", "Theme: Ghosts", "Theme: Magical Girl",
            "Theme: augmented reality", "Theme: literature", "Theme: Fairy tales", "Theme: Alternate history", "Theme: Virtual Reality", "Theme: swordsman",
            "Theme: Ninja", "Theme: Witches", "Theme: tanuki", "Theme: perverted female character", "Theme: Unrequited Love", "Theme: Living Dolls",
            "Theme: Revenge", "Theme: Male harem", "Theme: occult", "Theme: kendo", "Theme: Martial arts", "Theme: adoption", "Theme: single parents",
            "Theme: middle ages", "Theme: super hero", "Theme: Tennis", "Theme: succubi", "Theme: Baseball", "Theme: cat girls", "Theme: Gangs", "Theme: Dark Comedy",
            "Theme: Post-Apocalyptic", "Theme: Zombies", "Theme: Parenting", "Theme: neet", "Theme: butterfly effect", "Theme: Time travel", "Theme: Crossdressing",
            "Theme: bishojo", "Theme: fan service", "Theme: wizards/witches", "Theme: Delivery service", "Theme: ren'ai", "Theme: mind control", "Theme: Growing up",
            "Theme: superhuman", "Theme: Gore", "Theme: Politics", "Theme: Space", "Theme: disaster", "Theme: underwear", "Theme: alternate reality",
            "Theme: Dark comedy", "Theme: Literature", "Theme: life after death", "Theme: foxgirls", "Theme: superhumans", "Theme: society", "Theme: female warriors",
            "Theme: Super Robot", "Theme: earthquake", "Theme: Real robot", "Theme: Robots", "Theme: love rectangle", "Theme: computers", "Theme: Homunculi",
            "Theme: space western", "Theme: crime organizations", "Theme: College", "Theme: Cyborg", "Theme: Post-apocalyptic", "Theme: speedboat racing",
            "Theme: mermaid", "Theme: gunslinger", "Theme: western", "Theme: Hentai", "Theme: wolf girls", "Theme: Coming of Age", "Theme: Love Polygon",
            "Theme: Survival Game", "Theme: trading", "Theme: environmentalism", "Theme: aircraft", "Theme: Shoujo-ai", "Theme: Kendo", "Theme: Racing",
            "Theme: Mythology", "Theme: spy thriller", "Theme: Burning Manly Passion", "Theme: Magic Book", "Theme: MUSIC", "Theme: Fine Arts", "Theme: seiyuu",
            "Theme: host club", "Theme: acrobatics", "Theme: gymnastics", "Theme: quantum physics", "Theme: Vampires", "Theme: immortal", "Theme: Maids",
            "Theme: Giant Robots", "Theme: Space Navy", "Theme: england", "Theme: historical (england)", "Theme: secret organizations", "Theme: Bounty Hunters",
            "Theme: bishōjo", "Theme: animation production", "Theme: animators", "Theme: Magical Boy", "Theme: Steampunk", "Theme: Growing Up", "Theme: Magical girl",
            "Theme: Family", "Theme: Shoujo-Ai", "Theme: deforestation", "Theme: forest spirits", "Theme: Another world", "Theme: noir", "Theme: Duels",
            "Theme: Spy Thriller"};

    public static final String[] CREATORS = new String[] {"Tsubasa Masao", "Sh?ichir? Sat?", "Taku Matsuo", "Y?ichir? Nogami", "Tomomi Mochizuki",
            "Akira Senju", "Atsuko Asano", "Takako Shimura", "Hideoki Kusama", "Hiroyuki Kitazume", "Hajime Yatate"};


    public Integer _id;
    public String title;
    public String posterUrl;
    public String rating;
    public List<String> genres;
    public List<String> themes;
    public List<String> alternativeTitles;
    public List<String> creators;
    public String plot;

    public Anime(){}

    public Anime(Integer _id, String title, String posterUrl){
        this._id = _id;
        this.title = title;
        this.posterUrl = posterUrl;

        alternativeTitles = new ArrayList<>();
        alternativeTitles.add(title);
        alternativeTitles.add(title);
        alternativeTitles.add(title);

        rating = String.format("%2.1f",Math.random()*10);

        genres = new ArrayList<>();
        int n = (int)(Math.random()*5)+1;
        for(int i=0; i<n; i++){
            genres.add(GENRES[(int)(Math.random()*(GENRES.length-1))]);
        }

        themes = new ArrayList<>();
        n = (int)(Math.random()*5)+1;
        for(int i=0; i<n; i++){
            themes.add(THEMES[(int)(Math.random()*(THEMES.length-1))]);
        }

        creators = new ArrayList<>();
        n = (int)(Math.random()*5)+1;
        for(int i=0; i<n; i++){
            creators.add(CREATORS[(int)(Math.random()*(CREATORS.length-1))]);
        }

        plot = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
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



    public static List<String> listGenres(){
        return Arrays.asList(GENRES);
    }

    public static List<String> listThemes() {
        return Arrays.asList(THEMES);
    }

    public static List<String> listCreators(){
        return Arrays.asList(CREATORS);
    }

    public static String printList(List list){
        String result = list.toString();
        if (result.startsWith("[")) result=result.substring(1);
        if (result.endsWith("]")) result=result.substring(0,result.length()-1);
        return result.replaceAll("Genre: ","").replaceAll("Theme: ","");
    }
}
