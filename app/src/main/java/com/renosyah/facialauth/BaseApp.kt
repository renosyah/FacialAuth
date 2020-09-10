package com.renosyah.facialauth
import android.app.Application
import com.renosyah.facialauth.di.component.ApplicationComponent
import com.renosyah.facialauth.di.component.DaggerApplicationComponent
import com.renosyah.facialauth.di.module.ApplicationModule

class BaseApp : Application() {

    lateinit var component: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        instance = this
        setup()

        if (BuildConfig.DEBUG) {
        }
    }

    fun setup() {
        component = DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(this)).build()
        component.inject(this)
    }

    companion object {
        lateinit var instance: BaseApp private set
    }
}