package halit.sen.remindme.main

import halit.sen.remindme.database.ReminderData

interface RecyclerViewClickListener {

        fun onItemDelete(item: ReminderData)
        fun onItemUpdate(item: ReminderData)
        fun onEditItem(item: ReminderData)

}