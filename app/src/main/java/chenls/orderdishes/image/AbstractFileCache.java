package chenls.orderdishes.image;

import android.content.Context;
import android.util.Log;

import java.io.File;


abstract class AbstractFileCache {

    private final String dirString;

    public AbstractFileCache(Context context) {

        dirString = getCacheDir(context);
        boolean ret = FileHelper.createDirectory(dirString);
        Log.e("", "FileHelper.createDirectory:" + dirString + ", ret = " + ret);
    }

    public File getFile(Context context, String url) {
        return new File(getSavePath(context, url));
    }

    protected abstract String getSavePath(Context context, String url);

    public abstract String getCacheDir(Context context);

    public void clear() {
        FileHelper.deleteDirectory(dirString);
    }

}
