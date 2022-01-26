package com.example.healer.ui.fragments.accounts.profile.user_profile

import android.app.Activity.RESULT_OK
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
import com.example.healer.databinding.FragmentProfileBinding
import com.example.healer.utils.Constants.PICK_PHOTO
import java.lang.Exception


class UserProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val userProfileVM: UserProfileVM by lazy { ViewModelProvider(this)[UserProfileVM::class.java] }
    private lateinit var imageUri: Uri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userProfileVM.readUserDataFromFirestore().observe(viewLifecycleOwner) { user ->
            binding.userName.setText(user.name)
            binding.userPhoneNumber.setText(user.phoneNumber)
            binding.userGender.setText(user.gender)
            binding.profileImage.load(user.profileImage)
        }

        binding.profileImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_PHOTO)
        }

        binding.updateBTN.setOnClickListener {
            userProfileVM.updateUserProfile(
                binding.userName.text.toString(),
                binding.userGender.text.toString(),
                binding.userPhoneNumber.text.toString()
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_PHOTO && resultCode == RESULT_OK) {
            if (data != null) {
                imageUri = data.data!!
                try {
                    val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(
                        activity?.contentResolver, imageUri
                    )
                    binding.profileImage.setImageBitmap(bitmap)
                    userProfileVM.uploadPhotoToFirebaseStorage(imageUri)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}