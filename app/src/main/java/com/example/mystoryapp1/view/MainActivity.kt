package com.example.mystoryapp1.view

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mystoryapp1.R
import com.example.mystoryapp1.adapter.UserAdapter
import com.example.mystoryapp1.data.Result
import com.example.mystoryapp1.data.response.ListStoryItem
import com.example.mystoryapp1.databinding.ActivityMainBinding
import com.example.mystoryapp1.viewmodel.MainViewModel
import com.example.mystoryapp1.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

       showStory()
        setupView()
        supportActionBar?.title = "Story App"

    }
private  fun showStory(){
    val layoutManager = LinearLayoutManager(this)
    binding.storyRV.layoutManager = layoutManager

    val item = DividerItemDecoration(this, layoutManager.orientation)
    binding.storyRV.addItemDecoration(item)

    viewModel.getSession().observe(this){session->
        if (!session.isLogin){
            val intent= Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }else{
            viewModel.getStory().observe(this){item->
                if (item != null){
                    when(item){
                        is Result.Loading ->{
                            showLoading(isLoading = true)
                        }
                        is Result.Success -> {
                            val dataStory = item.data
                            val storyAdapter =UserAdapter(object : UserAdapter.OnItemClickCallBack{
                                override fun onItemClicked(data: ListStoryItem) {
                                   val intent = Intent(this@MainActivity,DetailActivity::class.java)
                                    intent.putExtra(DetailActivity.EXTRA_PHOTO, data.photoUrl)
                                    intent.putExtra(DetailActivity.EXTRA_DESCRIPTION, data.description)
                                    intent.putExtra(DetailActivity.EXTRA_NAME, data.name)
                                    binding.storyRV.context.startActivity(intent,ActivityOptionsCompat.makeSceneTransitionAnimation(binding.storyRV.context as Activity).toBundle())
                                }
                            })
                            showLoading(isLoading = false)
                            storyAdapter.submitList(dataStory)
                            binding.storyRV.adapter = storyAdapter
                        }
                        is Result.Error-> {
                            showLoading(isLoading = false)
                            showToast(item.error)
                        }
                    }
                }
            }
        }
    }
}
    override fun onResume() {
        super.onResume()
        viewModel.getStory()
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

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.btn_log->{

                viewModel.logout()
                true
            }
            R.id.btn_cam ->{
                val intent = Intent(this, AddStoryActivity::class.java)
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this)
                ActivityCompat.startActivity(this, intent, options.toBundle())
                true
            }
            else->false

        }
        return super.onOptionsItemSelected(item)
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}