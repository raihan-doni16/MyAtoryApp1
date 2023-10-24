package com.example.mystoryapp1.view


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.example.mystoryapp1.R
import com.example.mystoryapp1.data.Result
import com.example.mystoryapp1.databinding.ActivityAddStoryBinding
import com.example.mystoryapp1.reduceFileImage
import com.example.mystoryapp1.uriToFile
import com.example.mystoryapp1.view.CameraActivity.Companion.CAMERAX_RESULT
import com.example.mystoryapp1.viewmodel.AddStoryViewModel
import com.example.mystoryapp1.viewmodel.ViewModelFactory


class AddStoryActivity : AppCompatActivity() {
    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
    private  val viewModel by viewModels<AddStoryViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private  lateinit var binding: ActivityAddStoryBinding
    private  var imageUri: Uri? = null

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionGranted()){
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }


        binding.cameraXButton.setOnClickListener {  startCameraX()}
        binding.galleryButton.setOnClickListener { startGallery() }
        binding.uploadButton.setOnClickListener { uploadImage() }

        supportActionBar?.title = "Add Story"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private  fun allPermissionGranted()= ContextCompat.checkSelfPermission(this, REQUIRED_PERMISSION)== PackageManager.PERMISSION_GRANTED

    private  fun startGallery(){
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

    }
    private  val launcherGallery = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){uri: Uri?->
        if (uri != null){
            imageUri = uri
            showImage()
        }
    }
    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }
    private  val launcherIntentCameraX = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == CAMERAX_RESULT){
            imageUri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            showImage()
        }
    }
    private  fun showImage(){
        imageUri?.let {
            Log.d("Image URL", "ShowImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }
    private  fun uploadImage(){
        imageUri?.let { uri->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            Log.d("Image File", "ShowImage: ${imageFile.path}")
            val description = binding.tvDes.text.toString()

            viewModel.postImage(imageFile,description).observe(this){result->
                if (result != null){
                    when(result){
                            is Result.Loading -> {
                                showLoading(isLoading = true)
                            }
                        is Result.Success -> {
                            showLoading(isLoading = false)
                            val intent= Intent(this, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        }
                        is Result.Error -> {
                            showLoading(isLoading = false)
                            showToast(result.error)
                        }
                    }
                }
            }
        }?: showToast(getString(R.string.empty))

    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}