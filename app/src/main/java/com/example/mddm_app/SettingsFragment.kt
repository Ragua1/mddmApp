package com.example.mddm_app

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText

class SettingsFragment : Fragment() {
    var editText_city: EditText? = null
    var editText_country: EditText? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_close).setOnClickListener {
            activity?.let{
                savePlace()
                val intent = Intent (it, MainActivity::class.java)
                it.startActivity(intent)
            }
        }

        editText_city = view.findViewById<EditText>(R.id.editText_city)
        editText_country = view.findViewById<EditText>(R.id.editText_country)
    }

     fun savePlace() {
        var lat = "${editText_city?.text},${editText_country?.text}"

        val sharedPref: SharedPreferences = (activity as SettingsActivity)?.getSharedPreferences()
        sharedPref.edit().putString("destination", lat.trim())
        //sharedPref.edit().apply()
        val res = sharedPref.edit().commit()

        lat = "---"
        lat = sharedPref.getString("destination", "") ?: ""
        ;
    }

    override fun onStart() {
        super.onStart()

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        var lat: String? = sharedPref.getString("destination", "")
        if (lat.isNullOrEmpty()){
            lat = "Zlin,cz"
        }
        val latSplit = lat.split(',')
        editText_city?.setText(latSplit[0])
        editText_country?.setText(latSplit[1])
    }
}