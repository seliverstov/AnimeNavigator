package com.animenavigator.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import static com.animenavigator.db.Contract.*;

/**
 * Created by a.g.seliverstov on 04.04.2016.
 */
public class Provider extends ContentProvider{
    private static final UriMatcher sUriMatcher;

    static final int MANGA = 100;
    static final int MANGA_WITH_ID = 101;
    static final int MANGA_RELATED_FOR_MANGA = 102;

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

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = CONTENT_AUTHORITY;
        sUriMatcher.addURI(authority, PATH_MANGA, MANGA);
        sUriMatcher.addURI(authority, PATH_MANGA+"/#", MANGA_WITH_ID);
        sUriMatcher.addURI(authority, PATH_MANGA+"/"+PATH_RELATED+"/#", MANGA_RELATED_FOR_MANGA);

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
                        },MangaStaffEntry.MANGA_ID_COLUMN+" = ?",new String[]{id},null,null,TaskEntry.TABLE_NAME+"."+TaskEntry.NAME_COLUMN);
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
            default:
                throw new UnsupportedOperationException("Unsupported uri: "+uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(),uri);
        return retCursor;
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
            default:
                throw new UnsupportedOperationException("Unsupported uri: "+uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
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

        getContext().getContentResolver().notifyChange(uri,null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db =mDbHelper.getWritableDatabase();
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
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return deletedRows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
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
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return updatedRows;
    }

    @Override
    public void shutdown() {
        mDbHelper.close();
        super.shutdown();
    }
}
