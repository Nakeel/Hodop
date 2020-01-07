package com.we2dx.hodop.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.we2dx.hodop.HomeActivity
import com.we2dx.hodop.R
import com.we2dx.hodop.ui.activity.TrafficInfoActivity
import com.we2dx.hodop.utils.ApplicationConstants.Companion.TAG
import com.we2dx.hodop.utils.GeoCoder
import java.util.*

class HomeFragment : Fragment(), OnMapReadyCallback {


    private lateinit var homeViewModel: HomeViewModel
    private lateinit var mStartLocation : AppCompatTextView
    private lateinit var mEndLocation : AppCompatTextView
    private lateinit var mGetDestinationRouteButton : AppCompatImageButton
    private lateinit var currentLocation : String
    private var isUserIdle = true
    private lateinit var mMap: GoogleMap
    private lateinit var mAddressFeatureName :String
    private lateinit var mStartLocationLatlng :LatLng
    private lateinit var mStartLocationAddress :String
    private lateinit var mEndLocationLatlng :LatLng
    private lateinit var mEndLocationAddress :String

    private var PLACE_AUTOCOMPLETE_REQUEST_CODE = 1
    private var locationListener: LocationListener? = null
    private var locationManager: LocationManager? = null

    lateinit var mFusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        mStartLocation  = root.findViewById(R.id.select_current_loc_text)
        mEndLocation = root.findViewById(R.id.select_destination_loc_text)
        mGetDestinationRouteButton = root.findViewById(R.id.get_destination_route_button)


        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        homeViewModel.text.observe(this, Observer {
        })
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity as HomeActivity )
        mStartLocation.setOnClickListener {

        }
        mEndLocation.setOnClickListener {
            findPlace("endPoint")
        }
        mGetDestinationRouteButton.setOnClickListener {
            startRouteInfoActivity()
        }
    }
    private fun startRouteInfoActivity() {

        startActivity(TrafficInfoActivity.newIntent(context!!, mGetDestinationRouteButton,mStartLocationLatlng,LatLng(0.0,0.0),mStartLocationAddress,"EndAddress"))
    }


    val PERMISSION_ID = 42
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }


    lateinit var mlocation : Location

    @SuppressLint("MissingPermission")
    fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {

                mFusedLocationClient.lastLocation.addOnCompleteListener(activity as HomeActivity) { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {

                        updateMap(location)
                        mStartLocationLatlng = LatLng(location.latitude,location.longitude)

                        mStartLocation.text = geoCodeAddress(location)
                        mStartLocationAddress = mAddressFeatureName
                        mStartLocation.setTextColor(
                            ContextCompat.getColor(
                                context!!,
                                R.color.black
                            )
                        )
                    }
                }
            } else {
                Toast.makeText(context, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                context!!.startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }


    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context!!)
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback()  {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location = locationResult.lastLocation
            updateMap(mLastLocation)

            mStartLocationLatlng = LatLng(mLastLocation.latitude,mLastLocation.longitude)

            mStartLocation.text = geoCodeAddress(mLastLocation)
            mStartLocationAddress = mAddressFeatureName

            mStartLocation.setTextColor(
                ContextCompat.getColor(
                    context!!,
                    R.color.black
                )
            )

        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager = context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            activity as HomeActivity,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.isBuildingsEnabled = true

        mMap.uiSettings.isMapToolbarEnabled = false
        mMap.uiSettings.isCompassEnabled = false


        getLastLocation()
    }

    fun geoCodeAddress(location: Location?): String {
        //this creates an instance of the geoCoder class
        val geoCoder = GeoCoder()
        val address: String
        if (location != null) {
            //sets and get the address in a string variable
            address = geoCoder.getAddresses(context!!, location.latitude, location.longitude)!!
             mAddressFeatureName = geoCoder.street!!
        } else {
            address ="Your Location"
        }
        return address
    }



    private var AUTOCOMPLETE_REQUEST_CODE = 1
    private fun findPlace(locationType:String) {
        isUserIdle = false
        // Set the fields to specify which types of place data to
        // return after the user has made a selection.
        val fields= mutableListOf(Place.Field.ID,Place.Field.NAME,Place.Field.LAT_LNG)
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(context!!)

        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            when (resultCode) {
                RESULT_OK -> {
                    val place = Autocomplete.getPlaceFromIntent(data!!)
                    Log.i(TAG, "Place: " + place.name + ", " + place.id)
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    // TODO: Handle the error.
                    val status = Autocomplete.getStatusFromIntent(data!!)
                    Log.i(TAG, status.statusMessage!!)
                }
                RESULT_CANCELED -> {
                    // The user canceled the operation.
                }
            }
        }
    }




    /**
     * this handles the displaying of the user current location
     * based on the argument pass from its call
     */
    fun updateMap(location: Location) {

        //get and set the geoCodeAddress in a string
        // variable by invoking the geoCodeAddress()
        val Address = geoCodeAddress(location)


        //checks if the driver is not busy
        val userLocation = LatLng(location.latitude, location.longitude)
        mMap.clear()

        //then animate the map to the user current location
        //also adds a marker to indicate its location
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f))
        mMap.addMarker(MarkerOptions().position(userLocation).title(Address))
        //saveCurrentLocToParse(userLocation);

    }



}