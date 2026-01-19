package com.example.android.whileinuselocation

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class SensorActivity : AppCompatActivity() {

    private lateinit var sensorManager: SensorManager
    private lateinit var sensorListView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sensor)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorListView = findViewById(R.id.sensor_list_view)

        val sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL)
        val sensorNames = sensorList.map { it.name }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, sensorNames)
        sensorListView.adapter = adapter
    }
}
