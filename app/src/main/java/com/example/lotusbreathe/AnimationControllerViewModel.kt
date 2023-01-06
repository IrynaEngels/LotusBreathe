package com.example.lotusbreathe

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class AnimationControllerViewModel : ViewModel() {
    val animationPlayingFlow: MutableStateFlow<AnimationState>
    = MutableStateFlow(AnimationState.NOT_STARTED)

    fun setAnimationPlayingValue(isPlaying: AnimationState){
        Log.d("IRA", "isPlaying = ${isPlaying}")
        animationPlayingFlow.value = isPlaying
    }
}

enum class AnimationState {
    NOT_STARTED, PLAYING, PAUSED
}