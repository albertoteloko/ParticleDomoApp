package com.at.hal9000.domain.service.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.at.hal9000.app.MainAppService;

public class MainServiceStartReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, MainAppService.class);
        context.startService(service);
    }
}