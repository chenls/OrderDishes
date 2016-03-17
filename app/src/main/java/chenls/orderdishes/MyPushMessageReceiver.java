package chenls.orderdishes;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import org.json.JSONException;
import org.json.JSONObject;

import chenls.orderdishes.activity.NotifyActivity;
import cn.bmob.push.PushConstants;

public class MyPushMessageReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent0) {
        if (intent0.getAction().equals(PushConstants.ACTION_MESSAGE)) {
            String message = "";
            try {
                JSONObject jsonObject = new JSONObject(
                        intent0.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING));
                message = jsonObject.getString("alert");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(context, NotifyActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);
            Notification notification;
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setContentTitle("有新消息")
                    .setContentText(message)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources()
                            , R.mipmap.ic_launcher))
                    .setContentIntent(pendingIntent);
            // API level >= 16.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                notification = builder.build();
            } else {
                notification = builder.getNotification();
            }
            notification.flags = Notification.FLAG_AUTO_CANCEL;
            // 手机振动：
            notification.defaults |= Notification.DEFAULT_VIBRATE;
            notification.vibrate = new long[]{0, 100, 200, 300};
            // 发出提示音
            notification.defaults |= Notification.DEFAULT_SOUND;
            NotificationManager notificationManager = (NotificationManager)
                    context.getSystemService(context.NOTIFICATION_SERVICE);
            notificationManager.notify(1, notification);
        }
    }
}
