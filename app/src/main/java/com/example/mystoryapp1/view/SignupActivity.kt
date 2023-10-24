package com.example.mystoryapp1.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mystoryapp1.data.Result
import com.example.mystoryapp1.databinding.ActivitySignupBinding
import com.example.mystoryapp1.viewmodel.RegisterViewModel
import com.example.mystoryapp1.viewmodel.ViewModelFactory

class SignupActivity : AppCompatActivity() {
    private  val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textSignIn.setOnClickListener{
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
        setupView()
        setupAction()
        playAnimation()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val pass = binding.passwordEditText.text.toString()
            val name = binding.nameEditText.text.toString()
            if (name.isEmpty()){
                binding.nameEditText.error = "Name tidak boleh kosong"
                return@setOnClickListener
            }
            if (email.isEmpty()){
                binding.emailEditText.error = "Email tidak boleh kosong"
                return@setOnClickListener
            }
            if (pass.isEmpty()){
                binding.passwordEditText.error = "password tidak boleh kosong"
                return@setOnClickListener
            }
            if (name.isNotEmpty()&& pass.isNotEmpty()&& email.isNotEmpty()){
                viewModel.register(name,email,pass).observe(this){result->
                    when(result){
                        is Result.Loading ->{
                            showLoading(true)
                        }
                        is Result.Success ->{
                            showLoading(false)
                            AlertDialog.Builder(this).apply {
                                setTitle("Registrasi Success")
                                setMessage("Akun dengan $email sudah jadi nih. Yuk, login dan upload cerita mu")

                                create()
                                show()
                            }
                            startActivity(Intent(this, LoginActivity::class.java))
                        }
                        is Result.Error ->{
                            showLoading(false)
                            showToast(result.error)
                        }

                    }
                }

            }

        }
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val nameTextView =
            ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(100)
        val nameEditTextLayout =
            ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(100)
        val text = ObjectAnimator.ofFloat(binding.textSignIn,View.ALPHA, 1f).setDuration(100)
        val tvSignIn = ObjectAnimator.ofFloat(binding.tvSignIn,View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                title,
                nameTextView,
                nameEditTextLayout,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                signup,
                text,
                tvSignIn
            )
            startDelay = 100
        }.start()
    }
}