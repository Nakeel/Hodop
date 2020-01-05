package com.we2dx.hodop.ui.home

import android.Manifest
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
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
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

class HomeFragment : Fragment(), OnMapReadyCallback {


    private lateinit var homeViewModel: HomeViewModel
    private lateinit var mStartLocation : AppCompatTextView
    private lateinit var mEndLocation : AppCompatTextView
    private lateinit var mGetDestinationRouteButton : AppCompatImageButton
    private lateinit var currentLocation : String
    private var isUserIdle = true
    private lateinit var mMap: GoogleMap

    private var PLACE_AUTOCOMPLETE_REQUEST_CODE = 1
    private var locationListener: LocationListener? = null
    private var locationManager: LocationManager? = null

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


        homeViewModel.text.observe(this, Observer {
        })
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mStartLocation.setOnClickListener {

        }
        mGetDestinationRouteButton.setOnClickListener {
            startRouteInfoActivity()
        }
    }
    private fun startRouteInfoActivity() {
        val intent =  Intent(context!!,TrafficInfoActivity::class.java)
        startActivity(intent)
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


        if (Build.VERSION.SDK_INT < 23 || ContextCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) === PackageManager.PERMISSION_GRANTED
        ) {
            //this will listen for changes of thw user location
            locationListner()
        } else {
            if (ContextCompat.checkSelfPermission(
                    context!!,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) !== PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    context!!,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) !== PackageManager.PERMISSION_GRANTED
            ) {

                //                there seems to be not permission granted
                //                ask for permission
                ActivityCompat.requestPermissions(
                    activity as HomeActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    1
                )
            } else {
                //then permission is already granted so perform the below

                //this makes call to the sdkCheck for the permission
                sdkCheck()
                locationListner()

            }
        }
        //initialization of the geoCode Adrress of the user current locstion
        currentLocation = geoCodeAddress(currentLocation())
    }

    fun geoCodeAddress(location: Location?): String {
        //this creates an instance of the geoCoder class
        val geoCoder = GeoCoder()
        return if (location != null) {
            //sets and get the address in a string variable
            geoCoder.getAddresses(context!!, location.latitude, location.longitude)!!
        } else {
            "Your Location"
        }
    }


    /**
     * this check if the sdk versoin is lower than 23
     * and if the user location permission has been granted
     */
    private fun sdkCheck() {
        if (Build.VERSION.SDK_INT < 23) {

            if (ActivityCompat.checkSelfPermission(
                    context!!,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) !== PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context!!,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) !== PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

                ActivityCompat.requestPermissions(
                    activity as HomeActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    1
                )

            } else {
                locationManager!!.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    0,
                    0f,
                    locationListener
                )
            }

        } else {

            if (ContextCompat.checkSelfPermission(
                    context!!,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) !== PackageManager.PERMISSION_GRANTED
            ) {

                ActivityCompat.requestPermissions(
                    activity as HomeActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    1
                )

            } else {

                locationManager!!.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    0,
                    0f,
                    locationListener!!
                )

                val lastKnownLocation =
                    locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)

                if (lastKnownLocation != null) {

                    updateMap(lastKnownLocation)

                }


            }
        }
    }


    private fun locationListner() {

        // this will allow us to get the location of the user
        locationManager = context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        //        Thread listnerThread = new Thread(new Runnable() {
        //            @Override
        //            public void run() {
        //

        //this will listen for changes of thw user location
        locationListener = object : LocationListener {

            //called when the phone location(i.e user) is move
            override fun onLocationChanged(location: Location?) {

                if (isUserIdle){
                    //if the location is not null t avoid errors
                    if (location != null) {

                        //calls the updateMap() to display the user location on the map
                        updateMap(location)

                        mStartLocation.text = geoCodeAddress(location)
                        mStartLocation.setTextColor(
                            ContextCompat.getColor(
                                context!!,
                                R.color.black
                            )
                        )

                    }


                } else {
                    mStartLocation.text = geoCodeAddress(location)
                    mStartLocation.setTextColor(
                        ContextCompat.getColor(
                            context!!,
                            R.color.black
                        )
                    )
                    updateMap(currentLocation()!!)

                }
            }

            //called when the location service is enabled or disabled
            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {

            }

            //called when the location service is enabled by the user
            override fun onProviderEnabled(provider: String) {

            }


            //called when the location service is disabled by the user
            override fun onProviderDisabled(provider: String) {

            }
        }


    }


    /**
     * this method handles the getting of the user currentLocation
     */
    private fun currentLocation(): Location? {
        val location: Location?
        //this checks if the user has permission to user the location or nt

        if (Build.VERSION.SDK_INT < 23 || ContextCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) === PackageManager.PERMISSION_GRANTED
        ) {
            locationManager!!.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0,
                0f,
                locationListener!!
            )
            location = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            return location
        }


        if (ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) !== PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) !== PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            //if permission not granted request permission, then save the current location
            ActivityCompat.requestPermissions(
                activity as HomeActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
            locationManager!!.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0,
                0f,
                locationListener!!
            )
            location = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            return location
        } else {
            //else just get the current location and return
            locationManager!!.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0,
                0f,
                locationListener!!
            )
            location = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            return location
        }
        //        if(location==null){
        //            currentLocation();
        //        }


    }

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1) {

            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ContextCompat.checkSelfPermission(
                        context!!,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) === PackageManager.PERMISSION_GRANTED
                ) {

                    locationManager!!.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        0,
                        0f,
                        locationListener!!
                    )

                    if (locationManager != null) {

                        locationManager!!.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            0,
                            0f,
                            locationListener!!
                        )

                        val lastKnownLocation =
                            locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)

                        if (lastKnownLocation != null) {
                            updateMap(lastKnownLocation)
                        } else {
                            Toast.makeText(
                                context!!,
                                "Error get Current Location\n Try again ",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        Log.i("LocationManagerError", "Error")
                    }

                }
            }
        }
    }

    private var AUTOCOMPLETE_REQUEST_CODE = 1
    private fun findPlace(locationType:String) {
        isUserIdle = false
        // Set the fields to specify which types of place data to
        // return after the user has made a selection.
        val fields = mutableListOf(Place.Field.ID, Place.Field.NAME)

        // Start the autocomplete intent.
        val intent = Autocomplete.IntentBuilder(
            AutocompleteActivityMode.FULLSCREEN, fields
        ).build(context!!)

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