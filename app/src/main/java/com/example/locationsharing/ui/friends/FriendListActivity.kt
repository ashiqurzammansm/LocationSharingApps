package com.example.locationsharing.ui.friends

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.locationsharing.R
import com.example.locationsharing.databinding.ActivityFriendListBinding
import com.example.locationsharing.ui.auth.AuthActivity
import com.example.locationsharing.ui.map.GoogleMapActivity
import com.example.locationsharing.viewmodel.FriendViewModel
import com.google.firebase.auth.FirebaseAuth

class FriendListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFriendListBinding
    private lateinit var viewModel: FriendViewModel
    private lateinit var adapter: FriendAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_friend_list)
        viewModel = ViewModelProvider(this)[FriendViewModel::class.java]

        adapter = FriendAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        // 🔹 OPEN MAP
        binding.btnOpenMap.setOnClickListener {
            startActivity(Intent(this, GoogleMapActivity::class.java))
        }

        // 🔹 LOGOUT
        binding.btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, AuthActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        observeUsers()
        viewModel.fetchAllUsers()
    }

    private fun observeUsers() {
        viewModel.userList.observe(this) {
            adapter.submitList(it)
        }
    }
}