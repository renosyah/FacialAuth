package com.renosyah.facialauth.ui.activity.voiceLogin

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.renosyah.facialauth.R
import com.renosyah.facialauth.di.component.DaggerActivityComponent
import com.renosyah.facialauth.di.module.ActivityModule
import com.renosyah.facialauth.model.Student
import com.renosyah.facialauth.ui.util.ErrorLayout
import com.renosyah.facialauth.ui.util.LoadingLayout
import kotlinx.android.synthetic.main.activity_voice_login.*
import net.gotev.speech.GoogleVoiceTypingDisabledException
import net.gotev.speech.Speech
import net.gotev.speech.SpeechDelegate
import net.gotev.speech.SpeechRecognitionNotAvailable
import javax.inject.Inject


class VoiceLoginActivity : AppCompatActivity(),VoiceLoginActivityContract.View {

    @Inject
    lateinit var presenter: VoiceLoginActivityContract.Presenter

    val MY_PERMISSIONS_REQUEST_MICROPHONE = 122

    lateinit var context: Context
    lateinit var loading :LoadingLayout
    lateinit var error : ErrorLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voice_login)
        initWidget()
    }

    private fun initWidget(){
        this.context = this@VoiceLoginActivity

        injectDependency()
        presenter.attach(this)
        presenter.subscribe()

        Speech.init(context, packageName)

        loading = LoadingLayout(context,loading_layout)
        loading.setMessage(getString(R.string.login_message))
        loading.hide()

        error = ErrorLayout(context,error_layout)
        error.setMessage(getString(R.string.something_wrong))
        error.hide()

        speak_buttton.setOnClickListener {
            startSpeak()
        }

        requestMicrophonePermission { v ->
            enableSpeakButton(v)
        }
    }

    private fun startSpeak(){
        try {

            Speech.getInstance().startListening(object : SpeechDelegate {
                override fun onStartOfSpeech() {
                    enableSpeakButton(false)
                }

                override fun onSpeechRmsChanged(value: Float) {

                }

                override fun onSpeechPartialResults(results: List<String>) {

                }

                override fun onSpeechResult(result: String) {
                    val value = result.replace("\\s".toRegex(), "")
                    presenter.getOneStudent(value,true)
                    enableSpeakButton(true)
                }
            })

        } catch (exc: SpeechRecognitionNotAvailable) {

        } catch (exc: GoogleVoiceTypingDisabledException) {

        }
    }

    private fun enableSpeakButton(enable : Boolean){

        speak_buttton.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimaryDark))
        speak_buttton.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary))

        if (!enable){
            speak_buttton.setBackgroundColor(ContextCompat.getColor(context,R.color.colorAccent))
            speak_buttton.setTextColor(ContextCompat.getColor(context,android.R.color.white))
        }

        speak_buttton.isEnabled = enable
    }


    override fun onGetOneStudent(s: Student) {
        Toast.makeText(context,Gson().toJson(s).toString(),Toast.LENGTH_LONG).show()
    }

    override fun showProgressOnGetOneStudent(show: Boolean) {
        loading.setVisibility(show)
    }

    override fun showErrorOnGetOneStudent(e: String) {
        error.setMessage(e)
        error.show()
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_PERMISSIONS_REQUEST_MICROPHONE) {
            startActivity(Intent(context, VoiceLoginActivity::class.java))
            finish()
        }
    }

    private fun requestMicrophonePermission(next: (Boolean)->Unit) {
        if (ContextCompat.checkSelfPermission( context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ) {
            next.invoke(false)
            ActivityCompat.requestPermissions( (context as Activity),  arrayOf( Manifest.permission.RECORD_AUDIO), MY_PERMISSIONS_REQUEST_MICROPHONE )
        } else {
            next.invoke(true)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unsubscribe()
        Speech.getInstance().shutdown();
    }

    private fun injectDependency(){
        val listcomponent = DaggerActivityComponent.builder()
            .activityModule(ActivityModule(this))
            .build()

        listcomponent.inject(this)
    }


}