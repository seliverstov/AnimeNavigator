package com.animenavigator;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.concurrent.TimeUnit;

/**
 * Created by a.g.seliverstov on 01.04.2016.
 */
public class ImageLoader {
    private static final int READ_TIMEOUT = 60;
    private static final int CONNECT_TIMEOUT = 60;
    private static final boolean BYPASS_CLOUDFLARE = true;

    private static Picasso initPicasso(Context context){
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
        return new Picasso.Builder(context).downloader(new OkHttpDownloader(okHttpClient)).build();
    }

    private static String processUrl(String url){
        return (BYPASS_CLOUDFLARE) ? CloudFlare.bypass(url) : url;
    }

    public static void loadImageToView(String url, Context context, ImageView imageView){
        initPicasso(context).load(processUrl(url)).into(imageView);
    }

    public static void loadImageToView(String url, Context context, Target target){
        initPicasso(context).load(processUrl(url)).into(target);
    }
}
