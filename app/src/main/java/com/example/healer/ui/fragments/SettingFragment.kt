package com.example.healer.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.healer.R


class SettingFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        // Inflate the layout for this fragment
//        AgoraRTC.instance().bootstrap(requireContext(), "abcbccc386c6473c9455f897b2bd2555", "channel");
//        //setContentView(R.layout.fragment_chats);
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }


}