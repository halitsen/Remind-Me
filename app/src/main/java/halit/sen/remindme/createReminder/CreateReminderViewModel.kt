package halit.sen.remindme.createReminder

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import halit.sen.remindme.database.ReminderDao
import halit.sen.remindme.database.ReminderData

class CreateReminderViewModel(val reminder: ReminderData, val database: ReminderDao, application: Application) : AndroidViewModel(application){

    private val context = getApplication<Application>().applicationContext



}