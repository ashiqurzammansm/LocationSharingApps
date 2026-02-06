package com.example.locationsharing.ui.profile

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.locationsharing.R
import com.example.locationsharing.databinding.ActivityMyProfileBinding
import com.example.locationsharing.utils.LocationHelper
import com.example.locationsharing.viewmodel.ProfileViewModel

class MyProfileActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityMyProfileBinding
    private var binding: ActivityMyProfileBinding
        get() = _binding
        set(value) {
            _binding = value
        }
    private lateinit var viewModel: ProfileViewModel
    private lateinit var locationHelper: LocationHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_profile)
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        locationHelper = LocationHelper(this)

        binding.btnUpdateProfile.setOnClickListener {
            locationHelper.getLastLocation { lat, lng ->
                viewModel.updateProfile(
                    binding.etName.text.toString(),
                    lat,
                    lng
                )
            }
        }

        observeUpdate()
    }

    private fun observeUpdate() {
        viewModel.updateStatus.observe(this) {
            if (it) {
                Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}