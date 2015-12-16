package utils;

import android.graphics.Bitmap;

/**
 * Created by neikila on 15.12.15.
 */
public class BitmapReady extends OttoMessage {
    final private Bitmap bitmap;

    public BitmapReady(Bitmap bitmap) {
        messageType = MessageType.BitmapReady;
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
