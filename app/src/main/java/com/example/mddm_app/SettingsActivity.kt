package com.example.mddm_app

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import java.util.prefs.Preferences

public class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
    }


    public fun getSharedPreferences() : SharedPreferences {
        //return getSharedPreferences("APP", Context.MODE_PRIVATE)
        return  PreferenceManager.getDefaultSharedPreferences(applicationContext)
    }
}