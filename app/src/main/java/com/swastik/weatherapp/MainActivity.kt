package com.swastik.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var lat = intent.getStringExtra("lat")
        var long = intent.getStringExtra("long")

        jsonrequst(lat,long)

    }

    private fun jsonrequst(lat: String?, long: String?) {

        val queue = Volley.newRequestQueue(this)
        val API_KEY = "49cf7742fabec74afd6d48f1b3ce89e2"
        val url = "https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${long}&appid=${API_KEY}"

        val JsonRequest = JsonObjectRequest(
            Request.Method.GET, url,null,
            Response.Listener {  response->
                setvalue(response)
            },
            Response.ErrorListener { Toast.makeText(this,"ERROR",Toast.LENGTH_SHORT).show() })

        queue.add(JsonRequest)
    }

    private fun setvalue(response: JSONObject) {

        City_name.text = response.getString("name")
        Lat.text = response.getJSONObject("coord").getString("lat")
        Long.text = response.getJSONObject("coord").getString("lon")
        Humadity.text = response.getJSONObject("main").getString("humidity")
        Pressure.text = response.getJSONObject("main").getString("pressure")
        Clouds.text = response.getJSONArray("weather").getJSONObject(0).getString("main")
        var temp = response.getJSONObject("main").getString("temp")
        temp = (((temp).toFloat()- 273.15)).toInt().toString()
        Degre.text= temp+"°C"

    }
}