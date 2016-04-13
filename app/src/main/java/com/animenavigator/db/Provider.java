package com.animenavigator.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.animenavigator.R;
import com.animenavigator.common.Const;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.animenavigator.db.Contract.*;

/**
 * Created by a.g.seliverstov on 04.04.2016.
 */
public class Provider extends ContentProvider{
    private static final UriMatcher sUriMatcher;

    static final int MANGA = 100;
    static final int MANGA_WITH_ID = 101;
    static final int MANGA_RELATED_FOR_MANGA = 102;
    static final int MANGA_SEARCH = 103;
    static final int MANGA_FAVORITE = 104;

    static final int GENRE = 200;
    static final int GENRE_WITH_ID = 201;
    static final int GENRE_FOR_MANGA = 202;

    static final int THEME = 300;
    static final int THEME_WITH_ID = 301;
    static final int THEME_FOR_MANGA = 302;

    static final int PERSON = 400;
    static final int PERSON_WITH_ID = 401;
    static final int PERSON_FOR_MANGA = 402;
    static final int PERSON_AND_TASK_FOR_MANGA = 403;

    static final int TITLE = 500;
    static final int TITLE_WITH_ID = 501;
    static final int TITLE_FOR_MANGA = 502;

    static final int REVIEW = 600;
    static final int REVIEW_WITH_ID = 601;
    static final int REVIEW_FOR_MANGA = 602;

    static final int LINK = 700;
    static final int LINK_WITH_ID = 701;
    static final int LINK_FOR_MANGA = 702;

    static final int EPISODE = 800;
    static final int EPISODE_WITH_ID = 801;
    static final int EPISODE_FOR_MANGA = 802;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = CONTENT_AUTHORITY;
        sUriMatcher.addURI(authority, PATH_MANGA, MANGA);
        sUriMatcher.addURI(authority, PATH_MANGA+"/#", MANGA_WITH_ID);
        sUriMatcher.addURI(authority, PATH_MANGA+"/"+PATH_RELATED+"/#", MANGA_RELATED_FOR_MANGA);
        sUriMatcher.addURI(authority, PATH_MANGA+"/"+PATH_SEARCH, MANGA_SEARCH);
        sUriMatcher.addURI(authority, PATH_MANGA+"/"+PATH_FAVORITE, MANGA_FAVORITE);

        sUriMatcher.addURI(authority, PATH_GENRE, GENRE);
        sUriMatcher.addURI(authority, PATH_GENRE+"/#", GENRE_WITH_ID);
        sUriMatcher.addURI(authority, PATH_GENRE+"/"+PATH_MANGA+"/#", GENRE_FOR_MANGA);

        sUriMatcher.addURI(authority, PATH_THEME, THEME);
        sUriMatcher.addURI(authority, PATH_THEME+"/#", THEME_WITH_ID);
        sUriMatcher.addURI(authority, PATH_THEME+'/'+PATH_MANGA+"/#", THEME_FOR_MANGA);

        sUriMatcher.addURI(authority, PATH_PERSON, PERSON);
        sUriMatcher.addURI(authority, PATH_PERSON+"/#", PERSON_WITH_ID);
        sUriMatcher.addURI(authority, PATH_PERSON+'/'+PATH_MANGA+"/#", PERSON_FOR_MANGA);
        sUriMatcher.addURI(authority, PATH_PERSON+'/'+PATH_TASK+'/'+PATH_MANGA+"/#", PERSON_AND_TASK_FOR_MANGA);

        sUriMatcher.addURI(authority, PATH_TITLE, TITLE);
        sUriMatcher.addURI(authority, PATH_TITLE+"/#", TITLE_WITH_ID);
        sUriMatcher.addURI(authority, PATH_TITLE+'/'+PATH_MANGA+"/#", TITLE_FOR_MANGA);

        sUriMatcher.addURI(authority, PATH_REVIEW, REVIEW);
        sUriMatcher.addURI(authority, PATH_REVIEW+"/#", REVIEW_WITH_ID);
        sUriMatcher.addURI(authority, PATH_REVIEW+'/'+PATH_MANGA+"/#", REVIEW_FOR_MANGA);

        sUriMatcher.addURI(authority, PATH_LINK, LINK);
        sUriMatcher.addURI(authority, PATH_LINK+"/#", LINK_WITH_ID);
        sUriMatcher.addURI(authority, PATH_LINK+'/'+PATH_MANGA+"/#", LINK_FOR_MANGA);

        sUriMatcher.addURI(authority, PATH_EPISODE, EPISODE);
        sUriMatcher.addURI(authority, PATH_EPISODE+"/#", EPISODE_WITH_ID);
        sUriMatcher.addURI(authority, PATH_EPISODE+'/'+PATH_MANGA+"/#", EPISODE_FOR_MANGA);
    }

    private static final SQLiteQueryBuilder sGenresForMangaQueryBuilder;
    private static final SQLiteQueryBuilder sThemesForMangaQueryBuilder;
    private static final SQLiteQueryBuilder sPersonsForMangaQueryBuilder;
    private static final SQLiteQueryBuilder sPersonsAndTasksForMangaQueryBuilder;
    private static final SQLiteQueryBuilder sMangaRelatedForMangaQueryBuilder;

    static{
        sGenresForMangaQueryBuilder = new SQLiteQueryBuilder();
        sThemesForMangaQueryBuilder = new SQLiteQueryBuilder();
        sPersonsForMangaQueryBuilder = new SQLiteQueryBuilder();
        sPersonsAndTasksForMangaQueryBuilder = new SQLiteQueryBuilder();
        sMangaRelatedForMangaQueryBuilder = new SQLiteQueryBuilder();

        sGenresForMangaQueryBuilder.setTables(
                GenreEntry.TABLE_NAME +
                " INNER JOIN " +
                MangaGenreEntry.TABLE_NAME +
                " ON " +
                GenreEntry.TABLE_NAME + "." + GenreEntry._ID +
                " = " +
                MangaGenreEntry.TABLE_NAME + "." + MangaGenreEntry.GENRE_ID_COLUMN);

        sThemesForMangaQueryBuilder.setTables(
                ThemeEntry.TABLE_NAME +
                " INNER JOIN " +
                MangaThemeEntry.TABLE_NAME +
                " ON " +
                ThemeEntry.TABLE_NAME + "." + ThemeEntry._ID +
                " = " +
                MangaThemeEntry.TABLE_NAME + "." + MangaThemeEntry.THEME_ID_COLUMN);

        sPersonsForMangaQueryBuilder.setTables(
                PersonEntry.TABLE_NAME +
                " INNER JOIN " +
                MangaStaffEntry.TABLE_NAME +
                " ON " +
                PersonEntry.TABLE_NAME + "." + PersonEntry._ID +
                " = " +
                MangaStaffEntry.TABLE_NAME + "." + MangaStaffEntry.PERSON_ID_COLUMN);

        sPersonsForMangaQueryBuilder.setDistinct(true);

        sPersonsAndTasksForMangaQueryBuilder.setTables(
                PersonEntry.TABLE_NAME +
                " INNER JOIN " +
                MangaStaffEntry.TABLE_NAME +
                " ON " +
                PersonEntry.TABLE_NAME + "." + PersonEntry._ID +
                " = " +
                MangaStaffEntry.TABLE_NAME + "." + MangaStaffEntry.PERSON_ID_COLUMN +
                " INNER JOIN " +
                TaskEntry.TABLE_NAME +
                " ON " +
                MangaStaffEntry.TABLE_NAME + "." + MangaStaffEntry.TASK_ID_COLUMN +
                " = " +
                TaskEntry.TABLE_NAME + "." + TaskEntry._ID);

        sPersonsAndTasksForMangaQueryBuilder.setDistinct(true);

        sMangaRelatedForMangaQueryBuilder.setTables(
                MangaEntry.TABLE_NAME +
                " INNER JOIN " +
                MangaRelatedEntry.TABLE_NAME +
                " ON " +
                MangaEntry.TABLE_NAME + "." + MangaEntry._ID +
                " = " +
                MangaRelatedEntry.TABLE_NAME + "." + MangaRelatedEntry.REL_MANGA_ID_COLUMN +
                " INNER JOIN " +
                RelatedEntry.TABLE_NAME +
                " ON " +
                MangaRelatedEntry.TABLE_NAME + "." + MangaRelatedEntry.REL_ID_COLUMN +
                " = " +
                RelatedEntry.TABLE_NAME + "." + RelatedEntry._ID
        );
    }

    private DbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new DbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db =mDbHelper.getReadableDatabase();
        Context context = getContext();
        Cursor retCursor;
        switch (sUriMatcher.match(uri)){
            case MANGA: {
                retCursor = db.query(MangaEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            case MANGA_WITH_ID:{
                String id = String.valueOf(ContentUris.parseId(uri));
                retCursor = db.query(MangaEntry.TABLE_NAME,projection,MangaEntry._ID+" = ?",new String[]{id},null,null,sortOrder);
                break;
            }
            case MANGA_RELATED_FOR_MANGA:{
                String id = String.valueOf(ContentUris.parseId(uri));
                retCursor = sMangaRelatedForMangaQueryBuilder.query(db,
                        new String[]{
                                MangaEntry.TABLE_NAME+"."+MangaEntry._ID,
                                MangaEntry.TABLE_NAME+"."+MangaEntry.NAME_COLUMN,
                                MangaEntry.TABLE_NAME+"."+MangaEntry.TYPE_COLUMN,
                                MangaEntry.TABLE_NAME+"."+MangaEntry.VINTAGE_COLUMN,
                                MangaEntry.TABLE_NAME+"."+MangaEntry.PLOT_COLUMN,
                                MangaEntry.TABLE_NAME+"."+MangaEntry.PICTURE_COLUMN,
                                MangaEntry.TABLE_NAME+"."+MangaEntry.VOTES_COLUMN,
                                MangaEntry.TABLE_NAME+"."+MangaEntry.WEIGHTED_SCORE_COLUMN,
                                MangaEntry.TABLE_NAME+"."+MangaEntry.BAYESIAN_SCORE_COLUMN,
                                RelatedEntry.TABLE_NAME+"."+RelatedEntry.NAME_COLUMN
                        },
                        MangaRelatedEntry.MANGA_ID_COLUMN+" = ?",new String[]{id},null,null,sortOrder);
                break;
            }
            case MANGA_SEARCH: {
                retCursor = querySearch(db,selection, selectionArgs,sortOrder);
                break;
            }
            case MANGA_FAVORITE: {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
                String[] args = sp.getString(Const.SP_FAVORITE_LIST_KEY,"-1").split(",");
                String list = "";
                for(String s: args){
                    list +="?,";
                }
                if (list.length()>0) list = list.substring(0,list.length()-1);
                retCursor = db.query(MangaEntry.TABLE_NAME,projection, Contract.MangaEntry._ID+" IN ("+list+")", args, null, null, sortOrder);
                break;
            }
            case GENRE: {
                retCursor = db.query(GenreEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            case GENRE_WITH_ID:{
                String id = String.valueOf(ContentUris.parseId(uri));
                retCursor = db.query(GenreEntry.TABLE_NAME,projection,GenreEntry._ID+" = ?",new String[]{id},null,null,sortOrder);
                break;
            }
            case GENRE_FOR_MANGA:{
                String id = String.valueOf(ContentUris.parseId(uri));
                retCursor = sGenresForMangaQueryBuilder.query(db,projection,MangaGenreEntry.MANGA_ID_COLUMN+" = ?",new String[]{id},null,null,sortOrder);
                break;
            }
            case THEME: {
                retCursor = db.query(ThemeEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            case THEME_WITH_ID:{
                String id = String.valueOf(ContentUris.parseId(uri));
                retCursor = db.query(ThemeEntry.TABLE_NAME,projection,ThemeEntry._ID+" = ?",new String[]{id},null,null,sortOrder);
                break;
            }
            case THEME_FOR_MANGA:{
                String id = String.valueOf(ContentUris.parseId(uri));
                retCursor = sThemesForMangaQueryBuilder.query(db,projection,MangaThemeEntry.MANGA_ID_COLUMN+" = ?",new String[]{id},null,null,sortOrder);
                break;
            }
            case PERSON: {
                retCursor = db.query(PersonEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            case PERSON_WITH_ID:{
                String id = String.valueOf(ContentUris.parseId(uri));
                retCursor = db.query(PersonEntry.TABLE_NAME,projection,ThemeEntry._ID+" = ?",new String[]{id},null,null,sortOrder);
                break;
            }
            case PERSON_FOR_MANGA:{
                String id = String.valueOf(ContentUris.parseId(uri));
                retCursor = sPersonsForMangaQueryBuilder.query(db,
                        new String[]{
                                PersonEntry.TABLE_NAME+"."+PersonEntry._ID,
                                PersonEntry.TABLE_NAME+"."+PersonEntry.NAME_COLUMN
                        },MangaStaffEntry.MANGA_ID_COLUMN+" = ?",new String[]{id},null,null,sortOrder);
                break;
            }
            case PERSON_AND_TASK_FOR_MANGA:{
                String id = String.valueOf(ContentUris.parseId(uri));
                retCursor = sPersonsAndTasksForMangaQueryBuilder.query(db,
                        new String[]{
                                PersonEntry.TABLE_NAME+"."+PersonEntry._ID,
                                PersonEntry.TABLE_NAME+"."+PersonEntry.NAME_COLUMN,
                                TaskEntry.TABLE_NAME+"."+TaskEntry.NAME_COLUMN
                        },MangaStaffEntry.MANGA_ID_COLUMN+" = ?",new String[]{id},null,null,sortOrder);
                break;
            }
            case TITLE: {
                retCursor = db.query(MangaTitleEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            case TITLE_WITH_ID:{
                String id = String.valueOf(ContentUris.parseId(uri));
                retCursor = db.query(MangaTitleEntry.TABLE_NAME,projection,MangaTitleEntry._ID+" = ?",new String[]{id},null,null,sortOrder);
                break;
            }
            case TITLE_FOR_MANGA:{
                String id = String.valueOf(ContentUris.parseId(uri));
                retCursor = db.query(MangaTitleEntry.TABLE_NAME,projection,MangaTitleEntry.MANGA_ID_COLUMN+" = ?",new String[]{id},null,null,sortOrder);
                break;
            }
            case REVIEW: {
                retCursor = db.query(MangaReviewEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            case REVIEW_WITH_ID:{
                String id = String.valueOf(ContentUris.parseId(uri));
                retCursor = db.query(MangaReviewEntry.TABLE_NAME,projection,MangaReviewEntry._ID+" = ?",new String[]{id},null,null,sortOrder);
                break;
            }
            case REVIEW_FOR_MANGA:{
                String id = String.valueOf(ContentUris.parseId(uri));
                retCursor = db.query(MangaReviewEntry.TABLE_NAME,projection,MangaReviewEntry.MANGA_ID_COLUMN+" = ?",new String[]{id},null,null,sortOrder);
                break;
            }
            case LINK: {
                retCursor = db.query(MangaLinkEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            case LINK_WITH_ID:{
                String id = String.valueOf(ContentUris.parseId(uri));
                retCursor = db.query(MangaLinkEntry.TABLE_NAME,projection,MangaLinkEntry._ID+" = ?",new String[]{id},null,null,sortOrder);
                break;
            }
            case LINK_FOR_MANGA:{
                String id = String.valueOf(ContentUris.parseId(uri));
                retCursor = db.query(MangaLinkEntry.TABLE_NAME,projection,MangaLinkEntry.MANGA_ID_COLUMN+" = ?",new String[]{id},null,null,sortOrder);
                break;
            }
            case EPISODE: {
                retCursor = db.query(MangaEpisodeEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            case EPISODE_WITH_ID:{
                String id = String.valueOf(ContentUris.parseId(uri));
                retCursor = db.query(MangaEpisodeEntry.TABLE_NAME,projection,MangaEpisodeEntry._ID+" = ?",new String[]{id},null,null,sortOrder);
                break;
            }
            case EPISODE_FOR_MANGA:{
                String id = String.valueOf(ContentUris.parseId(uri));
                retCursor = db.query(MangaEpisodeEntry.TABLE_NAME,projection,MangaEpisodeEntry.MANGA_ID_COLUMN+" = ?",new String[]{id},null,null,sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unsupported uri: "+uri);
        }

        if (context!=null)
            retCursor.setNotificationUri(context.getContentResolver(),uri);
        return retCursor;
    }

    private Cursor querySearch(SQLiteDatabase db,String selection, String[] selectionArgs, String sortOrder) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        String value = sp.getString(Const.SP_SEARCH_KEY, null);
        String query;
        List<String> args = new ArrayList<>();
        Context context = getContext();
        if (value!=null && !"".equals(value.trim())) {
            String[] parts = value.split(",");
            List<String> genres = new ArrayList<>();
            List<String> themes = new ArrayList<>();
            List<String> other = new ArrayList<>();
            String genrePrefix = (context!=null)?context.getString(R.string.genre_search_prefix):"";
            String themePrefix = (context!=null)?context.getString(R.string.theme_search_prefix):"";
            String genresSet = null;
            String themesSet = null;
            String titlesSet = null;
            for (String s : parts) {
                s = s.trim();
                if (s.startsWith(genrePrefix)) {
                    s = s.replace(genrePrefix, "").trim();
                    if (!s.equals("")) {
                        genres.add(s);
                        genresSet = (genresSet == null) ? "?" : genresSet + ", ?";
                    }
                } else if (s.startsWith(themePrefix)) {
                    s = s.replace(themePrefix, "").trim();
                    if (!s.equals("")) {
                        themes.add(s);
                        themesSet = (themesSet == null) ? "?" : themesSet + ", ?";
                    }
                } else {
                    s = s.trim();
                    if (!s.equals("")) {
                        other.add("%"+s+"%");
                        titlesSet = (titlesSet == null) ? "mt." + Contract.MangaTitleEntry.NAME_COLUMN + " LIKE ?" : titlesSet + " AND mt." + Contract.MangaTitleEntry.NAME_COLUMN + " LIKE ?";
                    }
                }
            }

            if (genres.size() > 0) {
                args.addAll(genres);
                args.add("" + genres.size());
            }
            String genresSubQuery = (genres.size() == 0) ? null : "(select mg.manga_id  from manga_genres mg left join genres g on mg.genre_id = g._id where g.genre in (" +
                    genresSet + ") group by mg.manga_id having count(*) = CAST(? AS INTEGER)) gq";

            if (themes.size() > 0) {
                args.addAll(themes);
                args.add("" + themes.size());
            }
            String themesSubQuery = (themes.size() == 0) ? null : "(select mt.manga_id from manga_themes mt left join themes t on mt.theme_id = t._id where t.theme in (" +
                    themesSet + ") group by mt.manga_id having count(*) = CAST(? AS INTEGER)) tq";

            if (other.size() > 0) {
                args.addAll(other);
            }

            String titlesSubQuery = (other.size() == 0) ? null : "(select distinct mt.manga_id from manga_titles mt where " + titlesSet + ") nq";


            List<String> fromList = new ArrayList<>();
            List<String> whereList = new ArrayList<>();
            String projection = null;
            if (genresSubQuery != null) {
                fromList.add(genresSubQuery);
                whereList.add("gq." + Contract.MangaGenreEntry.MANGA_ID_COLUMN);
                projection = (projection==null)?" gq."+ Contract.MangaGenreEntry.MANGA_ID_COLUMN: projection;
            }
            if (themesSubQuery != null) {
                fromList.add(themesSubQuery);
                whereList.add("tq." + Contract.MangaThemeEntry.MANGA_ID_COLUMN);
                projection = (projection==null)?" tq."+ Contract.MangaThemeEntry.MANGA_ID_COLUMN: projection;
            }
            if (titlesSubQuery != null) {
                fromList.add(titlesSubQuery);
                whereList.add("nq." + Contract.MangaTitleEntry.MANGA_ID_COLUMN);
                projection = (projection==null)?" nq."+ Contract.MangaTitleEntry.MANGA_ID_COLUMN: projection;
            }

            String from = null;

            for (String f : fromList) {
                from = (from == null) ? f : from + " , " + f;
            }

            from = " FROM " + from;

            String where = "";
            if (whereList.size() == 3) {
                where = " WHERE " + whereList.get(0) + " = " + whereList.get(1) + " AND " + whereList.get(1) + " = " + whereList.get(2);
            } else if (whereList.size() == 2) {
                where = " WHERE " + whereList.get(0) + " = " + whereList.get(1);
            }

            query = "SELECT * FROM " + MangaEntry.TABLE_NAME + " WHERE " + MangaEntry._ID + " IN (SELECT "+projection +" " + from +" " + where + ")";

            if (selection != null && !"".equals(selection)) {
                query += " AND " + selection;
                args.addAll(Arrays.asList(selectionArgs));
            }
            if (sortOrder!=null && !"".equals(sortOrder)){
                query+= " ORDER BY "+sortOrder;
            }
            String[] queryArgs = new String[args.size()];
            queryArgs = args.toArray(queryArgs);
            return db.rawQuery(query,queryArgs);
        }else{
            return db.query(MangaEntry.TABLE_NAME,null,selection, selectionArgs,null,null,sortOrder);
        }

    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch(sUriMatcher.match(uri)){
            case MANGA:
                return MangaEntry.CONTENT_DIR_TYPE;
            case MANGA_WITH_ID:
                return MangaEntry.CONTENT_ITEM_TYPE;
            case MANGA_RELATED_FOR_MANGA:
                return MangaEntry.CONTENT_DIR_TYPE;
            case MANGA_SEARCH:
                return MangaEntry.CONTENT_DIR_TYPE;
            case MANGA_FAVORITE:
                return MangaEntry.CONTENT_DIR_TYPE;
            case GENRE:
                return GenreEntry.CONTENT_DIR_TYPE;
            case GENRE_WITH_ID:
                return GenreEntry.CONTENT_ITEM_TYPE;
            case GENRE_FOR_MANGA:
                return GenreEntry.CONTENT_DIR_TYPE;
            case THEME:
                return ThemeEntry.CONTENT_DIR_TYPE;
            case THEME_WITH_ID:
                return ThemeEntry.CONTENT_ITEM_TYPE;
            case THEME_FOR_MANGA:
                return ThemeEntry.CONTENT_DIR_TYPE;
            case PERSON:
                return PersonEntry.CONTENT_DIR_TYPE;
            case PERSON_WITH_ID:
                return PersonEntry.CONTENT_ITEM_TYPE;
            case PERSON_FOR_MANGA:
                return PersonEntry.CONTENT_DIR_TYPE;
            case PERSON_AND_TASK_FOR_MANGA:
                return PersonEntry.CONTENT_DIR_TYPE;
            case TITLE:
                return MangaTitleEntry.CONTENT_DIR_TYPE;
            case TITLE_WITH_ID:
                return MangaTitleEntry.CONTENT_ITEM_TYPE;
            case TITLE_FOR_MANGA:
                return MangaTitleEntry.CONTENT_DIR_TYPE;
            case REVIEW:
                return MangaTitleEntry.CONTENT_DIR_TYPE;
            case REVIEW_WITH_ID:
                return MangaTitleEntry.CONTENT_ITEM_TYPE;
            case REVIEW_FOR_MANGA:
                return MangaTitleEntry.CONTENT_DIR_TYPE;
            case LINK:
                return MangaLinkEntry.CONTENT_DIR_TYPE;
            case LINK_WITH_ID:
                return MangaLinkEntry.CONTENT_ITEM_TYPE;
            case LINK_FOR_MANGA:
                return MangaLinkEntry.CONTENT_DIR_TYPE;
            case EPISODE:
                return MangaEpisodeEntry.CONTENT_DIR_TYPE;
            case EPISODE_WITH_ID:
                return MangaEpisodeEntry.CONTENT_ITEM_TYPE;
            case EPISODE_FOR_MANGA:
                return MangaEpisodeEntry.CONTENT_DIR_TYPE;
            default:
                throw new UnsupportedOperationException("Unsupported uri: "+uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Context context = getContext();
        Uri returnUri;

        switch (sUriMatcher.match(uri)){
            case MANGA: {
                long id = db.insertWithOnConflict(MangaEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_NONE);
                if (id != -1)
                    returnUri = ContentUris.withAppendedId(MangaEntry.CONTENT_URI, id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case GENRE: {
                long id = db.insertWithOnConflict(GenreEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_NONE);
                if (id != -1)
                    returnUri = ContentUris.withAppendedId(GenreEntry.CONTENT_URI, id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case THEME: {
                long id = db.insertWithOnConflict(ThemeEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_NONE);
                if (id != -1)
                    returnUri = ContentUris.withAppendedId(ThemeEntry.CONTENT_URI, id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unsupported uri: "+uri);
        }

        if (context!=null)
            context.getContentResolver().notifyChange(uri,null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db =mDbHelper.getWritableDatabase();
        Context context = getContext();
        int deletedRows;

        switch(sUriMatcher.match(uri)){
            case MANGA: {
                deletedRows = db.delete(MangaEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case GENRE: {
                deletedRows = db.delete(GenreEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case THEME: {
                deletedRows = db.delete(ThemeEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unsupported uri: "+uri);
        }

        if (deletedRows>0){
            if (context!=null)
                context.getContentResolver().notifyChange(uri,null);
        }
        return deletedRows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Context context = getContext();
        int updatedRows;

        switch(sUriMatcher.match(uri)){
            case MANGA:{
                updatedRows = db.update(MangaEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
            }
            case GENRE:{
                updatedRows = db.update(GenreEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
            }
            case THEME:{
                updatedRows = db.update(ThemeEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unsupported uri: "+uri);
        }

        if (updatedRows>0){
            if (context!=null)
                context.getContentResolver().notifyChange(uri,null);
        }
        return updatedRows;
    }

    @Override
    public void shutdown() {
        mDbHelper.close();
        super.shutdown();
    }
}
