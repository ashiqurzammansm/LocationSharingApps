package com.example.locationsharing.ui.friends

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.locationsharing.data.model.User
import com.example.locationsharing.databinding.ItemFriendBinding

class FriendAdapter(
    private val onClick: (User) -> Unit
) : RecyclerView.Adapter<FriendAdapter.ViewHolder>() {

    private var list = listOf<User>()

    fun submitList(data: List<User>) {
        list = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFriendBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class ViewHolder(private val binding: ItemFriendBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) {
            binding.tvName.text = user.displayName
            binding.tvEmail.text = user.email
            binding.root.setOnClickListener { onClick(user) }
        }
    }
}
