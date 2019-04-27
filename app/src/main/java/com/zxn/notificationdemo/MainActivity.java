package com.zxn.notificationdemo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zxn.notification.GeneralNotification;

import static android.os.Build.VERSION_CODES.LOLLIPOP_MR1;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    protected TextView tv1;
    protected TextView tv2;
    protected TextView tv3;
    protected TextView tv4;
    private NotificationManager mNotificationManager;
    private PendingIntent pendingIntent;
    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);
        initView();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv1) {
            test1();
        } else if (view.getId() == R.id.tv2) {
            test2();
        } else if (view.getId() == R.id.tv3) {
            test3();
        } else if (view.getId() == R.id.tv4) {
            test4();
        }
    }

    private void test4() {
        Intent intent = new Intent();
        String action = getPackageName();
        Log.i(TAG, "action: " + action);
        intent.setAction(action);
        PendingIntent mPendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        new GeneralNotification.Builder(this)
                .icon(R.drawable.ic_xjl)
                .setContentTitle("小精灵商户通")
                .setContentText("会员卡收款6.0元")
                .notificationId(100)
                .setAutoCancel(true)
                .setContentIntent(mPendingIntent)
                .build()
                .sendNotify();
    }

    private void test1() {
        if (Build.VERSION.SDK_INT >= 16) {
            //创建Notification
            Notification mNotification = new Notification.Builder(this)
                    .setTicker("Notification:你有新的消息!")//来通知的时的提示文字
                    .setDefaults(Notification.DEFAULT_SOUND)//设置来消息的声音
                    .setSmallIcon(R.mipmap.ic_launcher_round)//展示小图标
                    .setContentTitle("this is ContentTitle !")//展示在状态栏的标题
                    .setContentText("this is ContentText!")//展示在状态栏的内容
                    .setContentIntent(pendingIntent)//状态栏的点击意图
                    //.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.payment_method_wechat))
                    .build();//所有属性设置后,到最后调用此方法

            //开始执行通知
            // 如果该NOTIFICATION_ID的通知已存在，会显示最新通知的相关信息 ，比如tickerText 等
            mNotificationManager.notify(1, mNotification);
        } else {
            Toast.makeText(this, "版本低于16!", Toast.LENGTH_SHORT).show();
        }
    }

    private void test3() {
        final int NOTIFICATION_ID = 12234;
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        //准备intent
        Intent intent = new Intent();
        String action = "com.tamic.myapp.action";
        intent.setAction(action);

        //notification
        Notification notification = null;
        String contentText = "593066063";
        // 构建 PendingIntent
        PendingIntent pi = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //版本兼容

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            notification = new Notification();
            notification.icon = android.R.drawable.stat_sys_download_done;
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            //notification.setLatestEventInfo(this, aInfo.mFilename, contentText, pi);

        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O && Build.VERSION.SDK_INT >= LOLLIPOP_MR1) {
            notification = new NotificationCompat.Builder(this)
                    .setContentTitle("Title")
                    .setContentText(contentText)
                    .setSmallIcon(android.R.drawable.stat_sys_download_done)
                    .setContentIntent(pi).build();

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN &&
                Build.VERSION.SDK_INT <= LOLLIPOP_MR1) {
            notification = new Notification.Builder(this)
                    .setAutoCancel(false)
                    .setContentIntent(pi)
                    .setSmallIcon(android.R.drawable.stat_sys_download_done)
                    .setWhen(System.currentTimeMillis())
                    .build();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


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

            notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(android.R.drawable.stat_sys_download_done)
                    .setContentTitle("Title").build();
        }
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    private static final int NOTIFICATION_ID = 1001;
    private String notificationId = "channelId";
    private String notificationName = "channelName";

    private void test2() {
        Log.i(TAG, "test2: ");
        Notification.Builder builder = new Notification.Builder(this)
//                .setSmallIcon(R.drawable.logo_small)
                .setContentTitle("测试服务")
                .setContentText("我正在运行");
        //设置Notification的ChannelID,否则不能正常显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(notificationId);
            Notification notification = builder.build();
            NotificationChannel channel = new NotificationChannel(notificationId, notificationName, NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(channel);
        }
    }

    private void initView() {
        tv1 = (TextView) findViewById(R.id.tv1);
        tv1.setOnClickListener(MainActivity.this);

        //获取NotificationManager
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent("android.settings.SETTINGS"), 0);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv2.setOnClickListener(MainActivity.this);
        tv3 = (TextView) findViewById(R.id.tv3);
        tv3.setOnClickListener(MainActivity.this);
        tv4 = (TextView) findViewById(R.id.tv4);
        tv4.setOnClickListener(MainActivity.this);
    }
}
