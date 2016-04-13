package com.animenavigator.model;

import android.database.Cursor;

import static com.animenavigator.db.Contract.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by a.g.seliverstov on 21.03.2016.
 */
public class Anime {

    public Integer _id;
    public String type;
    public String title;
    public String posterUrl;
    public String vintage;
    public float rating;
    public String plot;

    public static List<String> genresFromCursor(Cursor c){
        List<String> result = new ArrayList<>();
        while (c.moveToNext()){
            result.add(c.getString(c.getColumnIndex(GenreEntry.NAME_COLUMN)));
        }
        return result;
    }

    public static String genresFromCursorAsString(Cursor c){
        String result = "";
        while (c.moveToNext()){
            String s = c.getString(c.getColumnIndex(GenreEntry.NAME_COLUMN));
            result+= (c.isFirst())?s:", "+s;
        }
        return result;
    }

    public static List<String> themesFromCursor(Cursor c){
        List<String> result = new ArrayList<>();
        while (c.moveToNext()){
            result.add(c.getString(c.getColumnIndex(ThemeEntry.NAME_COLUMN)));
        }
        return result;
    }

    public static String themesFromCursorAsString(Cursor c){
        String result = "";
        while (c.moveToNext()){
            String s = c.getString(c.getColumnIndex(ThemeEntry.NAME_COLUMN));
            result+= (c.isFirst())?s:", "+s;
        }
        return result;
    }

    public static List<String> creatorsFromCursor(Cursor c){
        List<String> result = new ArrayList<>();
        while (c.moveToNext()){
            result.add(c.getString(c.getColumnIndex(PersonEntry.NAME_COLUMN)));
        }
        return result;
    }

    public static String creatorsFromCursorAsString(Cursor c){
        String result = "";
        while (c.moveToNext()){
            String s = c.getString(c.getColumnIndex(PersonEntry.NAME_COLUMN));
            result+= (c.isFirst())?s:", "+s;
        }
        return result;
    }

    public static String titlesFromCursorAsString(Cursor c){
        String result = "";
        while (c.moveToNext()){
            String s = c.getString(c.getColumnIndex(MangaTitleEntry.NAME_COLUMN));
            result+= (c.isFirst())?s:", "+s;
        }
        return result;
    }

    public static String creatorsAndTasksFromCursorAsHtml(Cursor c){
        Map<String,String> tasks = new HashMap<>();
        while (c.moveToNext()){
            String persons = tasks.get(c.getString(c.getColumnIndex(TaskEntry.NAME_COLUMN)));
            persons = (persons==null)?c.getString(c.getColumnIndex(PersonEntry.NAME_COLUMN)):persons+", "+c.getString(c.getColumnIndex(PersonEntry.NAME_COLUMN));
            tasks.put(c.getString(c.getColumnIndex(TaskEntry.NAME_COLUMN)),persons);
        }
        StringBuilder sb = new StringBuilder();
        for(String t: tasks.keySet()){
            sb.append("<b>"+t+"</b>: "+tasks.get(t)+"<br/>");
        }
        return sb.toString();
    }

    public static Anime fromCursor(Cursor c){
        Anime a = new Anime();
        a._id = c.getInt(c.getColumnIndex(MangaEntry._ID));
        a.type = c.getString(c.getColumnIndex(MangaEntry.TYPE_COLUMN));
        a.title = c.getString(c.getColumnIndex(MangaEntry.NAME_COLUMN));
        a.plot = c.getString(c.getColumnIndex(MangaEntry.PLOT_COLUMN));
        a.rating = c.getFloat(c.getColumnIndex(MangaEntry.BAYESIAN_SCORE_COLUMN));
        a.posterUrl = c.getString(c.getColumnIndex(MangaEntry.PICTURE_COLUMN));
        a.vintage = c.getString(c.getColumnIndex(MangaEntry.VINTAGE_COLUMN));
        return a;
    }

    public static String reviewsFromCursorAsHtml(Cursor c) {
        StringBuilder sb = new StringBuilder();
        while (c.moveToNext()){
            String href = c.getString(c.getColumnIndex(MangaReviewEntry.HREF_COLUMN));
            String name = c.getString(c.getColumnIndex(MangaReviewEntry.NAME_COLUMN));
            if (href!=null && name!=null)
                sb.append("<a href='"+href+"'>"+name+"</a><br/><br/>");
        }
        String result = sb.toString();
        if (result.endsWith("<br/><br/>"))
            result = result.substring(0,result.length()-5);
        return result;
    }

    public static String linksFromCursorAsHtml(Cursor c) {
        StringBuilder sb = new StringBuilder();
        while (c.moveToNext()){
            String href = c.getString(c.getColumnIndex(MangaLinkEntry.HREF_COLUMN));
            String name = c.getString(c.getColumnIndex(MangaLinkEntry.NAME_COLUMN));
            if (href!=null && name!=null)
                sb.append("<a href='"+href+"'>"+name+"</a><br/><br/>");
        }
        String result = sb.toString();
        if (result.endsWith("<br/><br/>"))
            result = result.substring(0,result.length()-5);
        return result;
    }

    public static String episodesFromCursorAsHtml(Cursor c) {
        StringBuilder sb = new StringBuilder();
        while (c.moveToNext()){
            String num = c.getString(c.getColumnIndex(MangaEpisodeEntry.NUM_COLUMN));
            String part = c.getString(c.getColumnIndex(MangaEpisodeEntry.PART_COLUMN));
            String name = c.getString(c.getColumnIndex(MangaEpisodeEntry.NAME_COLUMN));
            num = (num==null) ? "": num;
            sb.append(num+(part==null?"":"."+part)+" "+name+"<br/>");
        }
        return sb.toString();
    }
}
