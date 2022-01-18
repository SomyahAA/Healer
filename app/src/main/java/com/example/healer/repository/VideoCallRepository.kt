package com.example.healer.repository

import android.content.Context
import android.util.Log
import android.view.SurfaceView
import android.view.View
import android.widget.*
import com.example.healer.R
import com.example.healer.agora.media.RtcTokenBuilder
import com.example.healer.utils.Constants.APP_ID
import com.example.healer.utils.Constants.CHANNEL
import com.example.healer.utils.Constants.appCertificate
import com.example.healer.utils.Constants.expirationTimeInSeconds
import com.example.healer.utils.Constants.uid
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine
import io.agora.rtc.video.VideoCanvas
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class VideoCallRepository {

    var mRtcEngine: RtcEngine? = null

    private fun showButtons(show: Boolean, mMuteBtn: ImageView?, mSwitchCameraBtn: ImageView?) {
        val visibility = if (show) View.VISIBLE else View.GONE
        mMuteBtn!!.visibility = visibility

        if (mSwitchCameraBtn != null) {
            mSwitchCameraBtn.visibility = visibility
        }
    }

    fun generateTokenForCall(): String {

        val token = RtcTokenBuilder()
        val timestamp: Int = (System.currentTimeMillis() / 1000 + expirationTimeInSeconds).toInt()

        val result: String = token.buildTokenWithUid(
            APP_ID, appCertificate,
            CHANNEL, uid, RtcTokenBuilder.Role.Role_Publisher, timestamp
        )
        Log.d("TAG", "startVideoCall999: $result")

        return result
    }

    fun onRemoteUserLeft(container: FrameLayout, Msg: TextView) {
        container.removeAllViews()
        Msg.visibility = View.VISIBLE
    }

    fun endCall(
        mLocalView: SurfaceView?, mLocalContainer: FrameLayout?,
        mRemoteView: SurfaceView?, mRemoteContainer: RelativeLayout?
    ) {
        removeLocalVideo(mLocalView, mLocalContainer)
        removeRemoteVideo(mRemoteView, mRemoteContainer)
        leaveChannel()
    }

    private fun removeRemoteVideo(mRemoteView: SurfaceView?, mRemoteContainer: RelativeLayout?) {
        if (mRemoteView != null) {
            mRemoteContainer?.removeView(mRemoteView)
        }
        //mRemoteView = null
    }

    private fun removeLocalVideo(mLocalView: SurfaceView?, mLocalContainer: FrameLayout?) {

        if (mLocalView != null) {
            mLocalContainer?.removeView(mLocalView)
        }
        //mLocalView = null
    }

    private fun startCall() {
        generateTokenForCall()
    }

    private fun leaveChannel() {
        mRtcEngine!!.leaveChannel()
    }

    fun onSwitchCameraClicked(view: View?) {
        mRtcEngine!!.switchCamera()
    }

    fun onLocalAudioMuteClicked(view: View?, mMuted: Boolean, mMuteBtn: ImageView?) {
        val mMuted = !mMuted
        mRtcEngine!!.muteLocalAudioStream(mMuted)
        val res = if (mMuted) R.drawable.btn_mute else R.drawable.btn_unmute
        mMuteBtn?.setImageResource(res)
    }

    fun onRemoteUserVideoMuted(uid: Int, muted: Boolean, container: FrameLayout?) {

        val surfaceView = container?.getChildAt(0) as SurfaceView

        val tag = surfaceView.tag
        if (tag != null && tag as Int == uid) {
            surfaceView.visibility = if (muted) View.GONE else View.VISIBLE
        }
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
        if (mCallEnd) {
            startCall()
//            mCallEnd = false
            mCallBtn?.setImageResource(R.drawable.btn_endcall)
        } else {
            endCall(mLocalView, mLocalContainer, mRemoteView, mRemoteContainer)
//            mCallEnd = true
            mCallBtn?.setImageResource(R.drawable.btn_startcall)
        }
        showButtons(!mCallEnd, mMuteBtn, mSwitchCameraBtn)
    }

    fun initializeAndJoinChannel(
        context: Context,
        localContainer: FrameLayout?,
        remoteContainer: FrameLayout
    ) {

        fun setupRemoteVideo(uid: Int) {

            val remoteFrame = RtcEngine.CreateRendererView(context)
            remoteFrame.setZOrderMediaOverlay(true)
            remoteContainer.addView(remoteFrame)
            mRtcEngine!!.setupRemoteVideo(
                VideoCanvas(
                    remoteFrame,
                    VideoCanvas.RENDER_MODE_FIT,
                    uid
                )
            )
        }

        val mRtcEventHandler = object : IRtcEngineEventHandler() {
            // Listen for the remote user joining the channel to get the uid of the user.
            override fun onUserJoined(uid: Int, elapsed: Int) {
                //runOnUIThread{}
                CoroutineScope(Dispatchers.Main).launch {
                    setupRemoteVideo(uid)
                }
                // Call setupRemoteVideo to set the remote video view after getting uid from the onUserJoined callback.
            }
        }

        try {
            mRtcEngine = RtcEngine.create(context, APP_ID, mRtcEventHandler)
            Log.d("TAG", "initializeAndJoinChannel: ")
        } catch (e: Exception) {
            Log.e("TAG", "initializeAndJoinChannel: ", e)
        }

        // By default, video is disabled, and you need to call enableVideo to start a video stream.
        mRtcEngine!!.enableVideo()

        // Call CreateRendererView to create a SurfaceView object and add it as a child to the FrameLayout.
        val localFrame = RtcEngine.CreateRendererView(context)
        localContainer?.addView(localFrame)
        // Pass the SurfaceView object to Agora so that it renders the local video.
        mRtcEngine!!.setupLocalVideo(VideoCanvas(localFrame, VideoCanvas.RENDER_MODE_FIT, 0))
        generateTokenForCall()
        //the uid it must match the id used to generate the token
        mRtcEngine!!.joinChannel(generateTokenForCall(), CHANNEL, "", 0)
    }

}