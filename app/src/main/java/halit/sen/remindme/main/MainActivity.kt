package halit.sen.remindme.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import halit.sen.remindme.R
import halit.sen.remindme.database.ReminderData
import halit.sen.remindme.database.ReminderDatabase
import halit.sen.remindme.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.text.text = getString(R.string.app_name)

        val application = requireNotNull(this).application
        val dataSource = ReminderDatabase.getInstance(application).reminderDao
        val viewModelFactory = MainViewModelFactory(dataSource, application)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)

        val reminder = ReminderData()
        reminder.reminderTitle = "title1"

        viewModel.insert(reminder)

        viewModel.allReminders.observe(this, Observer {

            Log.i("All Reminders: ", it.size.toString())
            Toast.makeText(this, it.get(0).reminderTitle, Toast.LENGTH_SHORT).show()

        })
    }
}
