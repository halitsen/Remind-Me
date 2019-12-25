package halit.sen.remindme.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "reminder_table")
class ReminderData : Serializable{

    @PrimaryKey(autoGenerate = true)
    var reminderId: Long = 0L

    @ColumnInfo(name = "title")
    var reminderTitle:String = ""

    @ColumnInfo(name = "content")
    var reminderContent:String = ""

    @ColumnInfo(name = "created_date")
    var creaTedDate:Long = System.currentTimeMillis()

    @ColumnInfo(name = "is_birthday")
    var isBirthday: Boolean = false

    @ColumnInfo(name = "notify_time_as_day")
    var notifyTimeAsDay:String = ""

    @ColumnInfo(name = "notify_time_as_hour")
    var notifyTimeAsHour:String = ""

    @ColumnInfo(name = "notify_time_milis")
    var notifyTimeMilis:Long = 0

    @ColumnInfo(name = "is_active")
    var isActive: Boolean = false

}