package com.example.urskin.view.predict

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.urskin.R
import com.example.urskin.data.room.HistoryEntity
import com.example.urskin.databinding.ActivityPredictBinding
import com.example.urskin.helper.ImageClassifierHelper
import com.example.urskin.utils.DateFormat
import com.example.urskin.view.history.HistoryActivity
import com.example.urskin.view.history.HistoryModelFactory
import com.example.urskin.view.history.HistoryViewModel
import com.example.urskin.view.profile.ProfileActivity
import com.example.urskin.view.result.ResultActivity
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.tensorflow.lite.task.vision.classifier.Classifications

class PredictActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPredictBinding
    private val historyViewModel: HistoryViewModel by viewModels {
        HistoryModelFactory.getInstance(application)
    }
    private var currentImageUri: Uri? = null

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                showToast("Permission request granted")
            } else {
                showToast("Permission request denied")
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPredictBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        binding.apply {
            btnUpload.setOnClickListener { startGallery() }
            btnPredict.setOnClickListener {
                currentImageUri?.let {
                    analyzeImage(it)
                }?: showToast("No image selected")
            }
            topAppBar.setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
                finish()
            }
        }

    }

    private fun startGallery(){
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ){
        uri: Uri? ->
        if (uri != null){
            UCrop.of(uri, Uri.fromFile(cacheDir.resolve("${System.currentTimeMillis()}.jpg")))
                .withAspectRatio(16F, 9F)
                .withMaxResultSize(2000, 2000)
                .start(this)
        }else{
            Log.d("Photo Picker", "No media selected")
        }
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            val resultUri = UCrop.getOutput(data!!)
            currentImageUri = resultUri
            showImage()
        } else if (resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(data!!)
            Log.e("Crop Error", "onActivityResult: $cropError")
        }
    }

    private fun showImage(){
        currentImageUri?.let {
            Log.d("Image URI","showImage: $it")
            binding.imgPreview.setImageURI(it)
        }
    }

    private fun analyzeImage(image: Uri){
        val imageHelper = ImageClassifierHelper(
            context = this,
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    showToast(error)
                }

                override fun onResults(results: List<Classifications>?) {
                    val resultString = results?.joinToString("\n") {
                        val threshold = (it.categories[0].score * 100).toInt()
                        "${it.categories[0].label} : ${threshold}%"
                    }
                    if (resultString != null) {
                        val data = HistoryEntity(date = DateFormat(System.currentTimeMillis()),
                            uri = image.toString(),
                            result = resultString)
                        lifecycleScope.launch(Dispatchers.IO) {
                            this@PredictActivity.runOnUiThread {
                                historyViewModel.addHistory(data)
                                moveToResult(image, resultString)
                            }
                        }
                    }
                }
            }
        )
        imageHelper.classifyStaticImage(image)
    }

    private fun moveToResult(image: Uri, result1: String) {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra(ResultActivity.EXTRA_IMAGE_URI, image.toString())
        intent.putExtra(ResultActivity.EXTRA_RESULT, result1)
        startActivity(intent)
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE
    }
}