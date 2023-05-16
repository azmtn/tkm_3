package com.example.homework_2.presentation.adapter

import com.example.homework_2.domain.model.User

internal interface OnUserItemClickListener {

    fun userItemClickListener(user: User)

}