package halit.sen.remindme.main

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import halit.sen.remindme.createReminder.CreateReminderActivity
import halit.sen.remindme.database.ReminderDao
import halit.sen.remindme.database.ReminderData


class MainViewModel (val database: ReminderDao, application: Application) : AndroidViewModel(application){

    private val context = getApplication<Application>().applicationContext

    private var _allReminders: LiveData<List<ReminderData>> = database.getAllReminders()
    val allReminders
        get() = _allReminders

    fun insertReminder(reminder: ReminderData){
        database.insert(reminder)
    }

    fun deleteDeminder(reminder: ReminderData){
        database.deleteReminder(reminder)
    }

    fun updateReminder(reminder: ReminderData){
        database.update(reminder)
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
}