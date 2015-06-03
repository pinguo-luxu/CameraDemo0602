package com.camera.demol.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by luxu on 15-6-3.
 */
public class BitmapUtil {

    /**
     * 旋转Bitmap
     *
     * @param b
     * @param rotateDegree
     * @return
     */
    public static Bitmap getRotateBitmap(Bitmap b, float rotateDegree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(rotateDegree);
        Bitmap rotaBitmap = null;
        try {
            rotaBitmap = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, false);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return rotaBitmap;
    }

    public static void recyle(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }

}
