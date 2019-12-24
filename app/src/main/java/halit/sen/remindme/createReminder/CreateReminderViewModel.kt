package halit.sen.remindme.createReminder

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import halit.sen.remindme.database.ReminderDao
import halit.sen.remindme.database.ReminderData
import halit.sen.remindme.restart
import kotlinx.coroutines.*

class CreateReminderViewModel(
    val reminder: ReminderData,
    val database: ReminderDao,
    application: Application
) : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext

    private val _content = MutableLiveData<String>()
    val content: LiveData<String>
        get() = _content

    private val _reminderTimeAsDay = MutableLiveData<String>()
    val reminderTimeAsDay: LiveData<String>
        get() = _reminderTimeAsDay

    private val _reminderTimeAsHour = MutableLiveData<String>()
    val reminderTimeAsHour: LiveData<String>
        get() = _reminderTimeAsHour

    private val _isBirthday = MutableLiveData<Boolean>()
    val isBirthDay: LiveData<Boolean>
        get() = _isBirthday

    private val _isActive = MutableLiveData<Boolean>()
    val isActive: LiveData<Boolean>
        get() = _isActive

    private var _notifyTimeMilis = MutableLiveData<Long>()
    val notifyTimeMilis: LiveData<Long>
        get() = _notifyTimeMilis

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    init {
        _content.value = reminder.reminderContent
        _reminderTimeAsDay.value = reminder.notifyTimeAsDay.toString()
        _reminderTimeAsHour.value = reminder.notifyTimeAsHour.toString()
        _isBirthday.value = reminder.isBirthday
        _isActive.value = reminder.isActive
        _notifyTimeMilis.value = reminder.notifyTimeMilis
    }

    fun insertReminder(description: String) {
        uiScope.launch {
            reminder.reminderContent = description
            reminder.isActive = _isActive.value!!
            reminder.isBirthday = _isBirthday.value!!
            reminder.notifyTimeAsDay = _reminderTimeAsDay.value.toString()
            reminder.notifyTimeAsHour = _reminderTimeAsHour.value.toString()
            reminder.notifyTimeMilis = _notifyTimeMilis.value!!
            withContext(Dispatchers.IO) {
                database.insert(reminder)
            }
        }
        restart(context)
    }

    fun updateReminder(description: String) {

        uiScope.launch {
            reminder.reminderContent = description
            reminder.isActive = _isActive.value!!
            reminder.isBirthday = _isBirthday.value!!
            reminder.notifyTimeAsDay = _reminderTimeAsDay.value.toString()
            reminder.notifyTimeAsHour = _reminderTimeAsHour.value.toString()
            reminder.notifyTimeMilis = _notifyTimeMilis.value!!
            withContext(Dispatchers.IO) {
                database.update(reminder)
            }
        }
        restart(context)
    }

    fun setHourText(text: String, hourMilis: Int) {
        _reminderTimeAsHour.value = text
        _notifyTimeMilis.value = (_notifyTimeMilis.value)?.plus(hourMilis)
    }

    fun setDayText(text: String, dayTimeMilis: Long) {
        _notifyTimeMilis.value = (_notifyTimeMilis.value)?.plus(dayTimeMilis)
        _reminderTimeAsDay.value = text
    }

    fun isBirthdayClicked() {
        _isBirthday.value = _isBirthday.value != true
    }

    fun isActiveClicked() {
        _isActive.value = _isActive.value != true
    }
}
