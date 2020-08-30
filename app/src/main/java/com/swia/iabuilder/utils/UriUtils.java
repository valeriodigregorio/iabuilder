package com.swia.iabuilder.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.LinearLayout;

import androidx.core.content.FileProvider;

import com.swia.iabuilder.models.Army;
import com.swia.iabuilder.parsers.vassal.VassalParser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class UriUtils {

    public static String getFilename(Context context, Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = context.getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public static Bitmap toBitmap(View view) {
        if (view == null) {
            return null;
        }
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        int width = view.getMeasuredWidth();
        int height = view.getMeasuredHeight();
        if (width == 0 || height == 0) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Drawable drawable = view.getBackground();
        if (drawable != null) {
            drawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return bitmap;
    }

    public static Bitmap mergeBitmaps(List<Bitmap> bitmaps, int orientation) {
        if (bitmaps == null || bitmaps.size() == 0) {
            return null;
        }
        int width = 0;
        int height = 0;
        for (Bitmap bitmap : bitmaps) {
            if (orientation == LinearLayout.HORIZONTAL) {
                width += bitmap.getWidth();
            } else {
                width = Math.max(width, bitmap.getWidth());
            }
            if (orientation == LinearLayout.VERTICAL) {
                height += bitmap.getHeight();
            } else {
                height = Math.max(height, bitmap.getHeight());
            }
        }
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        width = 0;
        height = 0;
        for (Bitmap bitmap : bitmaps) {
            canvas.drawBitmap(bitmap, width, height, null);
            if (orientation == LinearLayout.HORIZONTAL) {
                width += bitmap.getWidth();
            } else {
                height += bitmap.getHeight();
            }
        }
        return result;
    }

    private static Uri toUri(Context context, File file) {
        if (file == null) {
            return null;
        }
        file.deleteOnExit();
        return FileProvider.getUriForFile(context, "com.swia.iabuilder.fileprovider", file);
    }

    private static File toFile(Context context, Bitmap bitmap, String filename, Bitmap.CompressFormat format) {
        if (bitmap == null) {
            return null;
        }

        try {
            File file = context.getFileStreamPath(filename);
            FileOutputStream stream = new FileOutputStream(file);
            bitmap.compress(format, 100, stream);
            stream.close();
            return file;
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static File toFile(Context context, Army army) {
        if (army == null) {
            return null;
        }

        try {
            File file = context.getFileStreamPath(army.getName());
            FileOutputStream stream = new FileOutputStream(file);
            VassalParser.save(stream, army);
            stream.close();
            return file;
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Uri toUri(Context context, Bitmap bitmap, String filename, Bitmap.CompressFormat format) {
        File file = toFile(context, bitmap, filename + "." + format.name().toLowerCase(), format);
        return toUri(context, file);
    }

    public static Uri toUri(Context context, Army army) {
        File file = toFile(context, army);
        return toUri(context, file);
    }

}
