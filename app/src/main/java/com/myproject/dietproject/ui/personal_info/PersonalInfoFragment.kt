package com.myproject.dietproject.ui.personal_info

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.myproject.dietproject.R
import com.myproject.dietproject.databinding.PersonalInfoFragmentBinding
import com.myproject.dietproject.ui.BaseFragment
import com.myproject.dietproject.ui.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonalInfoFragment : BaseFragment<PersonalInfoFragmentBinding>(R.layout.personal_info_fragment) {

    private val args by navArgs<PersonalInfoFragmentArgs>()

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

}