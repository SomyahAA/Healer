package com.example.healer.ui.fragments.setting

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.healer.R
import com.example.healer.databinding.FragmentSettingBinding
import com.example.healer.ui.main.MainActivity
import com.example.healer.utils.Constants.MY_SHARED_PREF
import com.example.healer.utils.Constants.Setting_Fragment_TAG
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.util.*


class SettingFragment : Fragment() {

    private val currentUser = FirebaseAuth.getInstance().currentUser
    private lateinit var binding: FragmentSettingBinding
    private val settingVM: SettingVM by lazy { ViewModelProvider(this)[SettingVM::class.java] }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSettingBinding.inflate(layoutInflater)

        binding.deleteAcoBtn.setOnClickListener {
            deleteUserDialog()
        }
        binding.changeLanguageBTN.setOnClickListener {
            showPopupMenu()
        }
        binding.getSupport.setOnClickListener {
            settingVM.sendEmail(requireContext(),Bundle())
        }
        return binding.root
    }

    private fun deleteUserDialog() {
        val builder = AlertDialog.Builder(context).apply {
            setTitle(getString(R.string.delete_user_title))
            setMessage(getString(R.string.delete_user_message))
        }
        builder.setPositiveButton(getString(R.string.delete)) { dialog, _ ->

            binding.ProgressBar.visibility = View.VISIBLE
            currentUser?.delete()?.addOnSuccessListener {
                binding.ProgressBar.visibility = View.GONE
                lifecycleScope.launch {
                    settingVM.deleteAccount()
                }
                Toast.makeText(context, getString(R.string.deleted_successfully), Toast.LENGTH_LONG)
                    .show()
                dialog.cancel()

            }?.addOnFailureListener {
                binding.ProgressBar.visibility = View.GONE
                Toast.makeText(context, getString(R.string.Error_deleting), Toast.LENGTH_LONG)
                    .show()
                Log.e(Setting_Fragment_TAG, getString(R.string.Error_deleting), it)
            }
            builder.create().also {
                it.show()
            }
        }

        builder.setNeutralButton(getString(R.string.cancel)) { dialog, _ ->
            dialog.cancel()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun setApplicationLanguage(language: String) {
        val sharedPreferences =
            requireActivity().getSharedPreferences(MY_SHARED_PREF, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(getString(R.string.language), language)
        editor.apply()
        val res: Resources = requireActivity().resources
        val display: DisplayMetrics = res.displayMetrics
        val configuration: Configuration = res.configuration
        configuration.locale = Locale(language)
        res.updateConfiguration(configuration, display)
        val refresh = Intent(activity, MainActivity::class.java)
        refresh.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        requireActivity().startActivity(refresh)
    }

    private fun showPopupMenu() {
        binding.changeLanguageBTN.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), binding.changeLanguageBTN)
            popupMenu.menuInflater.inflate(R.menu.language_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.arabic -> {
                        setApplicationLanguage("ar")
                        val res = context?.resources
                        val config = res?.configuration
                        config?.locale = Locale("ar")
                        config?.setLayoutDirection(Locale("ar"))
                    }
                    R.id.english -> {
                        setApplicationLanguage("en")
                        val res = context?.resources
                        val config = res?.configuration
                        config?.locale = Locale("en")
                        config?.setLayoutDirection(Locale("en"))
                    }
                }
                true
            })
            popupMenu.show()
        }
    }

}