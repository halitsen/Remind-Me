package halit.sen.remindme.main

import android.content.Context
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import halit.sen.remindme.R
import halit.sen.remindme.database.ReminderDao
import halit.sen.remindme.database.ReminderData
import halit.sen.remindme.database.ReminderDatabase
import halit.sen.remindme.getTimeFromMilis

class ReminderListAdapter(mainActivity: MainActivity) : RecyclerView.Adapter<ReminderListAdapter.ViewHolder>(){


    var main:MainActivity

    init {
        main = mainActivity
    }

    var data = listOf<ReminderData>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.reminder_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data.get(position)
        holder.bind(item, main)
        holder.itemView.setOnClickListener {
          main.onEditItem(item)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val res = itemView.context.resources
        val title: TextView = itemView.findViewById(R.id.reminder_title)
        val description: TextView = itemView.findViewById(R.id.reminder_text)
        val date : TextView = itemView.findViewById(R.id.reminder_date)
        val isActiveIcon: ImageView = itemView.findViewById(R.id.reminder_is_active_icon)
        val isBirthdayImage : ImageView = itemView.findViewById(R.id.is_birthday_image)
        val garbageIcon : ImageView = itemView.findViewById(R.id.garbage_icon)
        fun bind(item: ReminderData, main:MainActivity) {

            title.text = item.reminderTitle
            description.text = item.reminderContent
            date.text = getTimeFromMilis(item.notifyTime)
            if(item.isBirthday){
                isBirthdayImage.setImageResource(R.drawable.ic_birthday_cake)
            }
            if(item.isActive){
                isActiveIcon.setImageResource(R.drawable.ic_alarm_active)
            }else{
                isActiveIcon.setImageResource(R.drawable.ic_alarm_passive)
            }
            isActiveIcon.setOnClickListener {
                if(item.isActive){
                    isActiveIcon.setImageResource(R.drawable.ic_alarm_passive)
                    item.isActive = false
                    main.onItemUpdate(item)
                }else{
                    isActiveIcon.setImageResource(R.drawable.ic_alarm_active)
                    item.isActive = true
                    main.onItemUpdate(item)
                }
            }
            garbageIcon.setOnClickListener {
                main.onItemDelete(item)
            }
        }
    }
}