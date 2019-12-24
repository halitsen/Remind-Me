package halit.sen.remindme.createReminder

import android.app.Application
import android.app.TimePickerDialog
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import halit.sen.remindme.database.ReminderDao
import halit.sen.remindme.database.ReminderData

class CreateReminderViewModel(val reminder: ReminderData, val database: ReminderDao, application: Application) : AndroidViewModel(application){

    private val context = getApplication<Application>().applicationContext

    private val _content = MutableLiveData<String>()
    val content: LiveData<String>
        get() = _content

    private val _reminderTimeAsDay = MutableLiveData<String>()
    val reminderTimeAsDay: LiveData<String>
        get() = _reminderTimeAsDay

    private val _reminderTimeAsHour = MutableLiveData<String>()
    val reminderTimeAsHour : LiveData<String>
        get() = _reminderTimeAsHour

    private val _isBirthday = MutableLiveData<Boolean>()
    val isBirthDay : LiveData<Boolean>
    get() = _isBirthday

    private val _isActive = MutableLiveData<Boolean>()
    val isActive : LiveData<Boolean>
        get() = _isActive

    private var _notifyTimeMilis = MutableLiveData<Long>()
    val notifyTimeMilis : LiveData<Long>
    get() = _notifyTimeMilis

    init {
        //todo buradaki time ları convert et. insert ve update ederken bu textleri alırken de convert et
        _content.value = reminder.reminderContent
        _reminderTimeAsDay.value = reminder.notifyTimeAsDay.toString()
        _reminderTimeAsHour.value = reminder.notifyTimeAsHour.toString()
        _isBirthday.value = reminder.isBirthday
        _isActive.value = reminder.isActive
        _notifyTimeMilis.value = reminder.notifyTimeMilis
    }

    fun insertReminder(description: String){
        reminder.reminderContent = description
        reminder.isActive = _isActive.value!!
        reminder.isBirthday = _isBirthday.value!!
        reminder.notifyTimeAsDay = _reminderTimeAsDay.value.toString()
        reminder.notifyTimeAsHour = _reminderTimeAsHour.value.toString()
        reminder.notifyTimeMilis = _notifyTimeMilis.value!!
        database.insert(reminder)
        //todo bu işlemleri uiScope ta yap..
    }

    fun deleteDeminder(reminder: ReminderData){
        database.deleteReminder(reminder)
    }

    fun updateReminder(description: String){
        reminder.reminderContent = description
        reminder.isActive = _isActive.value!!
        reminder.isBirthday = _isBirthday.value!!
        reminder.notifyTimeAsDay = _reminderTimeAsDay.value.toString()
        reminder.notifyTimeAsHour = _reminderTimeAsHour.value.toString()
        reminder.notifyTimeMilis = _notifyTimeMilis.value!!
        database.update(reminder)
    }

    fun setHourText(text: String, hourMilis: Long){
        _reminderTimeAsHour.value = text
        _notifyTimeMilis.value =  (_notifyTimeMilis.value)?.plus(hourMilis)
    }
    fun setDayText(text:String, dayTimeMilis:Long){
        _notifyTimeMilis.value =  (_notifyTimeMilis.value)?.plus(dayTimeMilis)
        //todo yukarıda zamanı milisecond a çeviremezsem activity den burada milisecond gönderecem.
        _reminderTimeAsDay.value = text
    }

    fun isBirthdayClicked(){
        _isBirthday.value = _isBirthday.value != true
    }

    fun isActiveClicked(){
        _isActive.value = _isActive.value != true
    }

    fun getNotifyTimeMiliseconds(){
        //todo milisecond al alarm için
    }
}
