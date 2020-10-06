package com.oneroof.moviebrower.data.others

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.DisplayMetrics
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
fun ProgressBar.showLoader(){
    this.visibility = View.VISIBLE
}
fun ProgressBar.hideLoader(){
    this.visibility = View.GONE
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
    builder.setTitle("Logout")
    builder.setMessage("Are you sure you want to logout?")
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
                onFailure(Throwable("Some error occurred"))
            }
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            when(t){
                is UnknownHostException -> Throwable("Please check your internet connection.")
                is SocketTimeoutException -> Throwable("Very poor connection. Please check your internet.")
                else -> Throwable("Something went wrong")
            }
            onFailure(t)
        }
    })
}