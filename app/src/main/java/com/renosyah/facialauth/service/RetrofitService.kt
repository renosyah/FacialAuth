package com.renosyah.facialauth.service
import com.renosyah.facialauth.model.ValidateResponse
import com.renosyah.facialauth.model.Student
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface RetrofitService {

    // add more end point to access
    @GET("api/student/{nim}")
    fun getOneStudent(@Path("nim") nim: String) : Observable<Student>

    @Multipart
    @POST("api/student/{nim}/image/validate")
    fun validateImageProfile(@Path("nim") nim: String,@Part file: MultipartBody.Part) : Observable<ValidateResponse>

    companion object {

        val baseURL = "http://192.168.1.10:8000/"

        fun create() : RetrofitService {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseURL)
                .build()

            return retrofit.create(RetrofitService::class.java)
        }
    }
}