package com.example.healer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController


class UserOrPsyFragment : Fragment() {

    lateinit var psychologistReg: Button
    lateinit var userReg: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_user_or_psy, container, false)
        psychologistReg = view.findViewById(R.id.gotoPsychologistReg)
        userReg= view.findViewById(R.id.gotoUserReg)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        psychologistReg.setOnClickListener {
            findNavController().navigate(R.id.action_userOrPsyFragment_to_psychologistRegisterFragment)
        }
        userReg.setOnClickListener {
            findNavController().navigate(R.id.action_userOrPsyFragment_to_userRegisterFragment)
        }
    }
}