package halit.sen.remindme

import android.content.Context
import android.content.Intent
import halit.sen.remindme.main.MainActivity
import java.text.SimpleDateFormat
import java.util.*

fun getTimeFromMilis(milis: Long): String {
    val df = SimpleDateFormat("dd:MM:yy:HH:mm:ss", Locale.getDefault())
    val cal = Calendar.getInstance()
    cal.setTimeInMillis(milis)
    return df.format(cal.getTime())

}

fun restart(context: Context) {

    val intent = Intent(context, MainActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    context.startActivity(intent)
}