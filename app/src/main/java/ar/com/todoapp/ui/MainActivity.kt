package ar.com.todoapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ar.com.todoapp.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}