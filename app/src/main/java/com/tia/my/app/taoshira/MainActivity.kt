package com.tia.my.app.taoshira

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.iid.FirebaseInstanceId
import com.google.android.gms.tasks.OnCompleteListener
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener(OnCompleteListener { task ->

            val token = task.result?.token

            println("token")

            println(token.toString())

        })

        val callIntent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:01234567890")
        }
        val call119Intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:117")
        }
        val cleanNotifiIntent = Intent()

        val pendingCallIntent = PendingIntent.getActivity(this, 0, callIntent, 0)
        val pendingCall119Intent = PendingIntent.getActivity(this, 0, call119Intent, 0)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        this.button.setOnClickListener(View.OnClickListener { view ->


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
                    setContentTitle("taoshira!")
                    setContentText("おばあちゃんが")
                    setContentIntent(pendingCallIntent)
                    addAction(R.drawable.ic_launcher_background, "救急", pendingCall119Intent)
                    setAutoCancel(false)
                }.build()

                notification.flags = Notification.FLAG_NO_CLEAR

                notificationManager.notify(99, notification)
            }
            else {

                val notification = NotificationCompat.Builder(this, "taushira").run{
                    setContentTitle("バージョン低い")
                    setContentText("おばあちゃん")
                    setSmallIcon(android.R.drawable.sym_def_app_icon)
                    setContentIntent(pendingCallIntent)
                    addAction(R.drawable.ic_launcher_background, "救急", pendingCall119Intent)
                    setAutoCancel(false)
                    build()
                }

                notification.flags = Notification.FLAG_NO_CLEAR

                notificationManager.notify(99, notification)
            }
        })

        this.button2.setOnClickListener(View.OnClickListener{view ->
            notificationManager.cancelAll()
        })

        this.button4.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, com.tia.my.app.taoshira.RegisterTokenActivity::class.java)
            startActivity(intent)
        })

    }
}
