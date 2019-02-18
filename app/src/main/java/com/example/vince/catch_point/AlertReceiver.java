package com.example.vince.catch_point;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;

public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Vaudra true par défaut si on ne trouve pas l'extra booléen dont la clé est LocationManager.KEY_PROXIMITY_ENTERING
        boolean entrer = intent.getBooleanExtra(LocationManager.KEY_PROXIMITY_ENTERING, true);
    }
}

