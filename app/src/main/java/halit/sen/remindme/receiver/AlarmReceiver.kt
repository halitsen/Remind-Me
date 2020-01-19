package halit.sen.remindme.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import halit.sen.remindme.utils.sendNotification

class AlarmReceiver :BroadcastReceiver(){

    override fun onReceive(context: Context, intent: Intent?) {

        val text = intent!!.getStringExtra("text")

        val notificationManager = ContextCompat.getSystemService(context, NotificationManager::class.java) as NotificationManager

        notificationManager.sendNotification(text!!, context)
    }
}