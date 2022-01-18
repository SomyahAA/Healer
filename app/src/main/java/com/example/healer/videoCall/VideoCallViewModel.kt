package com.example.healer.videoCall

import android.app.Application
import android.content.Context
import android.view.SurfaceView
import android.view.View
import android.widget.*
import androidx.lifecycle.AndroidViewModel
import com.example.healer.repository.VideoCallRepository

class VideoCallViewModel(application: Application) : AndroidViewModel(application){

    private val videoRepo = VideoCallRepository()

    private fun onRemoteUserLeft(container: FrameLayout, Msg: TextView) {
        videoRepo.onRemoteUserLeft(container, Msg)
    }

    fun endCall(
        mLocalView: SurfaceView?, mLocalContainer: FrameLayout?,
        mRemoteView: SurfaceView?, mRemoteContainer: RelativeLayout?
    ) {
        videoRepo.endCall(mLocalView, mLocalContainer, mRemoteView, mRemoteContainer)
    }

    fun onRemoteUserVideoMuted(uid: Int, muted: Boolean, container: FrameLayout?){
        videoRepo.onRemoteUserVideoMuted(uid,muted,container)
    }

    fun onLocalAudioMuteClicked(view: View?, mMuted: Boolean, mMuteBtn: ImageView?) {
        videoRepo.onLocalAudioMuteClicked(view, mMuted, mMuteBtn)
    }

    fun onSwitchCameraClicked(view: View?) {
        videoRepo.onSwitchCameraClicked(view)
    }

    fun onCallClicked(
        view: View?,
        mCallBtn: ImageView?,
        mCallEnd: Boolean,
        mLocalView: SurfaceView?,
        mLocalContainer: FrameLayout?,
        mMuteBtn: ImageView?,
        mSwitchCameraBtn: ImageView?,
        mRemoteView: SurfaceView?,
        mRemoteContainer: RelativeLayout?
    ) {

        videoRepo.onCallClicked(
            view,
            mCallBtn,
            mCallEnd,
            mLocalView,
            mLocalContainer,
            mMuteBtn,
            mSwitchCameraBtn,
            mRemoteView,
            mRemoteContainer
        )
    }

    fun generateTokenForCall(): String {
        return videoRepo.generateTokenForCall()
    }

    fun initializeAndJoinChannel(context: Context, localContainer: FrameLayout?,remoteContainer:FrameLayout) {
        return videoRepo.initializeAndJoinChannel(context, localContainer , remoteContainer)
    }
}