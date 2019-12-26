package com.we2dx.hodop.utils

import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.graphics.Typeface
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Pattern
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import java.text.SimpleDateFormat
import java.util.*
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageButton
import com.bumptech.glide.Glide
import com.we2dx.hodop.R
import com.we2dx.hodop.ui.login.LoginActivity


class ApplicationUtility {
    companion object {
        fun showSnack(msg: String, view: View, action: String){
            val snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_INDEFINITE)
            snackbar.setAction(action) {
                snackbar.dismiss()
            }
            snackbar.show()
        }

        fun showSnack(msg: String, view: View, action: String, listener: TaskOnComplete, service: String) {
            val snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_INDEFINITE)
            snackbar.setAction(action) {
                if(service == ApplicationConstants.UPDATE_PROF_SUCCESS){
                    listener.onResponseReceived(ApplicationConstants.GOTO_LOGIN)
                    snackbar.dismiss()
                    return@setAction
                }
                listener.onResponseReceived(ApplicationConstants.SNACKBAR_ACTION)
                snackbar.dismiss()
            }
            snackbar.show()
        }


        fun switchView(viewToHide:View,viewToDisplay:View) {
            viewToDisplay.visibility = View.VISIBLE
            viewToHide.visibility = View.GONE
        }

//    fun loadImage(imagePath: String,intoView : ImageView, context: Context) {
//            Glide.with(context)
//                .load(imagePath)
//                .placeholder( R.drawable.male_icon)
//                .into(intoView)
//        }
//
//        fun switchEditIcon(isEdited: Boolean, view: AppCompatImageButton) {
//            if (isEdited){
//                view.setImageResource(R.drawable.ic_done_black_36dp)
//            }else{
//                view.setImageResource(R.drawable.ic_edit_icon)
//            }
//
//        }

        fun showDialog(context: Activity, title: String, msg: String, listener: TaskOnComplete, service: String) {
            val alertDialog = AlertDialog.Builder(context)
            alertDialog.setTitle(title)
            alertDialog.setMessage(msg)
            alertDialog.setPositiveButton(ApplicationConstants.POSITIVE_BUTTON) { _, _ ->
                if(service == ApplicationConstants.NUM_EDITED){
                    listener.onResponseReceived(ApplicationConstants.NUM_EDITED_GO)
                }else {
                    listener.onResponseReceived(ApplicationConstants.SUCCESS_RESP)
                }
            }
            alertDialog.setNegativeButton(ApplicationConstants.NEGATIVE_BUTTON) { _, _ ->
                if(service == ApplicationConstants.NUM_EDITED){
                    listener.onResponseReceived(ApplicationConstants.NUM_EDITED_NOGO)
                }else {
                    listener.onResponseReceived(ApplicationConstants.FAIL_RESP)
                }
            }
            alertDialog.show()
        }

        fun showDialogWithoutService(context: Activity, title: String, msg: String) {
            val alertDialog = AlertDialog.Builder(context)
            alertDialog.setTitle(title)
            alertDialog.setMessage(msg)
            alertDialog.setPositiveButton(ApplicationConstants.POSITIVE_BUTTON) { _, _ ->
                context.finish()
            }

            alertDialog.show()
        }

        fun showToast(context: Context, msg: String) {
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
        }

        fun closeKeyboardFromFragment(activity: Activity, fragment: Fragment) {
            val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

            var view: View? = fragment.view!!.rootView.windowToken as View

            if (view == null) {
                view = fragment.view!!.rootView.windowToken as View
            }

            // hide the keyboard
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }


        fun hideSoftKeyboard(activity: Activity) {
            val inputMethodManager = activity.getSystemService(
                Activity.INPUT_METHOD_SERVICE
            ) as InputMethodManager
            val focussedView = activity.currentFocus
            if (focussedView != null) {
                inputMethodManager.hideSoftInputFromWindow(
                    activity.currentFocus!!.windowToken, 0
                )
            }
        }

        fun regexValidator(pattern: Pattern, value: String): String{
            if(!pattern.matcher(value).matches()){
                return ApplicationConstants.FAIL_RESP
            }
            return ApplicationConstants.SUCCESS_RESP
        }

        fun storeValue(key: String, value: String, context: Activity) {
            val editor = context.getSharedPreferences(ApplicationConstants.MY_PREFS_NAME, MODE_PRIVATE).edit()
            editor.putString(key, value)
            editor.apply()
        }

        fun storeBooleanValue(key: String, value: Boolean, context: Activity) {
            val editor = context.getSharedPreferences(ApplicationConstants.MY_PREFS_NAME, MODE_PRIVATE).edit()
            editor.putBoolean(key, value)
            editor.apply()
        }

        fun readValue(context: Activity, key: String) : String{
            val pref = context.getSharedPreferences(ApplicationConstants.MY_PREFS_NAME, MODE_PRIVATE)
            return pref.getString(key, null) ?: ApplicationConstants.EMPTY
        }

        fun readBooleanValue(context: Activity, key: String) : Boolean{
            val pref = context.getSharedPreferences(ApplicationConstants.MY_PREFS_NAME, MODE_PRIVATE)
            return pref.getBoolean(key, false) ?: ApplicationConstants.FALSE
        }

        //this validates the entered number to see if its a valid country code
        fun isCountryCodeValid(countryCode: String): Boolean {
            return countryCode.contains("+") && countryCode.length <= 4
        }

        //this validates the entered number to see if its a valid mobile number
        fun isPhoneNumberValid(enterPhoneNumber: String): Boolean {
            return enterPhoneNumber.length == 11
        }

        //this is use to format enter number to phone format
        fun convertInputToPhoneFormat(input: String): String {
            val buf = StringBuilder(input.length - 1)
            buf.append(input.substring(0, 0)).append(input.substring(1))

            phoneNumber = buf.toString()
            return buf.toString().replaceFirst("(\\d{3})(\\d{3})(\\d+)".toRegex(), "$1-$2-$3")

        }

        fun convertToPhoneFormat(input: String): String {
            val buf = StringBuilder(input.length - 1)
            buf.append(input.substring(0, 0)).append(input.substring(1))

            return  buf.toString()
        }

         fun getCurrentDateStamp(): String {
            val date = Date()
            val dayOfWeek = SimpleDateFormat("EEEE").format(date)
            val currentDate = SimpleDateFormat("dd/mm/yyyy").format(date)
            return "$dayOfWeek $currentDate"
        }
        fun getCurrentTimeStamp() : String{
            val date = Date()
            return SimpleDateFormat("HH:mm").format(date)
        }

      fun dragView(dragView: View) {
             var dX  : Float = 0f
            var dY : Float = 0f
            var lastAction : Int = 0
            dragView.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(v: View, event: MotionEvent): Boolean {
                    when (event.actionMasked) {
                        MotionEvent.ACTION_DOWN -> {
                            dX = v.x - event.rawX
                            dY = v.y - event.rawY
                            lastAction = MotionEvent.ACTION_DOWN
                        }

                        MotionEvent.ACTION_MOVE -> {
                            v.y = event.rawY + dY
                            v.x = event.rawX + dX
                            lastAction = MotionEvent.ACTION_MOVE
                        }

                        MotionEvent.ACTION_UP -> if (lastAction == MotionEvent.ACTION_DOWN) {
                            Log.i("Draggable", "Image Clicked")
                            v.performClick()
                        }
                        else -> return false
                    }
                    return true
                }


            })
        }


       fun shakeAnimation(context: Context, view: View, anim : Int) {
            val shake = AnimationUtils.loadAnimation(context,anim)
            view.startAnimation(shake)
            Toast.makeText(context, "Wrong Password", Toast.LENGTH_SHORT).show()
        }

        lateinit var phoneNumber :  String


        fun isEmailValid(email: String): Boolean {
            //TODO: Replace this with your own logic
            return email.contains("@")
        }

        fun isPasswordValid(password: String): Boolean {
            //TODO: Replace this with your own logic
//            return password.length > 4
            return true
        }

        fun fontRegular(context: Context): Typeface {
            return Typeface.createFromAsset(context.assets, ApplicationConstants.FONT_REGULAR)
        }

        fun fontBold(context: Context): Typeface {
            return Typeface.createFromAsset(context.assets, ApplicationConstants.FONT_BOLD)
        }

        fun doLogout(context: Activity) {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(context, LoginActivity::class.java)
            intent.putExtra(ApplicationConstants.WELCOME_TYPE, ApplicationConstants.LOGIN)
            startActivity(context, Intent(context,  LoginActivity::class.java), null)
        }

        fun deviceHeight(): Int {
            val displayMetrics = DisplayMetrics()
            return displayMetrics.heightPixels
        }

        fun deviceWidth(): Int {
            val displayMetrics = DisplayMetrics()
            return displayMetrics.widthPixels
        }

//        fun showLoadingDialog(show: Boolean, context: Activity) {
//            val fragmentManager : FragmentManager = (context as FragmentActivity).supportFragmentManager
//            val loadingDialogFragment = LoadingDialogFragment.newInstance()
//            loadingDialogFragment.isCancelable = false
//            if (show) {
//                loadingDialogFragment.show(fragmentManager, "LoadingFragment")
//            } else {
//                val prev = fragmentManager.findFragmentByTag("LoadingFragment")
//                if (prev != null) {
//                    val df = prev as DialogFragment
//                    df!!.dismiss()
//                }
//            }
//
//        }
    }
}