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

    public static final String PATH_RELATED = "related";

    public static final String PATH_EPISODE = "episode";

    public static final String PATH_LINK = "link";

    public static final String PATH_REVIEW = "review";

    public static final String PATH_SEARCH = "search";

    public static final String PATH_FAVORITE = "favorite";

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
        public static final String VINTAGE_COLUMN = "vintage";

        public static Uri buildRelatedForManga(long mangaId) {
            return CONTENT_URI.buildUpon().appendPath(PATH_RELATED).appendPath(String.valueOf(mangaId)).build();
        }

        public static Uri buildSearch() {
            return CONTENT_URI.buildUpon().appendPath(PATH_SEARCH).build();
        }

        public static Uri buildMangaWithId(long mangaId) {
            return CONTENT_URI.buildUpon().appendPath(String.valueOf(mangaId)).build();
        }

        public static Uri buildFavorite() {
            return CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE).build();
        }
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

    public static final class MangaTitleEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TITLE).build();
        public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TITLE;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TITLE;

        public static final String TABLE_NAME = "manga_titles";

        public static final String MANGA_ID_COLUMN = "manga_id";
        public static final String NAME_COLUMN = "title";
        public static final String LANG_COLUMN = "lang";

        public static Uri buildTitlesForManga(long mangaId) {
            return CONTENT_URI.buildUpon().appendPath(PATH_MANGA).appendPath(String.valueOf(mangaId)).build();
        }
    }

    public static final class RelatedEntry implements BaseColumns{
        public static final String TABLE_NAME = "related";

        public static final String NAME_COLUMN = "related";
    }

    public static final class MangaRelatedEntry  implements BaseColumns{
        public static final String TABLE_NAME = "manga_related";

        public static final String MANGA_ID_COLUMN = "manga_id";
        public static final String REL_ID_COLUMN = "rel_id";
        public static final String REL_MANGA_ID_COLUMN = "rel_manga_id";
    }

    public static final class MangaEpisodeEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_EPISODE).build();
        public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EPISODE;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EPISODE;

        public static final String TABLE_NAME = "manga_episodes";

        public static final String MANGA_ID_COLUMN = "manga_id";
        public static final String NAME_COLUMN = "episode";
        public static final String NUM_COLUMN = "num";
        public static final String PART_COLUMN = "part";
        public static final String LANG_COLUMN = "lang";

        public static Uri buildEpisodesForManga(long mangaId) {
            return CONTENT_URI.buildUpon().appendPath(PATH_MANGA).appendPath(String.valueOf(mangaId)).build();
        }
    }

    public static final class MangaReviewEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEW).build();
        public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;

        public static final String TABLE_NAME = "manga_reviews";

        public static final String MANGA_ID_COLUMN = "manga_id";
        public static final String NAME_COLUMN = "review";
        public static final String HREF_COLUMN = "href";

        public static Uri buildReviewsForManga(long mangaId) {
            return CONTENT_URI.buildUpon().appendPath(PATH_MANGA).appendPath(String.valueOf(mangaId)).build();
        }
    }

    public static final class MangaLinkEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_LINK).build();
        public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LINK;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LINK;

        public static final String TABLE_NAME = "manga_links";

        public static final String MANGA_ID_COLUMN = "manga_id";
        public static final String NAME_COLUMN = "link";
        public static final String HREF_COLUMN = "href";
        public static final String LANG_COLUMN = "lang";

        public static Uri buildLinksForManga(long mangaId) {
            return CONTENT_URI.buildUpon().appendPath(PATH_MANGA).appendPath(String.valueOf(mangaId)).build();
        }
    }

    public static final class MangaNewsEntry implements BaseColumns {
        public static final String TABLE_NAME = "manga_news";

        public static final String MANGA_ID_COLUMN = "manga_id";
        public static final String NAME_COLUMN = "news";
        public static final String HREF_COLUMN = "href";
        public static final String DATE_COLUMN = "date";
    }

    public static final class MangaMusicEntry implements BaseColumns {
        public static final String TABLE_NAME = "manga_music";

        public static final String MANGA_ID_COLUMN = "manga_id";
        public static final String NAME_COLUMN = "music";
        public static final String TYPE_COLUMN = "type";
    }

}
