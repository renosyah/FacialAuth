package com.renosyah.facialauth.service
import com.renosyah.facialauth.model.Student
import com.renosyah.facialauth.model.ValidateResponse
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface RetrofitService {

    // add more end point to access
    @GET("api/student/{nim}")
    fun getOneStudent(@Path("nim") nim: String) : Observable<Student>

    @Multipart
    @POST("api/student/{id}/image/validate")
    fun validateImageProfile(@Path("id") id: String,@Part file: MultipartBody.Part) : Observable<ValidateResponse>

    companion object {

        // val baseURL = "http://192.168.1.10:8000/"
        val baseURL = "https://face-auth-renosyah.herokuapp.com/"

        fun create() : RetrofitService {

            val okHttpClient = OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build()

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseURL)
                .client(okHttpClient)
                .build()

            return retrofit.create(RetrofitService::class.java)
        }
    }
}