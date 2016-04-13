package com.animenavigator.http;

import android.util.Log;

import com.animenavigator.utils.CloudFlare;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by a.g.seliverstov on 11.04.2016.
 */
public class AnimeNewsNetworkClientImpl implements AnimeNewsNetworkClient {
    private static final String TAG = AnimeNewsNetworkClientImpl.class.getSimpleName();

    private OkHttpClient client;
    private String baseUrl;


    public AnimeNewsNetworkClientImpl(){
        client = new OkHttpClient();
        if (CloudFlare.BYPASS_CLOUDFLARE){
            baseUrl = CloudFlare.bypass(AnimeNewsNetworkClient.BASE_URL);
        }else{
            baseUrl = AnimeNewsNetworkClient.BASE_URL;
        }
    }

    @Override
    public String queryTitlesXML(Integer skip, Integer list, AnimeNewsNetworkClient.AnimeType type, String name) {
        if (skip < 0 || list < 0) return null;
        try{
            HttpUrl.Builder urlBuilder = HttpUrl.parse(baseUrl + QUERY_TITLES).newBuilder();
            if (skip>0) {
                urlBuilder.addQueryParameter(QUERY_TITLES_NSKIP_PARAMETER, skip.toString());
            }
            if (list == 0) {
                urlBuilder.addQueryParameter(QUERY_TITLES_NLIST_PARAMETER, QUERY_TITLES_NLIST_PARAMETER_ALL_VALUE);
            }else {
                urlBuilder.addQueryParameter(QUERY_TITLES_NLIST_PARAMETER, list.toString());
            }
            if (type!=null){
                urlBuilder.addQueryParameter(QUERY_TITLES_TYPE_PARAMETER, type.toString());
            }
            if (name!=null && !name.trim().equals("")){
                urlBuilder.addQueryParameter(QUERY_TITLES_NAME_PARAMETER,name);
            }
            String url = urlBuilder.build().toString();
            Log.i(TAG, "Query titles: " + url);
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()){
                return response.body().string();
            }else{
                Log.e(TAG,"Unexpected code " + response);
                return null;
            }
        }catch(IOException e){
            Log.e(TAG,e.getMessage(),e);
            return null;
        }
    }

    @Override
    public String queryDetailsXml(Integer id, AnimeType type) {
        if (id <= 0) return null;
        try{
            HttpUrl url = HttpUrl.parse(baseUrl+QUERY_DETAILS).newBuilder().addQueryParameter((type==null?QUERY_DETAILS_TITLE_PARAMETER:type.toString()),id.toString()).build();
            Log.i(TAG, "Query details: " + url.toString());
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()){
                return response.body().string();
            }else{
                Log.e(TAG,"Unexpected code " + response);
                return null;
            }
        }catch(IOException e){
            Log.e(TAG, e.getMessage(), e);
            return null;
        }
    }
}
