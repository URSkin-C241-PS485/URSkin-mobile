package com.example.urskin.view.result

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.urskin.R
import com.example.urskin.databinding.ActivityResultBinding
import com.example.urskin.view.main.MainActivity
import com.example.urskin.view.predict.PredictActivity

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI))
        val result = intent.getStringExtra(EXTRA_RESULT)

        imageUri?.let {
            Log.d("Image URI","showImage: $it")
            binding.imgResult.setImageURI(it)
        }
        result?.let {
            Log.d("Result","showResult: $it")
            binding.tvResult.text = it

        }

        setupAction()
    }

    private fun setupAction(){
        binding.layoutGoto.setOnClickListener {
            val predictIntent = Intent(this, MainActivity::class.java)
            startActivity(predictIntent)
        }
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_RESULT = "extra_result"
    }
}