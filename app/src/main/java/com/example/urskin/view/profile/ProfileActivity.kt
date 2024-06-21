package com.example.urskin.view.profile

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.urskin.R
import com.example.urskin.data.response.ProfileResponse
import com.example.urskin.databinding.ActivityProfileBinding
import com.example.urskin.view.ViewModelFactory
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    private val profileViewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.topAppBar.setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
                finish()
            }

        profileViewModel.profileData.observe(this, Observer { profile ->
            profile?.let {
                binding.profileName.text = it.fullName
                binding.profileEmail.text = it.email
            }
        })
    }


    private fun showProfile(profile: ProfileResponse){
        binding.apply {
            profileName.text = profile.fullName
            profileEmail.text = profile.email
        }
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
