package ru.cardiacare.cardiacare.idt_ecg;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import ru.cardiacare.cardiacare.R;

//Виджет с кнопками в статус-баре

public class ServiceNotification extends Notification {

    private Context ctx;
    static public NotificationManager mNotificationManager;
    static public Notification notification;

    public ServiceNotification(Context ctx) {
        super();
        this.ctx = ctx;
        String ns = Context.NOTIFICATION_SERVICE;
        mNotificationManager = (NotificationManager) ctx.getSystemService(ns);
        CharSequence tickerText = "ECG connected";
        long when = System.currentTimeMillis();
        Builder builder = new Builder(ctx);
        notification = builder.getNotification();
        notification.when = when;
        notification.tickerText = tickerText;
        notification.icon = R.drawable.ic_launcher;

        RemoteViews contentView = new RemoteViews(ctx.getPackageName(), R.layout.notification_ecg_layout);

        setListeners(contentView);

        notification.contentView = contentView;
        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        mNotificationManager.notify(548853, notification);
        BluetoothFindActivity.ecgService.startForeground(548853, notification);
    }

    public void setListeners(RemoteViews view) {
//        Intent openecgview = new Intent(ctx, NotificationHelperActivity.class);
        Intent openecgview = new Intent(ctx, ECGActivity.class);
        openecgview.putExtra("DO", "openecgview");
        PendingIntent pOpenecgview = PendingIntent.getActivity(ctx, 0, openecgview, 0);
        view.setOnClickPendingIntent(R.id.openecgview, pOpenecgview);

//        Intent stopservice = new Intent(ctx, NotificationHelperActivity.class);
        Intent stopservice = new Intent(ctx, ECGActivity.class);
        stopservice.putExtra("DO", "stopservice");
        PendingIntent pStopservice = PendingIntent.getActivity(ctx, 2, stopservice, 0);
        view.setOnClickPendingIntent(R.id.stopservice, pStopservice);
    }
}