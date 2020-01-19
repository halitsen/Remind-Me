package halit.sen.remindme.createReminder

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import halit.sen.remindme.utils.openInfoDialog


class CreateReminderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateReminderBinding
    private lateinit var viewModel: CreateReminderViewModel
    var reminder: ReminderData = ReminderData()
    var cal = Calendar.getInstance()
    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_reminder)
        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            reminder = bundle.getSerializable("reminder") as ReminderData
            binding.createReminderText.text = "Update Reminder"
            // todo nereden gelirse gelsin buradaki gün ve saati sıfırla.
            // todo milisaniye alındığı için tarih güncellemesi yapınca üstüne ekliyor sürekli.
        }
        reminder.notifyTimeMilis = 0
        reminder.notifyTimeAsHour = ""
        reminder.notifyTimeAsDay = ""
        binding.isBirthdaySwitch.isChecked = reminder.isBirthday
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

                cal.set(Calendar.HOUR_OF_DAY,0)
                cal.set(Calendar.MINUTE,0)
                cal.set(Calendar.SECOND,0)
                cal.set(Calendar.MILLISECOND,0)

             //   val hour = cl.get(Calendar.HOUR_OF_DAY)
             //   val minute = cl.get(Calendar.MINUTE)

              //  val mil = (hour * 60000 * 60) + (minute * 60000)

                viewModel.setDayText(df.format(cal.time), cal.timeInMillis)
                Log.i("Milisecond : ", cal.timeInMillis.toString())
            }
        binding.backIcon.setOnClickListener {
            finish()
        }
        binding.createReminderText.setOnClickListener {
            val title:String

            if(binding.reminderTitle.text.toString() != ""){
                title = binding.reminderTitle.text.toString()
            }else{
                title = getString(R.string.app_name)
            }

            if (binding.reminderDescription.text.toString() == "") {
                openInfoDialog(
                    this,
                    "Write your reminder description",
                    "Remind Me"
                )
                return@setOnClickListener
            } else if (binding.dateDayText.text.toString() == "") {
                openInfoDialog(
                    this,
                    "Please specify reminder day",
                    "Remind Me"
                )
                return@setOnClickListener
            } else if (binding.dateTimeText.text.toString() == "") {
                openInfoDialog(
                    this,
                    "Please specify reminder time",
                    "Remind Me"
                )
                return@setOnClickListener
            } else {
                if (reminder.reminderId > 0) {
                    viewModel.updateReminder(
                        binding.reminderDescription.text.toString(),title)
                } else {
                    viewModel.insertReminder(
                        binding.reminderDescription.text.toString(),title)
                }
            }
        }
        binding.dateDayLayout.setOnClickListener {
            openDatePicker()
        }

        binding.dateTimeLayout.setOnClickListener {
            openHourDialogDialog()
        }
        binding.isBirthdaySwitch.setOnCheckedChangeListener { compoundButton, isChecked ->
            viewModel.isBirthdayClicked()
        }
        binding.isActiveLayout.setOnClickListener {
            viewModel.isActiveClicked()
        }
        viewModel.isActive.observe(this, androidx.lifecycle.Observer {
            if (it) {
                binding.isActiveImage.setImageResource(R.drawable.ic_alarm_active)
            } else {
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

    fun openHourDialogDialog() {
        val dialog = TimePickerDialog(
            this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                val notifyTime =
                    (if (hourOfDay < 10) "0$hourOfDay" else hourOfDay).toString() + ":" + if (minute < 10) "0$minute" else minute
                val hourInMilis = hourOfDay * 60000 * 60 + minute * 60000

                var cal = Calendar.getInstance()

                cal.set(Calendar.YEAR,0)
                cal.set(Calendar.MONTH,0)
                cal.set(Calendar.DAY_OF_MONTH,0)
                cal.set(Calendar.HOUR_OF_DAY,hourOfDay)
                cal.set(Calendar.MINUTE,minute)
                cal.set(Calendar.SECOND,0)

                viewModel.setHourText(notifyTime, hourInMilis)
                Log.i("Hour Milis: ", (hourOfDay * 60000 * 60).toString())
                Log.i("Minute Milis: ", (minute * 60000).toString())
            }, 12, 30, true
        )
        dialog.show()
    }
}