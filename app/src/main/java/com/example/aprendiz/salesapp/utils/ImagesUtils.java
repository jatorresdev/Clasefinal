package com.example.aprendiz.salesapp.utils;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by jatorresdev on 30/09/16.
 */
public class ImagesUtils {

    public static String getPath(ContentResolver resolver, Uri uri) throws Exception {
        // this method will be used to get real path of Image chosen from gallery.
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = resolver.query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }
}
