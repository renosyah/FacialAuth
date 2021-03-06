package com.renosyah.facialauth.di.module
import dagger.Module
import dagger.Provides
import android.app.Activity
import com.renosyah.facialauth.service.RetrofitService
import com.renosyah.facialauth.ui.activity.facialLogin.FacialLoginActivityContract
import com.renosyah.facialauth.ui.activity.facialLogin.FacialLoginActivityPresenter
import com.renosyah.facialauth.ui.activity.voiceLogin.VoiceLoginActivityContract
import com.renosyah.facialauth.ui.activity.voiceLogin.VoiceLoginActivityPresenter

@Module
class ActivityModule(private var activity : Activity) {
    @Provides
    fun provideActivity() : Activity {
        return activity
    }

    @Provides
    fun provideApiService(): RetrofitService {
        return RetrofitService.create()
    }

    // add for each new activity
    @Provides
    fun provideVoiceLoginActivityPresenter(): VoiceLoginActivityContract.Presenter {
        return VoiceLoginActivityPresenter()
    }

    @Provides
    fun provideFacialLoginActivityPresenter(): FacialLoginActivityContract.Presenter {
        return FacialLoginActivityPresenter()
    }
}