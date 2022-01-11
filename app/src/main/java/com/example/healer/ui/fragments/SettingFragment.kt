package com.example.healer.ui.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.healer.R
import com.example.healer.databinding.FragmentSettingBinding
import com.example.healer.utils.Locale


class SettingFragment : Fragment() {


    private val selectedLanguage =true


    private lateinit var binding: FragmentSettingBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        lateinit var  context :Context
        lateinit var resources :Resources

        binding = FragmentSettingBinding.inflate(layoutInflater)

        binding.dialogLanguage.setOnClickListener{
            val language :Array<String> = arrayOf("English","عربي")

            val checkedItem:Int = if (selectedLanguage){
                0
            } else{
                1
            }
            val builder :AlertDialog.Builder =AlertDialog.Builder(requireContext())
            builder.setTitle("Select a language")
                .setSingleChoiceItems(language,checkedItem) { _, which ->
                    binding.dialogLanguage.text = language[which]
                    if (language[which] == "English") {
                        context = Locale.LocaleHelper.setLocale(requireContext(), "en")
                        resources = context.resources
                        binding.textView3.text = resources.getString(R.string.language)
                    }
                    if (language[which] == "عربي") {
                        context = Locale.LocaleHelper.setLocale(requireContext(), "ar")
                        resources = context.resources
                        binding.textView3.text = resources.getString(R.string.language)
                    }
                }
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
            builder.create().show()
        }
        return binding.root
    }
}