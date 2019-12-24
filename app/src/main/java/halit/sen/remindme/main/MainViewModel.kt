package halit.sen.remindme.main

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import halit.sen.remindme.R
import halit.sen.remindme.createReminder.CreateReminderActivity
import halit.sen.remindme.database.ReminderDao
import halit.sen.remindme.database.ReminderData
import kotlinx.coroutines.*


class MainViewModel (val database: ReminderDao, application: Application) : AndroidViewModel(application){

    private val context = getApplication<Application>().applicationContext

    private var _allReminders: LiveData<List<ReminderData>> = database.getAllReminders()
    val allReminders
        get() = _allReminders


    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun deleteDeminder(reminder: ReminderData){
        uiScope.launch {
            withContext(Dispatchers.IO){
                database.deleteReminder(reminder)
            }
        }
    }

    fun updateReminder(reminder: ReminderData){
        uiScope.launch {
            withContext(Dispatchers.IO){
                database.update(reminder)
            }
        }
    }

    fun openEditReminder(context:Context, reminder: ReminderData){
        val editRemidnerIntent = Intent(context,CreateReminderActivity::class.java)
        editRemidnerIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        editRemidnerIntent.putExtra("reminder",reminder)
        context.startActivity(editRemidnerIntent)
    }
    fun openCreateReminder(context: Context){
        val createRemidnerIntent = Intent(context,CreateReminderActivity::class.java)
        context.startActivity(createRemidnerIntent)
    }
    fun onShareClicked() {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(
            Intent.EXTRA_TEXT,
            context.getString(R.string.share_app_url)
        )
        intent.type = (context.getString(R.string.text_plain))
        context.startActivity(Intent.createChooser(intent,context.getString(R.string.share)))
    }
}