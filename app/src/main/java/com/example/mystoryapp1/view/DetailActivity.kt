package com.example.mystoryapp1.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.mystoryapp1.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private  lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val name = intent.getStringExtra(EXTRA_NAME)
        val photo = intent.getStringExtra(EXTRA_PHOTO)
        val desc = intent.getStringExtra(EXTRA_DESCRIPTION)

        supportActionBar?.title = "Detail Story"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.name.text = name
        Glide.with(this).load(photo).skipMemoryCache(true)
            .into(binding.photo)
        binding.description.text = desc
    }
    companion object {
        const val EXTRA_PHOTO = "extra_photo"
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_DESCRIPTION = "extra_description"

    }
}
