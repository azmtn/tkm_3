package com.example.homework_2.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.findFragment
import androidx.navigation.fragment.NavHostFragment
import com.example.homework_2.App
import com.example.homework_2.R
import com.example.homework_2.Utils.Companion.showSnackBarWithRetryAction
import com.example.homework_2.databinding.FragmentPeopleBinding
import com.example.homework_2.di.ActivityScope
import com.example.homework_2.di.component.DaggerPeopleComponent
import com.example.homework_2.domain.model.User
import com.example.homework_2.presentation.adapter.OnUserItemClickListener
import com.example.homework_2.presentation.adapter.PeopleAdapter
import com.example.homework_2.presentation.elm.people.PeopleEffect
import com.example.homework_2.presentation.elm.people.PeopleElmStoreFactory
import com.example.homework_2.presentation.elm.people.PeopleEvent
import com.example.homework_2.presentation.elm.people.PeopleState
import com.google.android.material.snackbar.Snackbar
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

@ActivityScope
internal class PeopleFragment : ElmFragment<PeopleEvent, PeopleEffect, PeopleState>(),
    OnUserItemClickListener {

    @Inject
    internal lateinit var peopleElmStoreFactory: PeopleElmStoreFactory

    override val initEvent: PeopleEvent = PeopleEvent.Ui.LoadPeopleList
    private lateinit var adapter: PeopleAdapter
    private lateinit var binding: FragmentPeopleBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPeopleBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = PeopleAdapter(this)
        binding.peopleList.adapter = adapter

    }

    override fun createStore(): Store<PeopleEvent, PeopleEffect, PeopleState> {
        val peopleComponent = DaggerPeopleComponent.factory().create(
            (activity?.application as App).applicationComponent
        )
        peopleComponent.inject(this)
        return peopleElmStoreFactory.provide()
    }

    override fun render(state: PeopleState) {
        with(adapter) {
            updateShimmer(state.isLoading)
            updateUsers(state.items)
        }
    }

    override fun handleEffect(effect: PeopleEffect) {
        when (effect) {
            is PeopleEffect.PeopleListLoadError -> {
                binding.root.showSnackBarWithRetryAction(
                    resources.getString(R.string.people_error),
                    Snackbar.LENGTH_LONG
                ) { store.accept(PeopleEvent.Ui.LoadPeopleList) }
            }
            is PeopleEffect.NavigateToProfile -> {
                NavHostFragment.findNavController(binding.root.findFragment())
                    .navigate(R.id.action_nav_people_to_nav_user, effect.bundle)
            }
        }
    }

    override fun userItemClickListener(user: User) {
        val bundle = bundleOf(
            ProfileFragment.USER_KEY to user
        )
        store.accept(PeopleEvent.Ui.LoadProfile(bundle))
    }

    companion object {
        const val NOT_FOUND_PRESENCE_KEY = "not found"
    }
}
