package com.we2dx.hodop.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder

import com.google.android.gms.maps.model.LatLng

import java.io.IOException
import java.util.Locale

/**
 * Created by NAK-EL on 4/21/2018.
 */

class GeoCoder {
    private var geocoder: Geocoder? = null
    private var addresses: List<Address>? = null
    var address: String? = null
        private set
    var street: String? = null
        private set
    var city: String? = null
        private set
    var state: String? = null
        private set
    var country: String? = null
        private set
    var postalCode: String? = null
        private set
    var knownName: String? = null
        private set

    fun getAddresses(context: Context, latitude: Double, longitude: Double): String? {
        geocoder = Geocoder(context, Locale.getDefault())


        try {
            addresses = geocoder!!.getFromLocation(latitude, longitude, 1)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: IndexOutOfBoundsException) {
            e.cause
        }

        // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        if (addresses != null) {
            // If any additional address line present than only,
            // check with max available address lines by getMaxAddressLineIndex()
            try {
                address = addresses!![0].getAddressLine(0)
                val city = addresses!![0].locality
                street = addresses!![0].featureName
                val state = addresses!![0].adminArea
                val country = addresses!![0].countryName
                val postalCode = addresses!![0].postalCode
                val knownName = addresses!![0].featureName


            } catch (e: IndexOutOfBoundsException) {
                e.cause
            }


        } else {
            address = "Your Location"
        }
        return address
    }

    constructor() {}

    constructor(context: Context, latitude: Double, longitude: Double) {
        geocoder = Geocoder(context, Locale.getDefault())


        try {
            addresses = geocoder!!.getFromLocation(latitude, longitude, 1)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: IndexOutOfBoundsException) {
            e.cause
        }

        // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        if (addresses != null) {
            // If any additional address line present than only,
            // check with max available address lines by getMaxAddressLineIndex()
            try {
                address = addresses!![0].getAddressLine(0)
                city = addresses!![0].locality
                street = addresses!![0].featureName
                state = addresses!![0].adminArea
                country = addresses!![0].countryName
                postalCode = addresses!![0].postalCode
                knownName = addresses!![0].featureName


            } catch (e: IndexOutOfBoundsException) {
                e.cause
            }


        } else {
            address = "Your Location"
            city = "Your Location"
            state = "Your Location"
            street = "Your Location"
            country = "Your Location"
            postalCode = "Your Location"
            knownName = "Your Location"
        }
    }
}
