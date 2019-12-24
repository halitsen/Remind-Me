package halit.sen.remindme.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import halit.sen.remindme.R
import halit.sen.remindme.database.ReminderData
import halit.sen.remindme.database.ReminderDatabase
import halit.sen.remindme.databinding.ActivityMainBinding
import halit.sen.remindme.openInfoDialog

class MainActivity : AppCompatActivity(),RecyclerViewClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val application = requireNotNull(this).application
        val dataSource = ReminderDatabase.getInstance(application).reminderDao
        val viewModelFactory = MainViewModelFactory(dataSource, application)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        binding.setLifecycleOwner(this)
        binding.reminderList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        val adapter = ReminderListAdapter(this)
        binding.reminderList.adapter = adapter
        binding.fab.setOnClickListener {
            viewModel.openCreateReminder(this)
        }
        viewModel.allReminders.observe(this, Observer {
            it?.let {
                adapter.data = it
                binding.emptyReminderPaceholderText.visibility = View.GONE
            }
            if(it.size < 1){
                binding.emptyReminderPaceholderText.visibility = View.VISIBLE
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

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
}
