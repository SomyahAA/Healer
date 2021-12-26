package com.example.healer.ui.fragments.home.user_home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healer.R
import com.example.healer.databinding.FragmentHomeBinding
import com.example.healer.databinding.FragmentPsyCardBinding
import com.example.healer.models.Psychologist
import com.example.healer.repository.Repository
import kotlinx.coroutines.launch


private const val TAG = "HomeFragment"
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val repo = Repository.getInstance()

    private val homeViewModel : HomeViewModel by lazy { ViewModelProvider(this)[HomeViewModel::class.java] }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        var state = false
        lifecycleScope.launch {
            state = repo.userTypeIsUser()
        }
        if (state){
            findNavController().navigate(R.id.psyHomeFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.psyLiveData().observe(
            viewLifecycleOwner, Observer{
                Log.d(TAG, "onViewCreated: $it ")
                updateUI(it)
            }
        )
    }

    private fun updateUI(psychologists: List<Psychologist>) {
        val psyAdapter = PsyAdapter(psychologists)
        binding.recyclerView.adapter = psyAdapter
    }

    private inner class PsyHolder(val binding: FragmentPsyCardBinding)
        :RecyclerView.ViewHolder(binding.root){

        fun bind(psychologist: Psychologist){
            if (!homeViewModel.userTypeIsUser()) {
                homeViewModel.psyLiveData().observe(viewLifecycleOwner){
                    Log.d("Home","and the result is .... $psychologist.name")
                    binding.sycName.text = psychologist.name
                    binding.sycSpecialty.text =psychologist.specialty
                    binding.psyExpYears.text =psychologist.experienceYears
                }
            }
        }
    }

    private inner class PsyAdapter(val psychologists: List<Psychologist>):
        RecyclerView.Adapter<PsyHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PsyHolder {
            val binding = FragmentPsyCardBinding.inflate(
                layoutInflater,
                parent,
                false
            )
            return PsyHolder(binding)
        }

        override fun onBindViewHolder(holder: PsyHolder, position: Int) {
            val psy = psychologists[position]
            holder.bind(psy)
        }

        override fun getItemCount(): Int = psychologists.size
    }

}