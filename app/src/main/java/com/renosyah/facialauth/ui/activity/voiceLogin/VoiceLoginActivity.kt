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
import com.renosyah.facialauth.ui.activity.facialLogin.FacialLoginActivity
import com.renosyah.facialauth.ui.util.ErrorLayout
import com.renosyah.facialauth.ui.util.LoadingLayout
import kotlinx.android.synthetic.main.activity_voice_login.*
import net.gotev.speech.GoogleVoiceTypingDisabledException
import net.gotev.speech.Speech
import net.gotev.speech.SpeechDelegate
import net.gotev.speech.SpeechRecognitionNotAvailable
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.schedule


class VoiceLoginActivity : AppCompatActivity(), VoiceLoginActivityContract.View {

    @Inject
    lateinit var presenter: VoiceLoginActivityContract.Presenter

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

        error = ErrorLayout(context,error_layout) {

        }
        error.setMessage(getString(R.string.something_wrong))
        error.hide()

        speak_buttton.setOnClickListener {
            startSpeak()
        }
        enableSpeakButton(true)
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
                    nim_textview.text = value
                    Timer().schedule(2000){
                        runOnUiThread {
                            presenter.getOneStudent(value,true)
                        }
                    }
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
            speak_buttton.setBackgroundColor(ContextCompat.getColor(context,android.R.color.darker_gray))
            speak_buttton.setTextColor(ContextCompat.getColor(context,R.color.colorAccent))
        }

        speak_buttton.isEnabled = enable
    }


    override fun onGetOneStudent(s: Student) {
        val i = Intent(context,FacialLoginActivity::class.java)
        i.putExtra("student",s)
        startActivity(i)
        finish()
    }

    override fun showProgressOnGetOneStudent(show: Boolean) {
        loading.setVisibility(show)
    }

    override fun showErrorOnGetOneStudent(e: String) {
        enableSpeakButton(true)
        error.setMessage(e)
        error.show()
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