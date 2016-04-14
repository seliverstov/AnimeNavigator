package com.animenavigator.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageView;

import com.animenavigator.utils.CloudFlare;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Callback;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by a.g.seliverstov on 01.04.2016.
 */
public class ImageLoader {
    private static final String TAG = ImageLoader.class.getSimpleName();

    private static final int READ_TIMEOUT = 60;
    private static final int CONNECT_TIMEOUT = 60;

    private static Picasso.Builder sPicassoBuilder;


    private static Picasso initPicasso(Context context){
        if (sPicassoBuilder==null) {
            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.setReadTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
            okHttpClient.setConnectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
            okHttpClient.networkInterceptors().add(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder().header("Cache-Control", "max-age=" + (60 * 60 * 24 * 365)).build();
                }
            });

            okHttpClient.setCache(new Cache(context.getCacheDir(), Integer.MAX_VALUE));
            sPicassoBuilder = new Picasso.Builder(context).downloader(new OkHttpDownloader(okHttpClient)).loggingEnabled(true).indicatorsEnabled(true);
        }
        return sPicassoBuilder.build();
    }

    private static String processUrl(String url, Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return (sp.getBoolean(Const.SP_ENABLE_CLOUDFLARE_KEY,false))? CloudFlare.bypass(url):url;
    }

    public static void loadImageToView(final String url, final Context context, final ImageView imageView){
        initPicasso(context).load(url).into(imageView, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                initPicasso(context).load(processUrl(url, context)).into(imageView);
            }
        });
    }

    public static void loadImageToView(final String url, final Context context, final Target target){
        initPicasso(context).load(url).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                target.onBitmapLoaded(bitmap,from);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                initPicasso(context).load(processUrl(url, context)).into(target);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                target.onPrepareLoad(placeHolderDrawable);
            }
        });
    }

    public static Bitmap getBitmap(final String url, final Context context){
        Bitmap bitmap = null;
        try {
            bitmap = initPicasso(context).load(url).get();
        }catch(Exception e){
            Log.e(TAG, e.getMessage(), e);
        }
        return bitmap;
    }
}
