package com.renosyah.facialauth.di.module
import dagger.Module
import dagger.Provides
import android.app.Activity
import com.renosyah.facialauth.service.RetrofitService

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
}