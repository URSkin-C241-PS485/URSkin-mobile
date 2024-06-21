package com.example.urskin.view.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.urskin.R
import com.example.urskin.databinding.ActivityArticleBinding
import com.example.urskin.databinding.ActivityLoginBinding
import com.example.urskin.view.ViewModelFactory
import com.example.urskin.view.main.MainActivity
import com.example.urskin.view.signup.SignupActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private  val loginViewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setAction()
    }

    private fun setAction(){
        binding.btnLogin.setOnClickListener {
            val email = binding.emailTextEdit.text.toString()
            val password = binding.pwTextEdit.text.toString()

            loginViewModel.login(email, password)
            loginViewModel.data.observe(this){ data ->
                when (data){
                    is LoginViewModel.Data.Success ->{
                        data.user
                        showLoginSuccessDialog()
                    }

                    is LoginViewModel.Data.Failed ->{
                        val errorResponse = data.errorResponse
                        val errorMessage = errorResponse.message
                        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            loginViewModel.isLoading.observe(this){
                showLoading(it)
            }

        }

        binding.tvSignup.setOnClickListener {
            val signupIntent = Intent(this,SignupActivity::class.java)
            startActivity(signupIntent)
        }
    }

    private fun showLoginSuccessDialog(){
        AlertDialog.Builder(this)
            .setTitle("Login Success")
            .setMessage("Click continue to start your journey")
            .setPositiveButton(getString(R.string.conti)) { _, _ ->
                val mainIntent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(mainIntent)
                finish()
            }
            .create()
            .show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


}