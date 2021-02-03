package com.example.mddm_app

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import java.io.File

class SettingsFragment : Fragment() {
    var editText_city: EditText? = null
    var editText_country: EditText? = null
    val fileName = "dest.cfg"
    val spKey = "destination"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //(activity as SettingsActivity).listener = { savePlace() }

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

        val sharedPref: SharedPreferences = (activity as SettingsActivity)?.getSharedPref()
        sharedPref.edit().putString(spKey, lat.trim())
        //sharedPref.edit().apply()
        val res = sharedPref.edit().commit()

         context?.openFileOutput(fileName, Context.MODE_PRIVATE).use { output ->
             output?.write(lat.toByteArray())
         }


        lat = "---"
        lat = sharedPref.getString(spKey, "") ?: ""
        ;

        context?.openFileInput(fileName).use { stream ->
            lat = stream?.bufferedReader().use {
                it?.readText() ?: ""
            }
            Log.d("TAG", "LOADED: $lat")
        }

        ;
    }

    override fun onStart() {
        super.onStart()

        val sharedPref = (activity as SettingsActivity)?.getSharedPref()
        var lat: String? = sharedPref.getString(spKey, "")
        if (lat.isNullOrEmpty()){
            var file = File(fileName)
            if (file.exists()){
                context?.openFileInput(fileName).use { stream ->
                    lat = stream?.bufferedReader().use {
                        it?.readText() ?: ""
                    }
                    Log.d("TAG", "LOADED: $lat")
                }
            }

            if (lat.isNullOrEmpty()){
                lat = "Zlin,cz"
            }
        }
        val latSplit = lat!!.split(',')
        editText_city?.setText(latSplit[0])
        editText_country?.setText(latSplit[1])
    }
}