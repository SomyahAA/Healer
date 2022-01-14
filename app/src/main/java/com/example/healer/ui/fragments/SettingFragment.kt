package com.example.healer.ui.fragments

import android.app.AlertDialog
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.example.healer.R
import com.example.healer.databinding.FragmentSettingBinding
import com.google.firebase.auth.FirebaseAuth
import java.util.*
import kotlin.collections.ArrayList


class SettingFragment : Fragment() {


    private val currentUser = FirebaseAuth.getInstance().currentUser


    private lateinit var binding: FragmentSettingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadLocate()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSettingBinding.inflate(layoutInflater)

        binding.deleteAcoBtn.setOnClickListener {
            deleteUserDialog()
        }

        showChangeLang()
        return binding.root
    }

    private fun deleteUserDialog() {
        val builder = AlertDialog.Builder(context).apply {
            setTitle("Are You Sure ?")
            setMessage("deleting your account will remove your account and you can't go back")
        }
        builder.setPositiveButton("YES") { _, _ ->

            currentUser?.delete()?.addOnSuccessListener {
                Toast.makeText(context, "your account is successfully deleted", Toast.LENGTH_LONG).show()

            }?.addOnFailureListener {
                Toast.makeText(context, "something goes wrong", Toast.LENGTH_LONG).show()
                Log.e("Tag", "something goes wrong", it)

            }
            builder.create().also {
                it.show()
            }
        }
        builder.setNegativeButton("NO") { dialog, _ ->
           dialog.dismiss()
        }

        builder.setNeutralButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }
            val dialog = builder.create()
        dialog.show()
    }

    private fun setLocal(language: String){

        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale

        context?.resources?.updateConfiguration(config,context?.resources?.displayMetrics)

        getDefaultSharedPreferences(context).edit()
            .putString("PREF_CHANGE_LANG_KEY",language)
            .apply()
    }

    private fun loadLocate(){
        val pref = getDefaultSharedPreferences(context)
        val language = pref.getString("PREF_CHANGE_LANG_KEY","")!!
        setLocal(language)

    }

    private fun showChangeLang () {
        val languageList = ArrayList<String>()
        languageList.add("Arabic")
        languageList.add("English")

        val adapterLanguage = ArrayAdapter(
            requireContext(),
            R.layout.support_simple_spinner_dropdown_item,
            languageList
        )
        binding.languageSpinner.adapter = adapterLanguage
        binding.languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    1 -> setLocal("ar")
                    2 -> setLocal("en")
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

}