package halit.sen.remindme.createReminder

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import halit.sen.remindme.database.ReminderData
import halit.sen.remindme.database.ReminderDatabase
import halit.sen.remindme.databinding.ActivityCreateReminderBinding
import java.text.SimpleDateFormat
import java.util.*
import android.app.TimePickerDialog
import android.util.Log
import halit.sen.remindme.R
import halit.sen.remindme.restart
import kotlinx.android.synthetic.main.activity_create_reminder.view.*
import kotlin.time.ExperimentalTime
import kotlin.time.milliseconds


class CreateReminderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateReminderBinding
    private lateinit var viewModel: CreateReminderViewModel
    var reminder: ReminderData = ReminderData()
    var cal = Calendar.getInstance()
    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            halit.sen.remindme.R.layout.activity_create_reminder
        )
        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            reminder = bundle.getSerializable("reminder") as ReminderData
            binding.createReminderText.text = "Update Reminder"
        }
        if (reminder.reminderId != 0L) {
            //todo note it te description yok ise ekle den gelmiş var ise update den gelmiş. burada da id 0 mı değil mi ye göre gidecez..
            Toast.makeText(
                this,
                "Reminder Id from update: " + reminder.reminderId,
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(this, "Reminder Id from add: " + reminder.reminderId, Toast.LENGTH_SHORT)
                .show()
        }

        val application = requireNotNull(this).application
        val datasource = ReminderDatabase.getInstance(application).reminderDao
        val viewModelFactory = CreateReminderViewModelFactory(reminder, datasource, application)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(CreateReminderViewModel::class.java)
        binding.setLifecycleOwner(this)
        binding.createReminderViewModel = viewModel
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(
                    Calendar.DAY_OF_MONTH, dayOfMonth
                )
                val df = SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault())
                viewModel.setDayText(df.format(cal.time), cal.timeInMillis)
                Log.i("Milisecond : ",cal.timeInMillis.toString())
            }
        // edit note dan gelince uı i databinding ile set et. Boş ise boş set edilecek zaten. Add ile edit arasındaki farkı ayır..

        binding.backIcon.setOnClickListener {
            finish()
        }
        binding.createReminderText.setOnClickListener {
            if(reminder.reminderId > 0){
                viewModel.updateReminder(binding.reminderDescription.text.toString())
            }else{
                viewModel.insertReminder(binding.reminderDescription.text.toString())
            }
            restart(this)
        }
        binding.dateDayLayout.setOnClickListener {
            openDatePicker()
        }

        binding.dateTimeLayout.setOnClickListener {
            openHourDialogDialog()
        }
        binding.isBirthdaySwitch.setOnClickListener {
            viewModel.isBirthdayClicked()
        }

        binding.isActiveLayout.setOnClickListener {
            viewModel.isActiveClicked()
        }
        viewModel.isBirthDay.observe(this,androidx.lifecycle.Observer {
            binding.isBirthdaySwitch.isSelected = it
        })
        viewModel.isActive.observe(this,androidx.lifecycle.Observer {
            if(it){
                binding.isActiveImage.setImageResource(R.drawable.ic_alarm_active)
            }else{
                binding.isActiveImage.setImageResource(R.drawable.ic_alarm_passive)
            }
        })
    }

    fun openDatePicker() {
        DatePickerDialog(
            this, dateSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    fun openHourDialogDialog(){
        val dialog = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            val notifyTime =
                (if (hourOfDay < 10) "0$hourOfDay" else hourOfDay).toString() + ":" + if (minute < 10) "0$minute" else minute
            val hourInMilis = hourOfDay*60000*60 + minute*60000 as Long
            viewModel.setHourText(notifyTime, hourInMilis)
            Log.i("Hour Milis: ", (hourOfDay*60000*60).toString())
            Log.i("Minute Milis: ", (minute*60000).toString())
        }, 12, 30, true
        )
        dialog.show()
    }
}