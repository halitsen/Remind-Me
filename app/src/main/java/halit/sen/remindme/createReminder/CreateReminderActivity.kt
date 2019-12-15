package halit.sen.remindme.createReminder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import halit.sen.remindme.R
import halit.sen.remindme.database.ReminderData
import halit.sen.remindme.database.ReminderDatabase
import halit.sen.remindme.databinding.ActivityCreateReminderBinding

class CreateReminderActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCreateReminderBinding

    private lateinit var viewModel: CreateReminderViewModel
    private lateinit var database: ReminderDatabase
    var reminder: ReminderData = ReminderData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_create_reminder)

        val application = requireNotNull(this).application
        val datasource = ReminderDatabase.getInstance(application).reminderDao
        val viewModelFactory = CreateReminderViewModelFactory(reminder, datasource, application)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CreateReminderViewModel::class.java)
    }
}
