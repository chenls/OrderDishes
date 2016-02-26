package chenls.orderdishes.image;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageLoader {
    private final Context context;
    private final MemoryCache memoryCache = new MemoryCache();
    private final AbstractFileCache fileCache;
    private final Map<ImageView, String> imageViews = Collections
            .synchronizedMap(new WeakHashMap<ImageView, String>());
    // 线程池
    private final ExecutorService executorService;
    private int requiredSize = 100;

    public ImageLoader(Context context) {
        this.context = context;
        fileCache = new FileCache(context);
        executorService = Executors.newFixedThreadPool(5);
    }

    /**
     * 加载图片
     *
     * @param url                 url
     * @param imageView           imageView
     * @param isLoadOnlyFromCache 是否只从缓冲中加载
     * @param requiredSize        要求的尺寸
     */
    public void DisplayImage(String url, ImageView imageView, boolean isLoadOnlyFromCache, int requiredSize) {
        this.requiredSize = requiredSize;
        DisplayImage(url, imageView, isLoadOnlyFromCache);
    }

    public void DisplayImage(String url, ImageView imageView, boolean isLoadOnlyFromCache) {
        imageViews.put(imageView, url);
        // 先从内存缓存中查找

        Bitmap bitmap = memoryCache.get(url);
        if (bitmap != null)
            imageView.setImageBitmap(bitmap);
        else if (!isLoadOnlyFromCache) {

            // 若没有的话则开启新线程加载图片
            queuePhoto(url, imageView);
        }
    }

    private void queuePhoto(String url, ImageView imageView) {
        PhotoToLoad p = new PhotoToLoad(url, imageView);
        executorService.submit(new PhotosLoader(p));
    }

    private Bitmap getBitmap(String url) {
        File f = fileCache.getFile(context, url);

        // 先从文件缓存中查找是否有
        Bitmap b = null;
        if (f != null && f.exists()) {
            b = decodeFile(f);
        }
        if (b != null) {
            return b;
        }
        // 最后从指定的url中下载图片
        try {
            Bitmap bitmap;
            URL imageUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) imageUrl
                    .openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);
            InputStream is = conn.getInputStream();
            OutputStream os = null;
            if (f != null) {
                os = new FileOutputStream(f);
            }
            CopyStream(is, os);
            if (os != null) {
                os.close();
            }
            bitmap = decodeFile(f);
            return bitmap;
        } catch (Exception ex) {
            Log.e("", "getBitmap catch Exception...\nmessage = " + ex.getMessage());
            return null;
        }
    }

    // decode这个图片并且按比例缩放以减少内存消耗，虚拟机对每张图片的缓存大小也是有限制的
    private Bitmap decodeFile(File f) {
        try {
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            // Find the correct scale value. It should be the power of 2.

            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < requiredSize
                        || height_tmp / 2 < requiredSize)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException ignored) {
        }
        return null;
    }

    // Task for the queue
    private class PhotoToLoad {
        public final String url;
        public final ImageView imageView;

        public PhotoToLoad(String u, ImageView i) {
            url = u;
            imageView = i;
        }
    }

    class PhotosLoader implements Runnable {
        final PhotoToLoad photoToLoad;

        PhotosLoader(PhotoToLoad photoToLoad) {
            this.photoToLoad = photoToLoad;
        }

        @Override
        public void run() {
            if (imageViewReused(photoToLoad))
                return;
            Bitmap bmp = getBitmap(photoToLoad.url);
            memoryCache.put(photoToLoad.url, bmp);
            if (imageViewReused(photoToLoad))
                return;
            BitmapDisplay bd = new BitmapDisplay(bmp, photoToLoad);
            // 更新的操作放在UI线程中
            Activity a = (Activity) photoToLoad.imageView.getContext();
            a.runOnUiThread(bd);
        }
    }


    //防止图片错位
    private boolean imageViewReused(PhotoToLoad photoToLoad) {
        String tag = imageViews.get(photoToLoad.imageView);
        return tag == null || !tag.equals(photoToLoad.url);
    }

    // 用于在UI线程中更新界面
    class BitmapDisplay implements Runnable {
        final Bitmap bitmap;
        final PhotoToLoad photoToLoad;

        public BitmapDisplay(Bitmap b, PhotoToLoad p) {
            bitmap = b;
            photoToLoad = p;
        }

        public void run() {
            if (imageViewReused(photoToLoad))
                return;
            if (bitmap != null)
                photoToLoad.imageView.setImageBitmap(bitmap);

        }
    }

    public void clearCache() {
        memoryCache.clear();
        fileCache.clear();
    }

    private static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
            Log.e("", "CopyStream catch Exception...");
        }
    }
}