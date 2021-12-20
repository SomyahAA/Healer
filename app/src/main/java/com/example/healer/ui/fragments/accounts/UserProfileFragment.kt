package com.example.healer.ui.fragments.accounts

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.healer.databinding.FragmentProfileBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


//private val PICK_IMAGE_REQUEST = 71
//private var filePath: Uri? = null


class UserProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    //private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val userProfileVM :UserProfileVM by lazy { ViewModelProvider(this)[UserProfileVM::class.java] }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater)
        var firebaseStorage= FirebaseStorage.getInstance()


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


            userProfileVM.readUserDataFromFirestore().observe(viewLifecycleOwner){user->
               binding.userName.setText(user.name)
               binding.userPhoneNumber.setText(user.phoneNumber)
               binding.userGender.setText(user.gender)

        }
    }
}

//        firebaseStore = FirebaseStorage.getInstance()
//        storageReference = FirebaseStorage.getInstance().reference
//
//        binding.profileImage.setOnClickListener { launchGallery()
//            uploadImage()}


//    private fun uploadImage(){
//    }
//
//    private fun launchGallery() {
//        val intent = Intent()
//        intent.type = "image/*"
//        intent.action = Intent.ACTION_GET_CONTENT
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
//    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
//            if(data == null || data.data == null){
//                return
//            }
//
//            filePath = data.data
//            try {
//                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
//                binding.profileImage.setImageBitmap(bitmap)
//
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//        }
//    }




