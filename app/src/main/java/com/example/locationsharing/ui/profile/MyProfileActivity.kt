package com.example.locationsharing.ui.profile

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.locationsharing.R
import com.example.locationsharing.databinding.ActivityMyProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MyProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_my_profile
        )

        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val db = FirebaseFirestore.getInstance()

        db.collection("users").document(uid)
            .get()
            .addOnSuccessListener {
                binding.tvEmail.text = it.getString("email")
                binding.tvLat.text = it.getDouble("latitude").toString()
                binding.tvLng.text = it.getDouble("longitude").toString()
            }

        binding.btnUpdateProfile.setOnClickListener {
            db.collection("users").document(uid)
                .update("displayName", binding.etName.text.toString())
                .addOnSuccessListener {
                    Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
