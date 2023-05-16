package com.example.homework_2.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.homework_2.App
import com.example.homework_2.R
import com.example.homework_2.Utils.Companion.showSnackBarWithRetryAction
import com.example.homework_2.databinding.FragmentProfileBinding
import com.example.homework_2.di.ActivityScope
import com.example.homework_2.di.component.DaggerProfileComponent
import com.example.homework_2.domain.model.User
import com.example.homework_2.presentation.elm.profile.ProfileEffect
import com.example.homework_2.presentation.elm.profile.ProfileElmStoreFactory
import com.example.homework_2.presentation.elm.profile.ProfileEvent
import com.example.homework_2.presentation.elm.profile.ProfileState
import com.google.android.material.snackbar.Snackbar
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

@ActivityScope
internal class ProfileFragment : ElmFragment<ProfileEvent, ProfileEffect, ProfileState>() {

    @Inject
    internal lateinit var profileElmStoreFactory: ProfileElmStoreFactory

    override var initEvent: ProfileEvent = ProfileEvent.Ui.InitEvent
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backIcon.setOnClickListener {
            requireActivity().onBackPressed()
        }
        if (arguments == null) {
            store.accept(ProfileEvent.Ui.LoadOwnProfile)
        } else {
            store.accept(ProfileEvent.Ui.LoadProfile(requireArguments()))
        }
    }

    override fun createStore(): Store<ProfileEvent, ProfileEffect, ProfileState> {
        val profileComponent = DaggerProfileComponent.factory().create(
            (activity?.application as App).applicationComponent
        )
        profileComponent.inject(this)
        return profileElmStoreFactory.provide()    }

    override fun render(state: ProfileState) {
        if (state.items.isNotEmpty()) fillViewsWithUserData(state.items[0])
    }

    override fun handleEffect(effect: ProfileEffect) {
        when(effect) {
            is ProfileEffect.ProfileLoadError -> {
                binding.root.showSnackBarWithRetryAction(
                    resources.getString(R.string.people_error),
                    Snackbar.LENGTH_LONG
                ) { store.accept(ProfileEvent.Ui.LoadOwnProfile) }
            }
        }
    }

    private fun fillViewsWithUserData(user: User) {
        binding.userName.text = user.fullName
        binding.userPresence.text = user.presence

        when (user.presence) {
            ACTIVE_PRESENCE_KEY -> binding.userPresence.setTextColor(
                binding.root.context.getColor(ACTIVE_PRESENCE_COLOR)
            )
            IDLE_PRESENCE_KEY -> binding.userPresence.setTextColor(
                binding.root.context.getColor(IDLE_PRESENCE_COLOR)
            )
            else -> binding.userPresence.setTextColor(
                binding.root.context.getColor(OFFLINE_PRESENCE_COLOR)
            )
        }

        if (user.avatarUrl != null) {
            Glide.with(binding.root)
                .asBitmap()
                .load(user.avatarUrl)
                .error(R.drawable.default_avatar)
                .into(binding.profileAvatar)
        } else {
            binding.profileAvatar.setImageResource(R.drawable.default_avatar)
        }
    }

    companion object {
        const val USER_KEY = "user_key"

        const val ACTIVE_PRESENCE_KEY = "active"
        const val IDLE_PRESENCE_KEY = "idle"

        const val ACTIVE_PRESENCE_COLOR = R.color.green_light
        const val IDLE_PRESENCE_COLOR = R.color.orange
        const val OFFLINE_PRESENCE_COLOR = R.color.offline
    }
}
