package com.example.practica03_notificaciones_firebase.models

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import java.util.Calendar

class Alarms {
    companion object {
        fun repetitiveAlarm(pendingIntent: PendingIntent, context: Context) {

            val alarmMgr = (context.applicationContext.getSystemService(Context.ALARM_SERVICE) as? AlarmManager)!!


            val calendar: Calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, 9, 2, 0, 52)
            }

            alarmMgr.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                60000,
                pendingIntent
            )

        }
    }
}