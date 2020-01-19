package halit.sen.remindme.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import halit.sen.remindme.R
import halit.sen.remindme.main.MainActivity

private val NOTIFICATION_ID = 0
private val REQUEST_CODE = 0

fun NotificationManager.sendNotification(messageBody: String, messageTitle:String, applicationContext: Context) {

    val contentIntent = Intent(applicationContext, MainActivity::class.java)
    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )
    val image = BitmapFactory.decodeResource(applicationContext.resources,R.drawable.image)

    val bigPicStyle = NotificationCompat.BigPictureStyle()
        .bigPicture(image)
        .bigLargeIcon(null)

    val bigTextStyle = NotificationCompat.BigTextStyle()
        .bigText(messageBody)

    val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.chanelID)
    )
        .setSmallIcon(R.drawable.ic_alarm_active)
        .setContentTitle(messageTitle)
        .setContentText(messageBody)
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)
        .setStyle(bigTextStyle)
        .setLargeIcon(image)
        .setSound(alarmSound)
        .setVibrate(longArrayOf(1000, 1000))
        .setPriority(NotificationCompat.PRIORITY_HIGH)

    notify(NOTIFICATION_ID, builder.build())
}

fun NotificationManager.cancelNotification(){
    cancelAll()
}

