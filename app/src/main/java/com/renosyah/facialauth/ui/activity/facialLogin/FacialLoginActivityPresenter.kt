package com.renosyah.facialauth.ui.activity.facialLogin

import android.net.DnsResolver
import com.renosyah.facialauth.model.Student
import com.renosyah.facialauth.model.ValidateResponse
import com.renosyah.facialauth.service.RetrofitService
import com.renosyah.facialauth.ui.activity.voiceLogin.VoiceLoginActivityContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response

class FacialLoginActivityPresenter : FacialLoginActivityContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private val api: RetrofitService = RetrofitService.create()
    private lateinit var view: FacialLoginActivityContract.View

    override fun validateImageProfile(id: String, file: MultipartBody.Part, enableLoading: Boolean) {
        if (enableLoading) {
            view.showProgressValidateImageProfile(true)
        }
        val subscribe = api.validateImageProfile(id,file)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result: ValidateResponse? ->
                if (enableLoading) {
                    view.showProgressValidateImageProfile(false)
                }
                if (result != null) {
                    view.onValidateImageProfile(result)
                }
            }, { t: Throwable ->
                if (enableLoading) {
                    view.showProgressValidateImageProfile(false)
                }
                view.showErrorValidateImageProfile(t.message!!)
            })

        subscriptions.add(subscribe)

    }

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: FacialLoginActivityContract.View) {
        this.view = view
    }
}