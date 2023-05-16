package com.example.homework_2.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.homework_2.domain.model.User

class PeopleDiffUtil(
    private var newPersons: List<User>,
    private var oldPersons: List<User>
) : DiffUtil.Callback() {


    override fun getOldListSize(): Int {
        return oldPersons.size
    }

    override fun getNewListSize(): Int {
        return newPersons.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldPersons[oldItemPosition].userId == newPersons[newItemPosition].userId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldPersons[oldItemPosition] == newPersons[newItemPosition]
    }
}