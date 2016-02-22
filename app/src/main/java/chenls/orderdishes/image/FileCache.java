package chenls.orderdishes.image;

import android.content.Context;


public class FileCache extends AbstractFileCache {

    public FileCache(Context context) {
        super(context);

    }


    @Override
    public String getSavePath(Context context,String url) {
        String filename = String.valueOf(url.hashCode());
        return getCacheDir(context) + filename;
    }

    @Override
    public String getCacheDir(Context context) {

        return FileManager.getSaveFilePath(context);
    }

}
