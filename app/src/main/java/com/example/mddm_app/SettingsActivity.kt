package com.example.mddm_app

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity

public class SettingsActivity : AppCompatActivity() {
    //public var listener : () -> Unit = {}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setSupportActionBar(findViewById(R.id.toolbar))

        //supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);
    }
/*
    override fun onSupportNavigateUp(): Boolean {
        listener
        onBackPressed()
        return true
    }*/

    public fun getSharedPref() : SharedPreferences {
        //return getSharedPreferences("APP", Context.MODE_PRIVATE)
        return  PreferenceManager.getDefaultSharedPreferences(applicationContext)
    }
}