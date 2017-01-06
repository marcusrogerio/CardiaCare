package ru.cardiacare.cardiacare.idt_ecg;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.RelativeLayout;

import ru.cardiacare.cardiacare.MainActivity;
import ru.cardiacare.cardiacare.R;

public class ECGActivity extends AppCompatActivity {

    ECGView myView;
    static public Handler handler;
    private final int IDD_THREE_BUTTONS = 0;
    public static Context mContext;

    private static final String TAG = "ECGActivity";
    private static final float TWO_INCHES = 2f;
//    boolean bound = false;
//    ServiceConnection sConn;
//    Intent intent;
//    ECGService ecgService;

    public final static String FILE_NAME = "ITS ALIVE!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate ECGActivity Activity");
        super.onCreate(savedInstanceState);
        mContext = this;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_ecg);

        RelativeLayout v = (RelativeLayout) findViewById(R.id.ecg_view);
        myView = new ECGView(this, setViewWidthInMillimeter());

        assert v != null;
        v.addView(myView);

//        intent = new Intent(this, ECGService.class);
//        sConn = new ServiceConnection() {
//
//            public void onServiceConnected(ComponentName name, IBinder binder) {
//                Log.d("QQQ", "MainActivity onServiceConnected");
//                ecgService = ((ECGService.MyBinder) binder).getService();
//                bound = true;
//            }
//
//            public void onServiceDisconnected(ComponentName name) {
//                Log.d("QQQ", "MainActivity onServiceDisconnected");
//                bound = false;
//            }
//        };
//
//
//        startService(intent);
//        bindService(intent, sConn, 0);

        handler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case 1:
//                        Log.i("QQQ", "HANDLER WORK");
                        int[] readBuf = (int[]) msg.obj;
                        myView.getECGData(readBuf);
                }
            }

            ;
        };
    }

    private double setViewWidthInMillimeter() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float mXDpi = metrics.xdpi;
        double ppmm = mXDpi / 25.4f;
        return ppmm;
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy ECGActivity Activity");
        myView.pulseTimer.cancel();
        myView.myTimer.cancel();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        showDialog(IDD_THREE_BUTTONS);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case IDD_THREE_BUTTONS:

                final String[] mDialogActions = {"*Функция недоступна* Продолжить работу с монитором в фоновом режиме", "Завершить работу с монитором", "Остаться на экране \"ЭКГ\""};

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Вы покидаете экран \"ЭКГ\"\n ");

                builder.setItems(mDialogActions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) {
                            dialog.cancel();
                            //TODO: Закрывать приложение. finish()?
                        }
                        if (item == 1) {
                            dialog.cancel();
//                            BluetoothFindActivity.doStop();
                            ECGService.doStop();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                            if (!bound) return;
//                            unbindService(sConn);
//                            stopService(intent);
//                            bound = false;
                        }
                        if (item == 2) {
                            dialog.cancel();
                        }
                    }
                });
                builder.setCancelable(false);
                return builder.create();

            default:
                return null;
        }
    }
}
