package com.example.healer.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil.setContentView
import com.example.healer.R
import com.example.healer.databinding.FragmentChatsBinding
import com.example.healer.repository.Repository


class ChatsFragment : Fragment() {

    private lateinit var binding: FragmentChatsBinding
    private val repo = Repository.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentChatsBinding.inflate(layoutInflater)


        val t = "0545454545"
        binding.phoneCallBTN.setOnClickListener {
            repo.makePhoneCall(requireContext(), t, Bundle())
        }
        return binding.root
    }
}