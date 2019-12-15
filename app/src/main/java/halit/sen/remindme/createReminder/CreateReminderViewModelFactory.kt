package halit.sen.remindme.createReminder

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import halit.sen.remindme.database.ReminderDao
import halit.sen.remindme.database.ReminderData

class CreateReminderViewModelFactory (val reminder: ReminderData, val database: ReminderDao, val application: Application): ViewModelProvider.Factory{

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateReminderViewModel::class.java)) {
            return CreateReminderViewModel(reminder,database, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}