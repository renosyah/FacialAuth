package com.renosyah.facialauth.di.component
import com.renosyah.facialauth.di.module.ActivityModule
import com.renosyah.facialauth.ui.activity.voiceLogin.VoiceLoginActivity
import dagger.Component

@Component(modules = arrayOf(ActivityModule::class))
interface ActivityComponent {
    // add for each new activity
    fun inject(voiceLoginActivity: VoiceLoginActivity)
}