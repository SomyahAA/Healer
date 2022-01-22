package com.example.healer.ui.fragments.appointment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.healer.databinding.FragmentDialogBinding


class ToAppointment :Fragment(){


    private lateinit var binding: FragmentDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentDialogBinding.inflate(layoutInflater)

        return binding.root
    }
}