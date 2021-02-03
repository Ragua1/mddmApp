package com.example.mddm_app

import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import org.json.JSONObject
import java.io.File
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    var CITY: String = "Zlin,cz"
    val API: String = "3fb82464b729b1e70524ff5ceb18e04e" // Use API key
    val fileName = "dest.cfg"
    val spKey = "destination"
    var jsonObj: JSONObject? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
    }


    override fun onStart() {
        super.onStart()
    }

    public fun updateWeather(detail: Boolean = false) {

        val sharedPref: SharedPreferences = getSharedPref()
        var lat: String? = sharedPref.getString(spKey, "")
        if (lat.isNullOrEmpty()){
            var file = File(fileName)
            if (file.exists()){
                applicationContext?.openFileInput(fileName).use { stream ->
                    lat = stream?.bufferedReader().use {
                        it?.readText() ?: ""
                    }
                    Log.d("TAG", "LOADED: $lat")
                }
            }
        }
        if (!lat.isNullOrEmpty()){
            CITY = lat!!
        }

        weatherTask(detail).execute()
    }

    public fun getSharedPref() : SharedPreferences {
        //return this.getSharedPreferences("APP", Context.MODE_PRIVATE)
        return  PreferenceManager.getDefaultSharedPreferences(applicationContext)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
                finish()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    inner class weatherTask(detail: Boolean = false) : AsyncTask<String, Void, String>() {
        private val isDetail: Boolean = detail

        override fun onPreExecute() {
            super.onPreExecute()
            // show ProgressBar
            if (!isDetail){
                findViewById<ProgressBar>(R.id.loader).visibility = View.VISIBLE
                findViewById<RelativeLayout>(R.id.mainContainer1).visibility = View.GONE
                findViewById<TextView>(R.id.errorText1).visibility = View.GONE
            }
        }

        override fun doInBackground(vararg params: String?): String? {
            return try {
                val urlPath = "https://api.openweathermap.org/data/2.5/weather?q=$CITY&units=metric&APPID=$API"

                URL(urlPath).readText(Charsets.UTF_8)
            } catch (e: Exception){
                Log.e("MainActivity", e.message)
                null
            }
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                // extract JSON
                val jsonObj = JSONObject(result)
                val main = jsonObj.getJSONObject("main")
                val sys = jsonObj.getJSONObject("sys")
                val wind = jsonObj.getJSONObject("wind")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)

                val updatedAt:Long = jsonObj.getLong("dt")
                val updatedAtText = "Updated at: ${SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.GERMANY).format(Date(updatedAt * 1000))}"
                val temp = "${main.getString("temp")} °C"
                val tempMin = "Min: ${main.getString("temp_min")} °C"
                val tempMax = "Max: ${main.getString("temp_max")} °C"
                val pressure = main.getString("pressure")
                val humidity = main.getString("humidity")

                val sunrise:Long = sys.getLong("sunrise")
                val sunset:Long = sys.getLong("sunset")
                val windSpeed = wind.getString("speed")
                val weatherDescription = weather.getString("description")

                val address = "${jsonObj.getString("name")}, ${sys.getString("country")}"

                // set data
                if (!isDetail){
                    findViewById<TextView>(R.id.address1).text = address
                    findViewById<TextView>(R.id.updated_at1).text = updatedAtText
                    findViewById<TextView>(R.id.status1).text = weatherDescription.capitalize()
                    findViewById<TextView>(R.id.temp1).text = temp

                    // hide loader
                    findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                    findViewById<RelativeLayout>(R.id.mainContainer1).visibility = View.VISIBLE
                } else {
                    findViewById<TextView>(R.id.address2).text = address
                    findViewById<TextView>(R.id.updated_at2).text = updatedAtText
                    findViewById<TextView>(R.id.status2).text = weatherDescription.capitalize()
                    findViewById<TextView>(R.id.temp2).text = temp
                    findViewById<TextView>(R.id.temp_min2).text = tempMin
                    findViewById<TextView>(R.id.temp_max2).text = tempMax
                    findViewById<TextView>(R.id.sunrise2).text = SimpleDateFormat("HH:mm", Locale.GERMANY).format(Date(sunrise * 1000))
                    findViewById<TextView>(R.id.sunset2).text = SimpleDateFormat("HH:mm", Locale.GERMANY).format(Date(sunset * 1000))
                    findViewById<TextView>(R.id.wind2).text = windSpeed
                    findViewById<TextView>(R.id.pressure2).text = pressure
                    findViewById<TextView>(R.id.humidity2).text = humidity
                }
            } catch (e: Exception) {
                Log.e("MainActivity", e.message)
                if (!isDetail){
                    findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                    findViewById<TextView>(R.id.errorText1).visibility = View.VISIBLE
                }
            }

        }
    }
}