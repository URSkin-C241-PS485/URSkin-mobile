package com.example.urskin.view.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.urskin.R
import com.example.urskin.data.adapter.ArticleAdapter
import com.example.urskin.data.pref.ArticleModel
import com.example.urskin.databinding.ActivityMainBinding
import com.example.urskin.view.ViewModelFactory
import com.example.urskin.view.article.ArticleActivity
import com.example.urskin.view.history.HistoryActivity
import com.example.urskin.view.login.LoginActivity
import com.example.urskin.view.onboarding.OnboardingActivity
import com.example.urskin.view.predict.PredictActivity
import com.example.urskin.view.profile.ProfileActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var rvItems: RecyclerView
    private val list = ArrayList<ArticleModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.topAppBar)

        rvItems = findViewById(R.id.rv_article)
        rvItems.setHasFixedSize(true)


        setupAction()

        list.addAll(getArticle())
        showRecycleList()



    }

    private fun setupAction(){
        binding.fabCamera.setOnClickListener {
            val intent = Intent(this, PredictActivity::class.java)
            startActivity(intent)
        }

        mainViewModel.getSession().observe(this){ user ->
            if (!user.isLogin){
                startActivity(Intent(this, OnboardingActivity::class.java))
                finish()
            }else{
                mainViewModel.isLoading.observe(this){
                    showLoading(it)
                }
            }
        }
    }


    @SuppressLint("Recycle")
    private fun getArticle(): ArrayList<ArticleModel>{
        val id = resources.getStringArray(R.array.data_id)
        val source = resources.getStringArray(R.array.data_source)
        val tittle = resources.getStringArray(R.array.data_tittle)
        val desc = resources.getStringArray(R.array.data_desc)
        val img = resources.obtainTypedArray(R.array.data_img)
        val listArticle =ArrayList<ArticleModel>()
        for (i in id.indices){
            val article =
                ArticleModel(id[i],source[i],tittle[i],desc[i],img.getResourceId(i,-1))
            listArticle.add(article)
        }
        return listArticle
    }

    private fun showRecycleList(){
        rvItems.layoutManager = LinearLayoutManager(this)
        val listAdapter = ArticleAdapter(list)
        rvItems.adapter= listAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_options, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.history -> {
                val historyIntent = Intent(this, HistoryActivity::class.java)
                startActivity(historyIntent)
                true
            }
            R.id.profie -> {
                val profileIntent = Intent(this,ProfileActivity::class.java)
                startActivity(profileIntent)
                true
            }
            R.id.logout -> {
                mainViewModel.logout()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}