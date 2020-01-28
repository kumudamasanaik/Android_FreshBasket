package com.freshbasket.customer.screens.location

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Location
import android.location.LocationManager
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.AppCompatTextView
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import com.freshbasket.customer.MyApplication
import com.freshbasket.customer.R
import com.freshbasket.customer.api.NetworkStatus
import com.freshbasket.customer.constants.Constants
import com.freshbasket.customer.screens.adddeliveryaddress.DeliveryAddressActivity
import com.freshbasket.customer.screens.home.HomeActivity
import com.freshbasket.customer.util.CommonUtils
import com.freshbasket.customer.util.Validation
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment
import com.google.android.gms.location.places.ui.PlaceSelectionListener
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng

class LocationActivity : AppCompatActivity(), OnMapReadyCallback, View.OnClickListener, PlaceSelectionListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {


    private lateinit var source: String
    lateinit var mContext: Context
    private var mLocationRequest: LocationRequest? = null
    /*MAP*/
    private var service: LocationManager? = null
    private var enabled: Boolean? = null
    var locationButton: View? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private var mLastLocation: Location? = null
    private var REQUEST_LOCATION_CODE = 101
    lateinit var mapFragment: SupportMapFragment
    private lateinit var mMap: GoogleMap
    lateinit var resutText: AppCompatTextView
    lateinit var submit_ok: AppCompatButton
    private var center: LatLng? = null
    internal var mCurentAddress = ""
    internal var mapAddress: Address? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var sourceType: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_location)
        mContext = this@LocationActivity
        if (intent != null) {
            source = intent.getStringExtra(Constants.SOURCE)
            sourceType = intent.getStringExtra(Constants.SOURCE_TYPE) ?: ""

        }
        mapDataInitilaise()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

    }

    private fun mapDataInitilaise() {

        service = this.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
        enabled = service!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        resutText = this.findViewById(R.id.dragg_result)
        submit_ok = findViewById(R.id.submit_ok)
        submit_ok.setOnClickListener(this)

        val autocompleteFragment = fragmentManager.findFragmentById(R.id.place_autocomplete_fragment) as PlaceAutocompleteFragment

        autocompleteFragment.setOnPlaceSelectedListener(this)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isMyLocationButtonEnabled = true

        locationButton = (findViewById<View>(Integer.parseInt("1")).parent as View).findViewById(Integer.parseInt("2"))
        locationButton!!.visibility = View.GONE
        val rlp = locationButton!!.layoutParams as RelativeLayout.LayoutParams
        rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0)
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0)

        rlp.setMargins(0, 180, 0, 0)
        with(mMap) {
            uiSettings.isZoomControlsEnabled = true
            mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        }

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
            ) {
                buildGoogleApiClient()
                mMap.isMyLocationEnabled = true
            } else {
                checkLocationPermission()
            }
        } else {
            buildGoogleApiClient()
            mMap.isMyLocationEnabled = true
        }
        if (CommonUtils.getCurrentLocation() != null) {
            mapAddress = CommonUtils.getCurrentLocation()
            val latLng = LatLng(mapAddress!!.latitude, mapAddress!!.longitude)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))


        }

        initCameraIdle()
    }

    @Synchronized
    fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
        mGoogleApiClient!!.connect()
    }


    override fun onConnected(p0: Bundle?) {
        mLocationRequest = LocationRequest()
        mLocationRequest!!.interval = 1000
        mLocationRequest!!.fastestInterval = 1000
        mLocationRequest!!.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY

        if (!enabled!!) {
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        }

        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //  LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient!!, mLocationRequest!!, this)
            fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            val latLng = LatLng(location.latitude, location.longitude)

                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f))
                        }
                    }
        }
    }

    override fun onLocationChanged(location: Location?) {
        mLastLocation = location

        val latLng = LatLng(location!!.latitude, location.longitude)

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f))
    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.submit_ok -> {
                if (source.isNotEmpty() && source.contentEquals(Constants.HOME_SCREEN)) {
                    val intent = Intent(MyApplication.context, HomeActivity::class.java)
                    intent.apply {
                        putExtra(Constants.SOURCE, Constants.SOURCE_LOCATION_ACTIVITY)
                        CommonUtils.saveMyAddress(mapAddress)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    val intent = Intent(MyApplication.context, DeliveryAddressActivity::class.java)
                    intent.apply {
                        putExtra(Constants.ADDRESS_LOC, mapAddress)
                        if (sourceType == Constants.ORDER_TYPE)
                            putExtra(Constants.SOURCE_TYPE, Constants.ORDER_TYPE)
                        putExtra(Constants.SOURCE, Constants.SOURCE_LOCATION_ACTIVITY)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }


    override fun onPlaceSelected(place: Place?) {
        if (Validation.isValidObject(place) && Validation.isValidObject(place!!.latLng))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.latLng, 16f))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place!!.latLng, 12.0f))
    }

    override fun onError(p0: Status?) {
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK") { dialog, which ->
                            ActivityCompat.requestPermissions(
                                    this,
                                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                                    REQUEST_LOCATION_CODE
                            )
                        }
                        .create()
                        .show()
            } else ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_LOCATION_CODE
            )
        }
    }

    private fun initCameraIdle() {

        mMap.setOnCameraIdleListener {
            center = mMap.cameraPosition.target
            if (NetworkStatus.isOnline2(this) && center!!.latitude > 0) {
                getCurrentAddress(center!!.latitude, center!!.longitude).execute()

            }
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_LOCATION_CODE -> {

                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient()
                        }
                        mMap.isMyLocationEnabled = true
                    }
                } else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }


    inner class getCurrentAddress(internal var Latitude: Double?, internal var Longitude: Double?) : AsyncTask<Void, Void, String>() {


        override fun onPreExecute() {
            super.onPreExecute()
            resutText.setText(R.string.please_wait)
        }

        override fun doInBackground(vararg voids: Void): String {
            mapAddress = CommonUtils.getCurrentLocationAddress(mContext, Latitude!!, Longitude!!)
            return if (Validation.isValidObject(mapAddress)) mapAddress!!.getAddressLine(0) else ""

        }

        override fun onPostExecute(s: String) {
            super.onPostExecute(s)
            mCurentAddress = s
            if (Validation.isValidString(mCurentAddress))
                resutText.text = mCurentAddress
            else
                resutText.setText(R.string.Whoops_Something_went_wrong)
        }
    }
}