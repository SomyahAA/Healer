package com.example.healer.ui.fragments.accounts.profile.psychologist_profile

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.example.healer.databinding.FragmentPsychologistProfileBinding
import com.example.healer.utils.Constants
import java.lang.Exception


class PsychologistProfileFragment : Fragment() {

    private lateinit var binding: FragmentPsychologistProfileBinding
    private lateinit var imageUri: Uri


    private val psychologistProfileVM: PsychologistProfileVM by lazy { ViewModelProvider(this)[PsychologistProfileVM::class.java] }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPsychologistProfileBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        psychologistProfileVM.readPsyDataFromFirestore().observe(viewLifecycleOwner) { psy ->

            binding.psyName.setText(psy.name)
            binding.psyPhoneNumber.setText(psy.phoneNumber)
            binding.psyExperienceYears.setText(psy.experienceYears)
            binding.psySpecialty.setText(psy.specialty)
            binding.psyBio.setText(psy.bio)
            binding.profileImage.load(psy.profileImage)
        }

        binding.profileImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            startActivityForResult(
                Intent.createChooser(intent, "Select Photo"),
                Constants.PICK_PHOTO
            )
        }
        binding.updateBTN.setOnClickListener {
            psychologistProfileVM.updatePsyProfile(binding.psyName.toString(),
            binding.psySpecialty.toString(),binding.psySpecialty.toString(),binding.psyBio.toString(),binding.psyExperienceYears.toString())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.PICK_PHOTO && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                imageUri = data.data!!
                try {
                    val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(
                        activity?.contentResolver, imageUri
                    )
                    binding.profileImage.setImageBitmap(bitmap)
                    psychologistProfileVM.uploadPhotoToFirebaseStorage(imageUri)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

}