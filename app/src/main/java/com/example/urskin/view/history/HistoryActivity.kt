package com.example.urskin.view.history

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.urskin.R
import com.example.urskin.data.adapter.HistoryAdapter
import com.example.urskin.data.room.HistoryEntity
import com.example.urskin.databinding.ActivityHistoryBinding
import com.example.urskin.databinding.ActivityMainBinding

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding

    private val historyViewModel: HistoryViewModel by viewModels {
        HistoryModelFactory.getInstance(application)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvHistory.layoutManager = layoutManager

        historyViewModel.historyList.observe(this){
            showHistory(it)
        }
    }

    private fun showHistory(consumer:List<HistoryEntity>){
        val adapter = HistoryAdapter()
        adapter.submitList(consumer)
        binding.rvHistory.adapter = adapter
    }
}