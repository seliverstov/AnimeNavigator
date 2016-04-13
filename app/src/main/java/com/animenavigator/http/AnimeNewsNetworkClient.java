package com.animenavigator.http;


/**
 * Created by a.g.seliverstov on 11.04.2016.
 */
public interface AnimeNewsNetworkClient {
    String BASE_URL = "http://cdn.animenewsnetwork.com";
    String QUERY_TITLES = "/encyclopedia/reports.xml?id=155";
    String QUERY_TITLES_NLIST_PARAMETER = "nlist";
    String QUERY_TITLES_NSKIP_PARAMETER = "nskip";
    String QUERY_TITLES_NAME_PARAMETER = "name";
    String QUERY_TITLES_TYPE_PARAMETER = "type";
    String QUERY_TITLES_NLIST_PARAMETER_ALL_VALUE = "all";

    String QUERY_DETAILS = "/encyclopedia/api.xml";
    String QUERY_DETAILS_TITLE_PARAMETER = "title";

    enum AnimeType {
        ANIME("anime"),
        MANGA("manga");

        private final String text;

        private AnimeType(final String text){
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }
    String queryTitlesXML(Integer skip, Integer list, AnimeType type, String name);

    String queryDetailsXml(Integer id, AnimeType type);
}
