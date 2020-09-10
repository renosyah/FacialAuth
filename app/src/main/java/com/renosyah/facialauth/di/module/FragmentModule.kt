package com.renosyah.facialauth.di.module
import dagger.Module
import dagger.Provides
import android.app.Activity
import androidx.fragment.app.Fragment
import com.renosyah.facialauth.service.RetrofitService

@Module
class FragmentModule(private var fragment: Fragment) {

    @Provides
    fun provideFragment() : Fragment {
        return fragment
    }

    @Provides
    fun provideApiService(): RetrofitService {
        return RetrofitService.create()
    }

    // add for each new fragment
}