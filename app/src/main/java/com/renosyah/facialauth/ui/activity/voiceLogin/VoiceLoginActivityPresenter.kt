package com.renosyah.facialauth.ui.activity.voiceLogin

import com.renosyah.facialauth.model.Student
import com.renosyah.facialauth.service.RetrofitService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class VoiceLoginActivityPresenter  : VoiceLoginActivityContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private val api: RetrofitService = RetrofitService.create()
    private lateinit var view: VoiceLoginActivityContract.View

    override fun getOneStudent(nim : String, enableLoading: Boolean) {
        if (enableLoading) {
            view.showProgressOnGetOneStudent(true)
        }
        val subscribe = api.getOneStudent(nim)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result: Student? ->
                if (enableLoading) {
                    view.showProgressOnGetOneStudent(false)
                }
                if (result != null) {
                    view.onGetOneStudent(result)
                }
            }, { t: Throwable ->
                if (enableLoading) {
                    view.showProgressOnGetOneStudent(false)
                }
                view.showErrorOnGetOneStudent(t.message!!)
            })

        subscriptions.add(subscribe)
    }

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: VoiceLoginActivityContract.View) {
        this.view = view
    }
}