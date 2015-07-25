package com.woalk.apps.xposed.htcblinkfeedauthorizer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class Common {
    private Common() {
    }

    public static final String versionName = "2.0";

    /**
     * Convert a {@link Drawable} into an {@link Bitmap} object.
     * <p>
     * Draws the {@code Drawable} onto a RAM-only {@link Canvas} and grabs the resulting
     * {@code Bitmap}.
     * </p>
     * <p>
     * If the {@code Drawable} is a {@link BitmapDrawable}, no conversion is needed, and no
     * conversion will be done.
     * </p>
     *
     * @param drawable The {@link Drawable} to convert. Can be any drawable.
     * @return A {@link Bitmap} representing the given {@code drawable}.
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
            // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable
                    .getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * Searches a {@link ViewGroup} for the first {@link ImageView} it can find in its child views.
     *
     * @param container The container {@link ViewGroup}.
     * @return The first {@link ImageView} in the {@code container ViewGroup} (counted by child
     * indexes), or {@code null} if no {@code ImageView} was found.
     */
    public static ImageView findFirstImageView(ViewGroup container) {
        for (int i = 0; i < container.getChildCount(); i++) {
            View v = container.getChildAt(i);
            if (v instanceof ImageView) {
                return (ImageView) v;
            } else if (v instanceof ViewGroup) {
                return findFirstImageView((ViewGroup) v);
            }
        }
        return null;
    }

    /**
     * Change light value of a color.
     * <p>
     * <b>Warning:</b> Removes alpha channel. Only pass colors with alpha = {@code 0xFF}.
     * </p>
     *
     * @param color      The {@link Color} {@code int} to use.
     * @param multiplier The {@code float} multiplier to apply to the light value.
     * @return A modified {@link Color} {@code int}.
     */
    public static int enlightColor(int color, float multiplier) {
        float[] hsv = new float[3];
        Color.RGBToHSV(Color.red(color), Color.green(color), Color.blue(color), hsv);
        hsv[2] = hsv[2] * multiplier;
        return Color.HSVToColor(hsv);
    }
}
