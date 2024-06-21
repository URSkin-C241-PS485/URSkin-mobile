package com.example.urskin.view.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.urskin.R
import com.example.urskin.databinding.ActivitySignupBinding
import com.example.urskin.view.ViewModelFactory
import com.example.urskin.view.login.LoginActivity

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private val signUpViewModel by viewModels<SignupViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
    }

    private fun setupAction(){

            binding.tvLogin.setOnClickListener{
                val loginIntent = Intent(this,LoginActivity::class.java)
                startActivity(loginIntent)
            }
        binding.btnSignup.setOnClickListener {
                val name = binding.nameTextEdit.text.toString()
                val email = binding.emailTextEdit.text.toString()
                val password = binding.pwTextEdit.text.toString()

                signUpViewModel.register(name, email, password).observe(this) { response ->
                    Log.d("Signup activity", response.error.toString())
                    Log.d("Signup activity", response.message.toString())

                    if (response.error != "fail") {
                        AlertDialog.Builder(this).apply {
                            setTitle(getString(R.string.welcome))
                            setMessage(getString(R.string.success))
                            setPositiveButton(getString(R.string.conti)) { _, _ ->
                                val intent = Intent(context, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            create()
                            show()
                        }
                    } else {
                        val error = response.message
                        AlertDialog.Builder(this).apply {
                            setTitle(getString(R.string.sorry))
                            setMessage(
                                getString(R.string.failed) +
                                        "Error: $error")
                            setPositiveButton(getString(R.string.try_again)) { _, _ ->
                                val intent = Intent(context, SignupActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            create()
                            show()
                        }
                    }
                }
            }
            signUpViewModel.isLoading.observe(this) {
                showLoading(it)
            }
        }




    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}