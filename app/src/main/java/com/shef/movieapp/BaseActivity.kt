package com.shef.movieapp
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    private val networkChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            val isConnected = activeNetworkInfo != null && activeNetworkInfo.isConnected
            updateActionBarColor(isConnected)
        }
    }

    fun updateActionBarColor(isConnected: Boolean) {
        val color = if (isConnected) Color.MAGENTA else Color.GRAY
        supportActionBar?.setBackgroundDrawable(ColorDrawable(color))
    }

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkChangeReceiver, intentFilter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(networkChangeReceiver)
    }
}
