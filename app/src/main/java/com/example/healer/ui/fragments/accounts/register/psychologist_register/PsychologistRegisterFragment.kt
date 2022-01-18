package com.example.healer.ui.fragments.accounts.register.psychologist_register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.healer.R
import com.example.healer.databinding.FragmentPsychologistRegisterBinding
import com.example.healer.models.Psychologist
import com.example.healer.utils.OTPFragment
import com.google.firebase.auth.FirebaseAuth


class PsychologistRegisterFragment : Fragment() {

    private lateinit var binding: FragmentPsychologistRegisterBinding
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val psyRegisterVM: PsyRegisterVM by lazy { ViewModelProvider(this)[PsyRegisterVM::class.java] }
    private lateinit var gender :String

    val opt= OTPFragment()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentPsychologistRegisterBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val genderItems = arrayOf(getString(R.string.Female),getString(R.string.male))

        val genderItemsAdapter = ArrayAdapter(requireContext(),R.layout.gender_item,genderItems)
        binding.psyGender.setAdapter(genderItemsAdapter)
        binding.psyGender.setOnClickListener { AdapterView.OnItemClickListener{ parent, view, position, id ->
        gender =parent.getItemAtPosition(position).toString() } }
        binding.registerPsy.setOnClickListener {

            if (binding.psyName.text.toString().isEmpty() || binding.psyPhoneNumber1.text.toString().isEmpty() || binding.psyEmail.text.toString().isEmpty() || binding.psyPassword.text.toString().isEmpty()
                || binding.psySpeciality.text.toString().isEmpty() || binding.psyExperienceYears.text.toString().isEmpty() || binding.psyBio.text.toString().isEmpty()
            ) {
                Toast.makeText(requireContext(), "You must fill all fields", Toast.LENGTH_SHORT)
                    .show()

            } else {//"+966"+
                //opt.startPhoneNumberVerification(binding.psyPhoneNumber1.text.toString(),requireContext(),requireActivity())
                val psychologistModel = Psychologist(
                    binding.psyName.text.toString(),
                    binding.psyPhoneNumber1.text.toString(),
                    binding.psyEmail.text.toString(),
                    binding.psySpeciality.text.toString(),
                    binding.psyExperienceYears.text.toString(),
                    binding.psyBio.text.toString(),
                    "https://firebasestorage.googleapis.com/v0/b/healer-a388a.appspot.com/o/photos%2Fdefault_profile_picture.jpeg?alt=media&token=abc744b8-bfee-4b5c-8e3f-b7b430e9ac85"
                )

                psyRegisterVM.registerPsychologist(
                    binding.psyEmail.text.toString(),
                    binding.psyPassword.text.toString(),
                    psychologistModel,
                    requireContext()
                )
                if (auth.currentUser != null) {
                    psyRegisterVM.updatePsyId(auth.currentUser!!.uid)
                    findNavController().navigate(R.id.action_psychologistRegisterFragment_to_psyHomeFragment)
                }
            }
        }

    }

}