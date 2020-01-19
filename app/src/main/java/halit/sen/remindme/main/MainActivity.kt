package halit.sen.remindme.main

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Color.RED
import android.media.SoundPool
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import halit.sen.remindme.R
import halit.sen.remindme.database.ReminderData
import halit.sen.remindme.database.ReminderDatabase
import halit.sen.remindme.databinding.ActivityMainBinding
import halit.sen.remindme.receiver.AlarmReceiver
import halit.sen.remindme.utils.cancelNotification
import halit.sen.remindme.utils.sendNotification

class MainActivity : AppCompatActivity(),RecyclerViewClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val application = requireNotNull(this).application
        val dataSource = ReminderDatabase.getInstance(application).reminderDao
        val viewModelFactory = MainViewModelFactory(dataSource, application)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        binding.lifecycleOwner = this
        binding.reminderList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        val adapter = ReminderListAdapter(this)
        binding.reminderList.adapter = adapter
        val notificationManager = ContextCompat.getSystemService(this, NotificationManager::class.java) as NotificationManager
        notificationManager.cancelNotification()

        binding.fab.setOnClickListener {
            viewModel.openCreateReminder(this)
        }
        viewModel.allReminders.observe(this, Observer {
            it?.let {
                adapter.data = it
                binding.emptyReminderPaceholderText.visibility = View.GONE
                val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val intent = Intent(this, AlarmReceiver::class.java)
                if(it.size>0){
                    for(i in 0 until it.size){
                        if(it.get(i).notifyTimeMilis > System.currentTimeMillis() && it.get(i).isActive){
                            intent.putExtra("text", it.get(i).reminderContent) //data to pass
                            intent.putExtra("title",it.get(i).reminderTitle)
                            val pendingIntent = PendingIntent.getBroadcast(this,i,intent,PendingIntent.FLAG_CANCEL_CURRENT)
                            alarmManager.setExact(AlarmManager.RTC_WAKEUP,it.get(i).notifyTimeMilis ,pendingIntent)
                            Log.i("alarmMilisaniye :", it.get(0).notifyTimeMilis.toString())
                        }
                    }
                }
            }
            if(it.size < 1){
                binding.emptyReminderPaceholderText.visibility = View.VISIBLE
            }
        })
        createChanel(getString(R.string.chanelID), getString(R.string.chanelName))
        createChanel(getString(R.string.fcmChannelId), getString(R.string.fcmChannelName))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.share){
           viewModel.onShareClicked()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemDelete(item: ReminderData) {
        viewModel.deleteDeminder(item)
    }

    override fun onItemUpdate(item: ReminderData) {
        viewModel.updateReminder(item)
    }

    override fun onEditItem(item: ReminderData) {
        viewModel.openEditReminder(this,item)
    }

    private fun createChanel(channelId: String, channelName: String){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            val notificationChannel = NotificationChannel(channelId,channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Reminder Time"
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager!!.createNotificationChannel(notificationChannel)

        }
    }
}
