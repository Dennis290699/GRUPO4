package com.example.electronicazytron

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.electronicazytron.controlador.AppNavigation
import com.example.electronicazytron.ui.theme.ElectronicaZytronTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ElectronicaZytronTheme {
                AppNavigation()
                }
            }
        }
    }
