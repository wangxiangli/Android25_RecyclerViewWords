package com.bush.android25_recyclerviewwords.helper;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.bush.android25_recyclerviewwords.R;

/**
 * Created by 嘉华盛世 on 2016-07-29.
 */
public class AlertHelper {
    private Context context;
    private static AlertListener listener;
    public interface AlertListener{
        void callBack();
    }

    public static void getAlertResult(Context context,String title){
        AlertDialog.Builder builder=new AlertDialog.Builder(context)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle(title)
                .setNegativeButton("取消", null)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       listener.callBack();
                    }
                });
        builder.show();
    }
}
