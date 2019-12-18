package halit.sen.remindme

import java.text.SimpleDateFormat
import java.util.*

fun getTimeFromMilis(milis: Long): String {
    val df = SimpleDateFormat("dd:MM:yy:HH:mm:ss", Locale.getDefault())
    val cal = Calendar.getInstance()
    cal.setTimeInMillis(milis)
    return df.format(cal.getTime())

}