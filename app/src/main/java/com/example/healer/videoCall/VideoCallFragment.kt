package com.example.healer.videoCall

import android.Manifest.permission.*
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.healer.databinding.VideoCallFragmentBinding
import com.example.healer.utils.Constants.PERMISSION_REQ_ID_CAMERA
import com.example.healer.utils.Constants.PERMISSION_REQ_ID_RECORD_AUDIO
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import io.agora.rtc.RtcEngine
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.EasyPermissions.onRequestPermissionsResult
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast

import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat

import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContentProviderCompat
import androidx.core.content.ContentProviderCompat.requireContext

import java.util.ArrayList
import java.util.jar.Manifest


class VideoCallFragment : Fragment() {

    private lateinit var binding: VideoCallFragmentBinding

    private val viewModel: VideoCallViewModel by lazy { ViewModelProvider(this)[VideoCallViewModel::class.java] }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = VideoCallFragmentBinding.inflate(layoutInflater)

        makeVideoCall()

        viewModel.initializeAndJoinChannel(
            requireContext(),
            binding.localVideoViewContainer, binding.remoteVideoViewContainer)

        return binding.root
    }

    private fun makeVideoCall() {
        checkForPermission(CAMERA,"camera",PERMISSION_REQ_ID_CAMERA)
        checkForPermission(RECORD_AUDIO,"audio", PERMISSION_REQ_ID_RECORD_AUDIO)
        viewModel.initializeAndJoinChannel(
            requireContext(),
            binding.localVideoViewContainer, binding.remoteVideoViewContainer)
    }

    private fun checkForPermission(permission: String ,name:String ,requestCode:Int){
       when {
           ContextCompat.checkSelfPermission(requireContext(),permission) == PackageManager.PERMISSION_GRANTED -> {
               Log.d("permission","$name permission granted")
           }
           shouldShowRequestPermissionRationale(permission) -> showDialog(permission,name ,requestCode)

           else -> requestPermissions(requireActivity(), arrayOf(permission),requestCode)
       }
    }

    private fun showDialog(permission: String, name: String, requestCode: Int) {

        val builder = AlertDialog.Builder(requireContext())
        builder.apply {
            setMessage(" permission to access your $name is required to use this app")
            setTitle("Permission required ")
            setPositiveButton("Ok"){ dialog ,which ->
                requestPermissions(requireActivity(),arrayOf(permission),requestCode)
            }
        }
        val dialog :AlertDialog =builder.create()
        dialog.show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        fun innerCheck(name: String){
            if (grantResults.isEmpty() || grantResults [0] != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(requireContext(),"$name permission refused ",Toast.LENGTH_LONG).show()
            }
            else {
                Toast.makeText(requireContext(), "$name permission granted ", Toast.LENGTH_LONG)
                    .show()
            }
        }
        when(requestCode){
            PERMISSION_REQ_ID_CAMERA -> innerCheck("camera")
            PERMISSION_REQ_ID_RECORD_AUDIO ->innerCheck("audio")
        }
    }

}

