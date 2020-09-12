package com.renosyah.facialauth.ui.activity.facialLogin

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.FileUtils
import android.util.Size
import android.view.Surface
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.renosyah.facialauth.R
import com.renosyah.facialauth.di.component.DaggerActivityComponent
import com.renosyah.facialauth.di.module.ActivityModule
import com.renosyah.facialauth.model.Student
import com.renosyah.facialauth.model.ValidateResponse
import com.renosyah.facialauth.ui.activity.home.HomeActivity
import com.renosyah.facialauth.ui.activity.voiceLogin.VoiceLoginActivity
import com.renosyah.facialauth.ui.util.ErrorLayout
import com.renosyah.facialauth.ui.util.LoadingLayout
import com.renosyah.facialauth.util.ImageRotation.Companion.getStreamByteFromImage
import com.renosyah.facialauth.util.SerializableSave
import kotlinx.android.synthetic.main.activity_facial_login.*
import kotlinx.android.synthetic.main.activity_voice_login.error_layout
import kotlinx.android.synthetic.main.activity_voice_login.loading_layout
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors
import javax.inject.Inject


class FacialLoginActivity : AppCompatActivity(), FacialLoginActivityContract.View {

    @Inject
    lateinit var presenter: FacialLoginActivityContract.Presenter

    lateinit var context: Context
    lateinit var loading : LoadingLayout
    lateinit var error : ErrorLayout

    lateinit var student : Student
    lateinit var detector : FaceDetector
    private var imageCapture: ImageCapture? = null
    private var captured = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_facial_login)
        initWidget()
    }

    private fun initWidget() {
        this.context = this@FacialLoginActivity

        injectDependency()
        presenter.attach(this)
        presenter.subscribe()

        if (intent.hasExtra("student")){
            student = intent.getSerializableExtra("student") as Student
        }
        
        loading = LoadingLayout(context,loading_layout)
        loading.setMessage(getString(R.string.validating))
        loading.hide()

        error = ErrorLayout(context,error_layout){
            val i = Intent(context,VoiceLoginActivity::class.java)
            startActivity(i)
            finish()
        }
        error.setMessage(getString(R.string.something_wrong))
        error.hide()

        val realTimeOpts = FaceDetectorOptions.Builder()
            .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
            .build()

        detector = FaceDetection.getClient(realTimeOpts)

        startCamera()
    }


    @SuppressLint("RestrictedApi")
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener(Runnable {
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewFinder.createSurfaceProvider())
                }

            imageCapture = ImageCapture.Builder()
                .setTargetResolution(Size(480,480))
                .setTargetRotation(Surface.ROTATION_0)
                .build()

            try {

                cameraProvider.unbindAll()

                val imageAnalysis = ImageAnalysis.Builder()
                    .setTargetResolution(Size(1080,1080))
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()

                imageAnalysis.setAnalyzer(Executors.newSingleThreadExecutor(), ImageAnalysis.Analyzer { imageProxy ->
                    detectFace(imageProxy)
                })

                cameraProvider.bindToLifecycle(this, CameraSelector.DEFAULT_FRONT_CAMERA, imageAnalysis, preview, imageCapture)

            } catch(ignore : Exception) { }

        }, ContextCompat.getMainExecutor(context))
    }

    @SuppressLint("UnsafeExperimentalUsageError")
    private fun detectFace(imageProxy : ImageProxy){

        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            detector.process(image)
                .addOnSuccessListener { faces ->
                    if (faces.isEmpty()){
                        imageProxy.close()
                        return@addOnSuccessListener
                    }

                    if (!captured){
                        takePhoto()
                        captured = true
                    }

                }
                .addOnFailureListener { e ->
                    imageProxy.close()
                }
        }
    }

    private fun uploadImage(f : File){

        val requestFile = RequestBody.create(MediaType.parse("image/*"),f)
        val file = MultipartBody.Part.createFormData("file", f.name, requestFile)

        presenter.validateImageProfile(student.Id,file,true)
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val photoFile = File(
            getOutputDirectory(),
            SimpleDateFormat(FILENAME_FORMAT, Locale.US
            ).format(System.currentTimeMillis()) + ".jpg")

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions, ContextCompat.getMainExecutor(context), object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(context, exc.message, Toast.LENGTH_SHORT).show()
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val byteF = getStreamByteFromImage(photoFile)
                    FileOutputStream(photoFile).use { stream ->
                        stream.write(byteF)
                        uploadImage(photoFile)
                    }
                }
            })
    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() } }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }


    override fun onValidateImageProfile(v: ValidateResponse) {
        if (v.Validate){
            SerializableSave(context, SerializableSave.userDataFileSessionName).save(student)
            val i = Intent(context,HomeActivity::class.java)
            startActivity(i)
            finish()
            return
        }
        error.setMessage(v.Message)
        error.show()
    }

    override fun showProgressValidateImageProfile(show: Boolean) {
        loading.setVisibility(show)
    }

    override fun showErrorValidateImageProfile(e: String) {
        error.setMessage(e)
        error.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unsubscribe()
    }

    private fun injectDependency(){
        val listcomponent = DaggerActivityComponent.builder()
            .activityModule(ActivityModule(this))
            .build()

        listcomponent.inject(this)
    }


    companion object {
        private const val TAG = "CameraXBasic"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    }
}