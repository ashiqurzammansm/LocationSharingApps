package com.example.locationsharing.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.locationsharing.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FriendViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _userList = MutableLiveData<List<User>>()
    val userList: LiveData<List<User>> = _userList

    fun fetchAllUsers() {
        firestore.collection("users")
            .addSnapshotListener { snapshot, _ ->
                val currentUid = auth.currentUser?.uid
                snapshot?.let {
                    val users = it.toObjects(User::class.java)
                        .filter { user -> user.uid != currentUid }
                    _userList.postValue(users)
                }
            }
    }
}
