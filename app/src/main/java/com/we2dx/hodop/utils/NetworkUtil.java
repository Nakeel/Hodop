package com.we2dx.hodop.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtil {
    /**
     * All thanks to stackOverFlow great user for the Network class
     *
     * <p>Utility methods to check the current network connection status.</p>
     *
     * <p>This requires the caller to hold the permission
     * {@link android.Manifest.permission#ACCESS_NETWORK_STATE}.</p>
     */
        /** The absence of a connection type. */
        public static final int TYPE_NONE = -1;

        /** Unknown network class. */
        public static final int NETWORK_CLASS_UNKNOWN = 0;
        /** Class of broadly defined "2G" networks. */
        public static final int NETWORK_CLASS_2_G = 1;
        /** Class of broadly defined "3G" networks. */
        public static final int NETWORK_CLASS_3_G = 2;
        /** Class of broadly defined "4G" networks. */
        public static final int NETWORK_CLASS_4_G = 3;

        /**
         * Returns details about the currently active default data network. When connected, this network
         * is the default route for outgoing connections. You should always check {@link
         * NetworkInfo#isConnected()} before initiating network traffic. This may return {@code null}
         * when there is no default network.
         *
         * @return a {@link NetworkInfo} object for the current default network or {@code null} if no
         * network default network is currently active
         *
         * This method requires the call to hold the permission
         * {@link android.Manifest.permission#ACCESS_NETWORK_STATE}.
         * @see ConnectivityManager#getActiveNetworkInfo()
         */
        public static NetworkInfo getInfo(Context context) {
            return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE))
                    .getActiveNetworkInfo();
        }

        /**
         * Reports the current network type.
         *
         * @return {@link ConnectivityManager#TYPE_MOBILE}, {@link ConnectivityManager#TYPE_WIFI} ,
         * {@link ConnectivityManager#TYPE_WIMAX}, {@link ConnectivityManager#TYPE_ETHERNET}, {@link
         * ConnectivityManager#TYPE_BLUETOOTH}, or other types defined by {@link ConnectivityManager}.
         * If there is no network connection then -1 is returned.
         * @see NetworkInfo#getType()
         */
        public static int getType(Context context) {
            NetworkInfo info = getInfo(context);
            if (info == null || !info.isConnected()) {
                return TYPE_NONE;
            }
            return info.getType();
        }

        /**
         * Return a network-type-specific integer describing the subtype of the network.
         *
         * @return the network subtype
         * @see NetworkInfo#getSubtype()
         */
        public static int getSubType(Context context) {
            NetworkInfo info = getInfo(context);
            if (info == null || !info.isConnected()) {
                return TYPE_NONE;
            }
            return info.getSubtype();
        }

        /** Returns the NETWORK_TYPE_xxxx for current data connection. */
        public static int getNetworkType(Context context) {
            return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE))
                    .getNetworkType();
        }

        /** Check if there is any connectivity */
        public static boolean isConnected(Context context) {
            return getType(context) != TYPE_NONE;
        }

        /** Check if there is any connectivity to a Wifi network */
        public static boolean isWifiConnection(Context context) {
            NetworkInfo info = getInfo(context);
            if (info == null || !info.isConnected()) {
                return false;
            }
            switch (info.getType()) {
                case ConnectivityManager.TYPE_WIFI:
                    return true;
                default:
                    return false;
            }
        }

        /** Check if there is any connectivity to a mobile network */
        public static boolean isMobileConnection(Context context) {
            NetworkInfo info = getInfo(context);
            if (info == null || !info.isConnected()) {
                return false;
            }
            switch (info.getType()) {
                case ConnectivityManager.TYPE_MOBILE:
                    return true;
                default:
                    return false;
            }
        }

        /** Check if the current connection is fast. */
        public static boolean isConnectionFast(Context context) {
            NetworkInfo info = getInfo(context);
            if (info == null || !info.isConnected()) {
                return false;
            }
            switch (info.getType()) {
                case ConnectivityManager.TYPE_WIFI:
                case ConnectivityManager.TYPE_ETHERNET:
                    return true;
                case ConnectivityManager.TYPE_MOBILE:
                    int networkClass = getNetworkClass(getNetworkType(context));
                    switch (networkClass) {
                        case NETWORK_CLASS_UNKNOWN:
                        case NETWORK_CLASS_2_G:
                            return false;
                        case NETWORK_CLASS_3_G:
                        case NETWORK_CLASS_4_G:
                            return true;
                    }
                default:
                    return false;
            }
        }

        private static int getNetworkClassReflect(int networkType)
                throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
            Method getNetworkClass = TelephonyManager.class.getDeclaredMethod("getNetworkClass", int.class);
            if (!getNetworkClass.isAccessible()) {
                getNetworkClass.setAccessible(true);
            }
            return (int) getNetworkClass.invoke(null, networkType);
        }

        /**
         * Return general class of network type, such as "3G" or "4G". In cases where classification is
         * contentious, this method is conservative.
         */
        public static int getNetworkClass(int networkType) {
            try {
                return getNetworkClassReflect(networkType);
            } catch (Exception ignored) {
            }

            switch (networkType) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case 16: // TelephonyManager.NETWORK_TYPE_GSM:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    return NETWORK_CLASS_2_G;
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                case 17: // TelephonyManager.NETWORK_TYPE_TD_SCDMA:
                    return NETWORK_CLASS_3_G;
                case TelephonyManager.NETWORK_TYPE_LTE:
                case 18: // TelephonyManager.NETWORK_TYPE_IWLAN:
                    return NETWORK_CLASS_4_G;
                default:
                    return NETWORK_CLASS_UNKNOWN;
            }
        }

    /**this method checks if there is network or not
     and its returns a boolean result when called*/
    public static boolean isInternetAvailable(Context context){

        //this first checks if network is available or nt
        //with the help of the NetworkUtil class(thanks to good stackOverflow users)
        if(NetworkUtil.isConnected(context)) {

            //then a try and catch function is perform to
            //check if the network is good or not
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                 return urlc.getResponseCode() == 200;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return false;
    }

        NetworkUtil() {
            throw new AssertionError();
        }

    }