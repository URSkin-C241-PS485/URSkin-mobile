package com.example.urskin.view.predict

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.urskin.R

import com.example.urskin.databinding.ActivityPredictBinding
import com.example.urskin.helper.ImageClassifierHelper
import com.example.urskin.view.result.ResultActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.tensorflow.lite.task.vision.classifier.Classifications

class PredictActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPredictBinding

    private var currentImageUri: Uri? = null

  
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPredictBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.apply {
            btnUpload.setOnClickListener { startGallery() }
            btnPredict.setOnClickListener {
                currentImageUri?.let {
                    analyzeImage(it)
                }?: showToast("No image selected")
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
            currentImageUri = uri
            showImage()
        }else{
            Log.d("Photo Picker", "No media selected")
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
                        lifecycleScope.launch(Dispatchers.IO) {
                            this@PredictActivity.runOnUiThread {
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