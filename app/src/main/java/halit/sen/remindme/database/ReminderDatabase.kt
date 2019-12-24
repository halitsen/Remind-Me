package halit.sen.remindme.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [ReminderData::class], version = 5, exportSchema = false)
abstract class ReminderDatabase: RoomDatabase(){

    abstract val reminderDao: ReminderDao

    companion object{
        @Volatile
        private var INSTANCE: ReminderDatabase? = null

        fun getInstance(context: Context): ReminderDatabase{

            synchronized(this){
                var instance = INSTANCE
                // if instance is null, make a new database

                if(instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ReminderDatabase::class.java,
                        "reminder_database"
                    ).fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}