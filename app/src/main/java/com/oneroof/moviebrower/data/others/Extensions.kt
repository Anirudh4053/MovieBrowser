package com.oneroof.moviebrower.data.others

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

const val BASE_URL = "https://api.themoviedb.org"
const val API = "2a4c1a201c7c4ffc801192a2c7e6096d"
const val MOVIE_IMAGE_PATH = "https://image.tmdb.org/t/p/w500"
const val ERROR = "Something went wrong, please try again later"
fun Context.showToast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, text, duration).show()
}
fun hideKeyboard(activity: Activity) {
    val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    var view = activity.currentFocus
    if (view == null) {
        view = View(activity)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}
fun View.show(){
    visibility = View.VISIBLE
}
fun View.hide(){
    visibility = View.GONE
}
fun Context.globalDpToPx(dp: Int): Int {
    val r = resources
    return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), r.displayMetrics))
}
fun Window.disableUserInteraction(){
    this.setFlags(
        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
}

fun Window.enableUserInteraction(){
    this.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
}

fun customAlert(activity: Activity, title:String = "Error", msg:String = "", finish:Boolean = true){
    val builder = AlertDialog.Builder(activity)
    builder.setTitle(title)
    builder.setMessage(msg)
        .setCancelable(false)
        .setPositiveButton("Yes") { dialog, _ ->
            dialog.dismiss()
            if(finish)
                activity.onBackPressed()
        }
        /*.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }*/
    val alert: AlertDialog = builder.create()
    alert.show()
}
fun <T> Call<T>.makeRequest(onSuccess: (T) -> Unit, onFailure: (Throwable) -> Unit) {
    this.enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            if (response.isSuccessful){
                response.body()?.let { onSuccess(it) }
            }else{
                onFailure(Throwable("No record found"))
            }
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            when(t){
                is UnknownHostException -> onFailure(Throwable("Please check your internet connection."))
                is SocketTimeoutException -> onFailure(Throwable("Very poor connection. Please check your internet."))
                else -> onFailure(Throwable("Something went wrong"))
            }
            onFailure(Throwable("Please check your internet connection."))
        }
    })
}