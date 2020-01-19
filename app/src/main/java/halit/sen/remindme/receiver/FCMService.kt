package halit.sen.remindme.receiver

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FCMService : FirebaseMessagingService(){


    override fun onMessageReceived(remoteMessage: RemoteMessage) {



    }

    override fun onNewToken(token: String) {
        Log.i("New Token : ",token)

    }
}