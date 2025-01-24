package org.hyperskill.phrases

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Calendar
import java.util.Locale



val handler = Handler()
lateinit var notificationManager: NotificationManager
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val reminderTextView: TextView = findViewById(R.id.reminderTextView)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

        val recyclerAdapter = RecyclerAdapter(Phrases.predefinedPhrases)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = recyclerAdapter

        val channelId = "org.hyperskill.phrases"
        val channelName = "AlarmReminder"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager =
                this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val runnable: Runnable = Runnable {
            val intent = Intent(this, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                this,
                1,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            val notification: Notification =
                NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                    .setContentTitle("Your phrase of the day")
                    .setContentText(getRandomPhrase())
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build()
            notificationManager.notify(393939, notification) }

        reminderTextView.setOnClickListener {
            val timePickerDialog = TimePickerDialog(
                this,
                { _, hourOfDay, minute ->
                    reminderTextView.text = String.format(Locale.getDefault(), "Reminder set for %02d:%02d", hourOfDay, minute)
                    handler.postDelayed(runnable , setTriggeredTime(hourOfDay, minute))
                },
                0,
                0,
                true
            )
            timePickerDialog.show()
        }
    }
}

    fun getRandomPhrase(): String {
        return Phrases.predefinedPhrases.random()
    }

//    fun setAlarmTime(context: Context, tvReminder: TextView) {
//        val timePickerDialog = TimePickerDialog(
//            context,
//            { _, hourOfDay, minute ->
//                tvReminder.text = String.format(Locale.getDefault(), "Reminder set for %02d:%02d", hourOfDay, minute)
//                handler.postDelayed(, setTriggeredTime(hourOfDay, minute))
//            },
//            0,
//            0,
//            true
//        )
//        timePickerDialog.show()
//    }


    fun setTriggeredTime(hourOfDay: Int, minute: Int): Long {
        val millisInDay = 86400000L

        val currentTimeInMillis = System.currentTimeMillis()

        val alarmTime = Calendar.getInstance()
        alarmTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
        alarmTime.set(Calendar.MINUTE, minute)
        alarmTime.set(Calendar.SECOND, 0)
        alarmTime.set(Calendar.MILLISECOND, 0)
        val alarmTimeInMillis = alarmTime.timeInMillis
        println(alarmTimeInMillis)
        println(currentTimeInMillis)
        println(alarmTimeInMillis - currentTimeInMillis)
        println(millisInDay - (currentTimeInMillis - alarmTimeInMillis))
        return if (alarmTimeInMillis > currentTimeInMillis) {
            alarmTimeInMillis - currentTimeInMillis
        } else {
            millisInDay - (currentTimeInMillis - alarmTimeInMillis)
        }
    }

    fun setAlarm(context: Context, hourOfDay: Int, minute: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            1,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            setTriggeredTime(hourOfDay, minute),
            86400000L,
            pendingIntent
        )
    }

    fun notify(context: Context) {
        val channelId = "org.hyperskill.phrases"
        val channelName = "AlarmReminder"
        lateinit var notificationManager: NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            1,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification: Notification =
            NotificationCompat.Builder(context, channelId)
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                .setContentTitle("Your phrase of the day")
                .setContentText(getRandomPhrase())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()
        notificationManager.notify(393939, notification)
    }