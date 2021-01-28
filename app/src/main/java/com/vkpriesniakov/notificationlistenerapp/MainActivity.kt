package com.vkpriesniakov.notificationlistenerapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.vkpriesniakov.notificationlistenerapp.ui.main.NotificationMainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, NotificationMainFragment.newInstance())
                    .commitNow()
        }
    }
}