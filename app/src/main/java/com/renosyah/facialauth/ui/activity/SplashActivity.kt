package com.renosyah.facialauth.ui.activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.renosyah.facialauth.R
import com.renosyah.facialauth.ui.activity.home.HomeActivity
import com.renosyah.facialauth.ui.activity.voiceLogin.VoiceLoginActivity
import com.renosyah.facialauth.util.SerializableSave
import java.util.*
import kotlin.concurrent.schedule

class SplashActivity : AppCompatActivity() {

    lateinit var context: Context
    private val MY_PERMISSIONS_REQUEST = 122

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        initWidget()
    }

    private fun initWidget() {
        this.context = this@SplashActivity

        if (SerializableSave(context, SerializableSave.userDataFileSessionName).load() != null){
            val i = Intent(context, HomeActivity::class.java)
            startActivity(i)
            finish()
            return
        }

        Timer().schedule(2000){
            requestPermission { v ->
                startActivity(Intent(context,VoiceLoginActivity::class.java))
                finish()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_PERMISSIONS_REQUEST) {
            startActivity(Intent(context, SplashActivity::class.java))
            finish()
        }
    }

    private fun requestPermission(next: (Boolean)->Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((context as Activity),arrayOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA), MY_PERMISSIONS_REQUEST)

        } else {
            next.invoke(true)
        }
    }
}