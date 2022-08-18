package com.swastik.weatherapp


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*

@Suppress("DEPRECATION")
class SplashScrean : AppCompatActivity() {

    private lateinit var mfusedprovider : FusedLocationProviderClient
    private var myrequstcode = 1010
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screan)

        mfusedprovider = LocationServices.getFusedLocationProviderClient(this)
        getlastlocation()

    }

    @SuppressLint("MissingPermission")
    private fun getlastlocation() {
        if (checkpermission()){
            if (chechgps()){

                mfusedprovider.lastLocation.addOnCompleteListener{
                    task->
                    var location:Location?= task.result
                    if (location == null){
                        newlocation()
                    }
                    else{
                        Handler(Looper.getMainLooper()).postDelayed({
                            val intent = Intent(this,MainActivity::class.java)
                            intent.putExtra("lat",location.latitude.toString())
                            intent.putExtra("long",location.longitude.toString())
                            startActivity(intent)
                            finish()
                        },3000)
                    }
                }
            }
            else{

                Toast.makeText(this,"Please turn on your GPS",Toast.LENGTH_SHORT).show()
            }
        }
        else{

            Requstnewlocation()
        }
    }

    @SuppressLint("MissingPermission")
    private fun newlocation() {
        val locationRequest=LocationRequest()
        locationRequest.priority=LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval=0
        locationRequest.fastestInterval=0
        locationRequest.numUpdates=1
        mfusedprovider=LocationServices.getFusedLocationProviderClient(this)
        mfusedprovider.requestLocationUpdates(locationRequest,locationcallback, Looper.myLooper())
    }

    private val locationcallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            var lastlocation : Location= p0.lastLocation
        }
    }

    private fun Requstnewlocation() {

        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION),myrequstcode)

    }

    private fun chechgps(): Boolean {

        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

    }

    private fun checkpermission(): Boolean {
        if (
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            ||
                    ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        )
        {
            return true
        }
        return false
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == myrequstcode){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getlastlocation()
            }
        }
    }
}