package chenls.orderdishes.image;


import android.content.Context;
import android.os.Environment;

class FileManager {
    private static String re;

    public static String getSaveFilePath(Context context) {
        if (re == null)
            re = getRootFilePath(context) + "/image/";
        return re;
    }

    private static boolean hasSDCard() {
        String status = Environment.getExternalStorageState();
        return status.equals(Environment.MEDIA_MOUNTED);
    }

    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    private static String getRootFilePath(Context context) {
        if (hasSDCard() && isExternalStorageReadable()) {
            return context.getExternalFilesDir("") + "";
        } else {
            return context.getFilesDir() + "";
        }
    }
}
