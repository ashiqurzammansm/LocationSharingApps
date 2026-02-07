package com.example.locationsharing.ui.friends

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.locationsharing.R
import com.example.locationsharing.databinding.ActivityFriendListBinding
import com.example.locationsharing.ui.auth.AuthActivity
import com.example.locationsharing.ui.map.GoogleMapActivity
import com.example.locationsharing.utils.LocationHelper
import com.example.locationsharing.viewmodel.FriendViewModel
import com.google.firebase.auth.FirebaseAuth

class FriendListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFriendListBinding
    private lateinit var viewModel: FriendViewModel
    private lateinit var adapter: FriendAdapter
    private lateinit var locationHelper: LocationHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_friend_list
        )

        viewModel = ViewModelProvider(this)[FriendViewModel::class.java]
        locationHelper = LocationHelper(this)

        adapter = FriendAdapter { user ->
            val intent = Intent(this, GoogleMapActivity::class.java)
            intent.putExtra("lat", user.latitude)
            intent.putExtra("lng", user.longitude)
            intent.putExtra("name", user.displayName)
            startActivity(intent)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        // 🔴 Start real-time location updates
        locationHelper.startLocationUpdates()

        binding.btnOpenMap.setOnClickListener {
            startActivity(Intent(this, GoogleMapActivity::class.java))
        }

        binding.btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, AuthActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        viewModel.userList.observe(this) {
            adapter.submitList(it)
        }

        viewModel.fetchAllUsers()
    }
}
