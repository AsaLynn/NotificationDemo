package com.zxn.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.v4.app.NotificationCompat;

import static android.os.Build.VERSION_CODES.LOLLIPOP_MR1;


/**
 * GeneralNotification
 * GeneralNotification
 * 通知构建器.兼容8.0
 * Created by zxn on 2019/4/27.
 */
public class GeneralNotification {

    private Context mContext;
    private Notification mNotification;
    private NotificationManager mNotificationManager;
    private int mNotificationId;

    private GeneralNotification() {

    }

    public static class Builder {
        private Context context;
        private NotificationManager notificationManager;
        private Notification notification;
        @DrawableRes
        private int mIcon;
        private int mFlags = Notification.FLAG_AUTO_CANCEL;
        private boolean mAutoCancel;
        private PendingIntent mContentIntent;
        private CharSequence mContentTitle;
        private CharSequence mContentText;
        private int notificationId;

        public Builder(Context context) {
            this.context = context;
            this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        public Builder icon(int icon) {
            mIcon = icon;
            return this;
        }

        public Builder flags(int flags) {
            mFlags = flags;
            return this;
        }

        public Builder setAutoCancel(boolean autoCancel) {
            mAutoCancel = autoCancel;
            return this;
        }

        public Builder setContentIntent(PendingIntent intent) {
            mContentIntent = intent;
            return this;
        }

        public Builder setContentTitle(CharSequence title) {
            this.mContentTitle = title;
            return this;
        }

        public Builder setContentText(CharSequence text) {
            this.mContentText = text;
            return this;
        }

        public Builder notificationId(int notificationId) {
            this.notificationId = notificationId;
            return this;
        }

        public GeneralNotification build() {

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                //SDK_INT < 16
                notification = new Notification();
//            notification.icon = android.R.drawable.stat_sys_download_done;
                notification.icon = mIcon;
                notification.flags |= mFlags;
                //notification.setLatestEventInfo(this, aInfo.mFilename, contentText, pi);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
                //  SDK_INT >= 16 && SDK_INT < 22   (4.4-->19)
                notification = new Notification.Builder(context)
                        .setAutoCancel(mAutoCancel)
                        .setContentTitle(mContentTitle)
                        .setContentText(mContentText)
                        .setSmallIcon(mIcon)
                        .setContentIntent(mContentIntent)
                        .setWhen(System.currentTimeMillis())
                        .build();
            } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1 && Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O) {
                // SDK_INT >= 22 &&  SDK_INT < 26   (6.0-->23)
                notification = new NotificationCompat.Builder(context)
                        .setContentTitle(mContentTitle)
                        .setContentText(mContentText)
                        .setSmallIcon(mIcon)
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), mIcon))
                        .setContentIntent(mContentIntent)
                        .setWhen(System.currentTimeMillis())
                        .build();
            } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                //SDK_INT >= 26
                String CHANNEL_ID = "my_channel_01";
                CharSequence name = "my_channel";
                String Description = "This is my channel";
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                mChannel.setDescription(Description);
                mChannel.enableLights(true);
                mChannel.setLightColor(Color.RED);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                mChannel.setShowBadge(false);

                notificationManager.createNotificationChannel(mChannel);

                notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(mIcon)
                        .setContentTitle(mContentTitle)
                        .setContentText(mContentText)
                        .setAutoCancel(mAutoCancel)
                        .setWhen(System.currentTimeMillis())
                        .setContentIntent(mContentIntent)
                        .build();
            }

            GeneralNotification commonNotification = new GeneralNotification();
            commonNotification.mNotification = this.notification;
            commonNotification.mNotificationManager = this.notificationManager;
            commonNotification.mNotificationId = this.notificationId;
            return commonNotification;
        }

    }

    public void sendNotify() {
        if (mNotificationManager != null && null != mNotification) {
            mNotificationManager.notify(mNotificationId, mNotification);
        }
    }
}
