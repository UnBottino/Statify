package com.example.statify.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.example.statify.Util.SpotifyFunctions;

public class AlarmReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        SpotifyFunctions.getSpotifySongs(context);
    }
}

