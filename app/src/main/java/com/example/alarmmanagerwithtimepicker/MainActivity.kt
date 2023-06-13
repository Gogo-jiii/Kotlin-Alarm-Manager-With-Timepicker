package com.example.alarmmanagerwithtimepicker

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.alarmmanagerwithtimepicker.databinding.ActivityMainBinding
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var alarmManager: AlarmManager? = null
    private var intent: Intent? = null
    private lateinit var oneTimeAlarmIntent : PendingIntent

    private val ALARM_TYPE = "ALARM_TYPE"
    private val ALARM_TYPE_ONE_TIME = "ALARM_TYPE_ONE_TIME"
    private val ALARM_DESCRIPTION = "ALARM_DESCRIPTION"
    private var timePickerDialog: TimePickerDialog? = null
    private val currentTime: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        intent = Intent(this, MyReceiver::class.java)

        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        oneTimeAlarmIntent = PendingIntent.getBroadcast(
            this,
            100,
            intent!!,
            PendingIntent.FLAG_IMMUTABLE
        )

        binding.editText.setOnClickListener {
            it.requestFocus()
            openTimepickerDialog()
        }

        binding.btnStart.setOnClickListener {
            intent!!.putExtra(ALARM_TYPE, ALARM_TYPE_ONE_TIME)
            intent!!.putExtra(ALARM_DESCRIPTION, "Timepicker alarm")

            alarmManager!!.setAndAllowWhileIdle(AlarmManager.RTC, currentTime.timeInMillis,
                oneTimeAlarmIntent)
            Toast.makeText(this, "Alarm set.", Toast.LENGTH_SHORT).show()
        }

        binding.btnStop.setOnClickListener {
            alarmManager!!.cancel(oneTimeAlarmIntent)
            Toast.makeText(this, "Alarm cancelled.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openTimepickerDialog() {
        val hour = currentTime.get(Calendar.HOUR_OF_DAY)
        val minute = currentTime.get(Calendar.MINUTE)
        timePickerDialog = TimePickerDialog(this@MainActivity,
            { _, hourOfDay, minuteOfHour ->
                currentTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                currentTime.set(Calendar.MINUTE, minuteOfHour)
                currentTime.set(Calendar.SECOND, 0)
                currentTime.set(Calendar.MILLISECOND, 0)
                var hour = "" + hourOfDay
                var minute = "" + minuteOfHour
                if (hourOfDay < 10) {
                    hour = "0$hour"
                }
                if (minuteOfHour < 10) {
                    minute = "0$minute"
                }
                val time = "$hour:$minute"
                binding.editText.setText(time)
            }, hour, minute, false
        )
        timePickerDialog!!.setTitle("Select Time")
        timePickerDialog!!.show()
    }
}