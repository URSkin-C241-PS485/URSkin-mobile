package com.example.urskin.view.article

import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.urskin.R
import com.example.urskin.data.pref.ArticleModel
import com.example.urskin.databinding.ActivityArticleBinding

class ArticleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArticleBinding

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = if (Build.VERSION.SDK_INT >= 33) intent.getParcelableExtra(
            "key_article",
            ArticleModel::class.java
        ) as ArticleModel
        else @Suppress("DEPRECATION")
        intent.getParcelableExtra("key_article")


        if(data!=null){
            binding.apply {
                tvSource.text=data.source
                tvTittle.text=data.title
                tvDesc.text=data.description
                imgArticle.setImageResource(data.img)
            }
        }

        binding.topAppBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            finish()
        }

    }


}