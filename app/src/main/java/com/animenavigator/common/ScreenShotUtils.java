package com.animenavigator.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.animenavigator.R;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by a.g.seliverstov on 12.04.2016.
 */
public class ScreenShotUtils {
    private static final String TAG = ScreenShotUtils.class.getSimpleName();
    private Context mContext;
    public ScreenShotUtils(Context context){
        this.mContext = context;
    }

    public Bitmap getScreenShot(View v) {
        View view = v.getRootView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache(true);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache(true));
        view.setDrawingCacheEnabled(false);
        return bitmap;
    }

    public File storeScreenShot(Bitmap bitmap, String fileName){
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), mContext.getResources().getString(R.string.app_name));
        if (!storageDir.exists() && !storageDir.mkdirs()){
            return null;
        }else {
            File file = new File(storageDir,fileName);
            Log.i(TAG, file.getAbsolutePath());
            try {
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
                return null;
            }
            return file;
        }
    }

}
