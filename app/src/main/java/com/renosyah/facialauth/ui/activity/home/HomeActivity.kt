package com.renosyah.facialauth.ui.activity.home

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.renosyah.facialauth.R
import com.renosyah.facialauth.model.Student
import com.renosyah.facialauth.ui.activity.voiceLogin.VoiceLoginActivity
import com.renosyah.facialauth.util.SerializableSave
import com.renosyah.facialauth.util.SerializableSave.Companion.userDataFileSessionName
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    lateinit var context: Context
    lateinit var student : Student

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initWidget()
    }

    private fun initWidget() {
        this.context = this@HomeActivity

        if (SerializableSave(context, SerializableSave.userDataFileSessionName).load() != null){
            student = SerializableSave(context,SerializableSave.userDataFileSessionName).load() as Student
            name_textview.text = "Hello, ${student.Name}"
        }

        logout_buttton.setOnClickListener {
            if (SerializableSave(context, SerializableSave.userDataFileSessionName).delete()){
                startActivity(Intent(context, VoiceLoginActivity::class.java))
                finish()
            }
        }
    }
}