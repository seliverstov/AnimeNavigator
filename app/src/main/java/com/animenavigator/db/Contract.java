package com.animenavigator.db;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by a.g.seliverstov on 04.04.2016.
 */
public class Contract {
    public static final String CONTENT_AUTHORITY = "com.animenavigator.provider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MANGA = "manga";

    public static final String PATH_GENRE = "genre";

    public static final String PATH_THEME = "theme";

    public static final String PATH_PERSON = "person";

    public static final String PATH_TASK = "task";

    public static final String PATH_TITLE = "title";

    public static final class MangaEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MANGA).build();
        public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MANGA;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MANGA;

        public static final String TABLE_NAME = "manga";

        public static final String TYPE_COLUMN = "type";
        public static final String NAME_COLUMN = "name";
        public static final String PLOT_COLUMN = "plot";
        public static final String VOTES_COLUMN = "votes";
        public static final String WEIGHTED_SCORE_COLUMN = "weighted_score";
        public static final String BAYESIAN_SCORE_COLUMN = "bayesian_score";
        public static final String PAGES_COLUMN = "pages";
        public static final String EPISODES_COLUMN = "episodes";
        public static final String OBJECTIONABLE_CONTENT_COLUMN = "objectionable_content";
        public static final String PICTURE_COLUMN = "picture";
        public static final String COPYRIGHT_COLUMN = "copyright";
    }

    public static final class GenreEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_GENRE).build();
        public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_GENRE;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_GENRE;

        public static final String TABLE_NAME = "genres";

        public static final String NAME_COLUMN = "genre";

        public static Uri buildGenreForManga(long mangaId) {
            return CONTENT_URI.buildUpon().appendPath(PATH_MANGA).appendPath(String.valueOf(mangaId)).build();
        }
    }

    public static final class MangaGenreEntry implements BaseColumns {
        public static final String TABLE_NAME = "manga_genres";

        public static final String MANGA_ID_COLUMN = "manga_id";
        public static final String GENRE_ID_COLUMN = "genre_id";
    }

    public static final class ThemeEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_THEME).build();
        public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_THEME;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_THEME;

        public static final String TABLE_NAME = "themes";

        public static final String NAME_COLUMN = "theme";

        public static Uri buildThemesForManga(long mangaId) {
            return CONTENT_URI.buildUpon().appendPath(PATH_MANGA).appendPath(String.valueOf(mangaId)).build();
        }
    }

    public static final class MangaThemeEntry implements BaseColumns {
        public static final String TABLE_NAME = "manga_themes";

        public static final String MANGA_ID_COLUMN = "manga_id";
        public static final String THEME_ID_COLUMN = "theme_id";


    }

    public static final class PersonEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_PERSON).build();
        public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PERSON;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PERSON;
        public static final String TABLE_NAME = "persons";

        public static final String NAME_COLUMN = "person";

        public static Uri buildPersonsForManga(long mangaId) {
            return CONTENT_URI.buildUpon().appendPath(PATH_MANGA).appendPath(String.valueOf(mangaId)).build();
        }

        public static Uri buildPersonsAndTasksForManga(long mangaId) {
            return CONTENT_URI.buildUpon().appendPath(PATH_TASK).appendPath(PATH_MANGA).appendPath(String.valueOf(mangaId)).build();
        }
    }

    public static final class TaskEntry implements BaseColumns{
        public static final String TABLE_NAME = "tasks";

        public static final String NAME_COLUMN = "task";
    }

    public static final class MangaStaffEntry implements BaseColumns{
        public static final String TABLE_NAME = "manga_staff";

        public static final String MANGA_ID_COLUMN = "manga_id";
        public static final String TASK_ID_COLUMN = "task_id";
        public static final String PERSON_ID_COLUMN = "person_id";
    }

    public static final class MangaTitleEntry {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TITLE).build();
        public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TITLE;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TITLE;

        public static final String TABLE_NAME = "manga_titles";

        public static final String _ID = "_id";
        public static final String MANGA_ID_COLUMN = "manga_id";
        public static final String NAME_COLUMN = "title";
        public static final String LANG_COLUMN = "lang";

        public static Uri buildTitlesForManga(long mangaId) {
            return CONTENT_URI.buildUpon().appendPath(PATH_MANGA).appendPath(String.valueOf(mangaId)).build();
        }
    }

}
