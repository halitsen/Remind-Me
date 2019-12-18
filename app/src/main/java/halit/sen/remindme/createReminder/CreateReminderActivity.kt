package halit.sen.remindme.createReminder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import halit.sen.remindme.R
import halit.sen.remindme.database.ReminderData
import halit.sen.remindme.database.ReminderDatabase
import halit.sen.remindme.databinding.ActivityCreateReminderBinding

class CreateReminderActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCreateReminderBinding

    private lateinit var viewModel: CreateReminderViewModel
    var reminder: ReminderData = ReminderData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_create_reminder)
        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            reminder = bundle.getSerializable("reminder") as ReminderData
        }
        if(reminder.reminderId != 0L){
            //todo note it te description yok ise ekle den gelmiş var ise update den gelmiş. burada da id 0 mı değil mi ye göre gidecez..
            Toast.makeText(this,"Reminder Id from update: "+reminder.reminderId, Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this,"Reminder Id from add: "+reminder.reminderId, Toast.LENGTH_SHORT).show()
        }

        val application = requireNotNull(this).application
        val datasource = ReminderDatabase.getInstance(application).reminderDao
        val viewModelFactory = CreateReminderViewModelFactory(reminder, datasource, application)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CreateReminderViewModel::class.java)
        binding.setLifecycleOwner(this)
        binding.createReminderViewModel = viewModel
        // edit note dan gelince uı i databinding ile set et. Boş ise boş set edilecek zaten. Add ile edit arasındaki farkı ayır..
    }
}
