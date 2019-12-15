package halit.sen.remindme.database

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface ReminderDao {

    @Insert
    fun insert(reminderData: ReminderData)

    @Update
    fun update(reminderData: ReminderData)

    @Query("SELECT * from reminder_table WHERE reminderId = :key")
    fun get(key: Long): LiveData<ReminderData>

    @Query("SELECT * FROM reminder_table ORDER BY reminderId DESC")
    fun getAllReminders(): LiveData<List<ReminderData>>

    @Delete
    fun deleteReminder(reminderData: ReminderData)

    @Query("DELETE FROM reminder_table")
    fun clear()
}