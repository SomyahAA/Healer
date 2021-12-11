package com.example.healer

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text

class PsychologistRegisterFragment : Fragment() {

    private val PsycAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private var database: DatabaseReference = Firebase.database.reference
    private lateinit var registerPsy: Button
    private lateinit var name: TextView
    private lateinit var phoneNumber: TextView
    private lateinit var email: TextView
    private lateinit var password: TextView
    private lateinit var specialty:TextView
    private lateinit var experienceYears:TextView
    lateinit var bio:TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_psychologist_register, container, false)
        registerPsy = view.findViewById(R.id.registerPsy)
        name =view.findViewById(R.id.psyName)
        phoneNumber=view.findViewById(R.id.psyPhoneNumber)
        email=view.findViewById(R.id.psyEmail)
        password=view.findViewById(R.id.psyPassword)
        specialty=view.findViewById(R.id.psySpeciality)
        experienceYears=view.findViewById(R.id.psyExperienceYears)
        bio =view.findViewById(R.id.psyBio)
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerPsy.setOnClickListener {

            if (name.text.isEmpty()|| phoneNumber.text.isEmpty()|| email.text.isEmpty() ||password.text.isEmpty()
            || specialty.text.isEmpty()||experienceYears.text.isEmpty()||bio.text.isEmpty()){
            Toast.makeText(requireContext(), "You must fill all fields", Toast.LENGTH_SHORT).show()

        }
        else{
            val model = Psychologist(
                name.toString(), phoneNumber.toString(), email.toString(), password.toString()
                ,specialty.toString(),experienceYears.toString(), bio.toString())
            PsycAuth.createUserWithEmailAndPassword(email.toString(), password.toString())
                .addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        Log.d("healer", "createUserWithEmail:success")

                        database.child("Psychologist Information")
                            .child(PsycAuth.currentUser?.uid!!).setValue(model)
                        Toast.makeText(requireContext(), "Registration Successful", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_psychologistRegisterFragment_to_homeFragment)
                    } else {
                        Log.d("healer", "createUserWithEmail:failure", task.exception)
                        Toast.makeText(requireContext(), task.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
    }
}