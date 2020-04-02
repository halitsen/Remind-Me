package halit.sen.remindme.utils

import android.content.Context
import android.content.Intent
import com.afollestad.materialdialogs.MaterialDialog
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
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(intent)
}

fun openInfoDialog(context: Context, content: String, title: String) {
    val dialog = MaterialDialog.Builder(context)
        .title(title)
        .content(content)
        .positiveText("OK")
        .onPositive { dialog1, which -> dialog1.dismiss() }
        .show()
    dialog.titleView.textSize = 16f
    dialog.contentView!!.textSize = 14f
}