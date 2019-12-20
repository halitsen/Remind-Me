package halit.sen.remindme.createReminder

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import halit.sen.remindme.database.ReminderDao
import halit.sen.remindme.database.ReminderData

class CreateReminderViewModel(val reminder: ReminderData, val database: ReminderDao, application: Application) : AndroidViewModel(application){

    private val context = getApplication<Application>().applicationContext



    fun insertReminder(reminder: ReminderData){
        database.insert(reminder)
        //todo bu işlemleri uiScope ta yap..
    }

    fun deleteDeminder(reminder: ReminderData){
        database.deleteReminder(reminder)
    }

    fun updateReminder(reminder: ReminderData){
        database.update(reminder)
    }


}