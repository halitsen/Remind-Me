package halit.sen.remindme.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import halit.sen.remindme.database.ReminderDao
import halit.sen.remindme.database.ReminderData


class MainViewModel (val database: ReminderDao, application: Application) : AndroidViewModel(application){

    private val context = getApplication<Application>().applicationContext

    private var _allReminders: LiveData<List<ReminderData>> = database.getAllReminders()
    val allReminders
        get() = _allReminders

    fun insert(reminderData: ReminderData){
        database.insert(reminderData)
    }
}