package com.renosyah.facialauth.di.module
import android.app.Application
import com.renosyah.facialauth.BaseApp
import com.renosyah.facialauth.di.scope.PerApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class ApplicationModule(private val baseApp: BaseApp) {

    @Provides
    @Singleton
    @PerApplication
    fun provideApplication(): Application {
        return baseApp
    }
}