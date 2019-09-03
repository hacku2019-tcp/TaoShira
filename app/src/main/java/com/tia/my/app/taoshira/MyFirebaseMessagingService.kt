package com.tia.my.app.taoshira

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.util.LogPrinter
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.timerTask
import kotlin.math.log

class MyFirebaseMessagingService : FirebaseMessagingService(){
    override fun onNewToken(p0: String?) {
        super.onNewToken(p0)

        println("new token")
        println(p0)

        val intent = Intent(this, com.tia.my.app.taoshira.RegisterTokenActivity::class.java)
        startActivity(intent)
    }

    override fun onMessageReceived(p0: RemoteMessage?) {
        super.onMessageReceived(p0)

        println("received message")

        println(p0?.notification?.body)

        this.sendNotification(p0)
    }

    fun sendNotification(remoteMessage: RemoteMessage?) {


        val callIntent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:01234567890")
        }
        val call119Intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:117")
        }

        val pendingCallIntent = PendingIntent.getActivity(this, 0, callIntent, 0)
        val pendingCall119Intent = PendingIntent.getActivity(this, 0, call119Intent, 0)


        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= 26) {

            val name = "taoshira"
            val id = "taoshira"
            val notifiDescription = "詳細"

            if (notificationManager.getNotificationChannel(id) == null) {
                val mChannel =
                    NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH)
                mChannel.apply {
                    description = notifiDescription
                }
                notificationManager.createNotificationChannel(mChannel)
            }

            val notification = NotificationCompat.Builder(this, id).apply {
                setSmallIcon(R.drawable.ic_launcher_background)
                setContentTitle("登録者の転倒を検知しました")
                setContentText("タップすると登録者に電話をかけます")
                setContentIntent(pendingCallIntent)
                addAction(R.drawable.ic_launcher_background, "救急", pendingCall119Intent)
                setAutoCancel(false)
            }.build()

            notification.flags = Notification.FLAG_NO_CLEAR

            notificationManager.notify(99, notification)
        } else {
            val notification = NotificationCompat.Builder(this, "taushira").run{
                setContentTitle("登録者の転倒を検知しました")
                setContentText("タップすると登録者に電話をかけます")
                setSmallIcon(android.R.drawable.sym_def_app_icon)
                setContentIntent(pendingCallIntent)
                addAction(R.drawable.ic_launcher_background, "救急", pendingCall119Intent)
                setAutoCancel(false)
                build()
            }

            notification.flags = Notification.FLAG_NO_CLEAR

            notificationManager.notify(99, notification)
        }
    }
}
