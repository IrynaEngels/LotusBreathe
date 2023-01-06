package com.example.lotusbreathe

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.airbnb.lottie.compose.*
import com.example.lotusbreathe.ui.theme.LotusBreatheTheme
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    val viewModel: AnimationControllerViewModel = AnimationControllerViewModel()

    @OptIn(ObsoleteCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LotusBreatheTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val isPlaying by viewModel.animationPlayingFlow.collectAsState()
                    val animationProgress = remember { mutableStateOf(0f) }
                    val tickerChannel = ticker(delayMillis = 1000, initialDelayMillis = 0)

                    Log.d("IRA", "isplaying + $isPlaying")

                    if (isPlaying==AnimationState.PLAYING)
                        ChangeAnimationState(AnimationState.PAUSED, tickerChannel)

                    if (isPlaying==AnimationState.PAUSED)
                        ChangeAnimationState(AnimationState.PLAYING, tickerChannel)


                    Column() {
                        Loader(isPlaying==AnimationState.PLAYING, 1.05f){
                            animationProgress.value = it
                        }
                        Button(onClick = {
                            viewModel.setAnimationPlayingValue(AnimationState.PLAYING) }, ) {
                            Text("Start")
                        }
                    }
                    

                }
            }
        }
    }

    @Composable
    fun ChangeAnimationState(animationState: AnimationState, tickerChannel: ReceiveChannel<Unit>){
        LaunchedEffect(key1 = Unit) {
            // Enter animation
            coroutineScope {
                launch {
                    var count = 0
                    for (event in tickerChannel) {

                        count++
                        if (count == 4) {
                            viewModel.setAnimationPlayingValue(animationState)
                        }
                    }
                }
            }
        }
    }
}




@Composable
fun Loader(isPlaying: Boolean, speed: Float, updateProgress: (progress: Float) -> Unit) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.blue_lotus_flower))
    val progress by animateLottieCompositionAsState(composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = isPlaying,
        speed = speed,
        restartOnPlay = false)

    Log.d("IRA", "progress = $progress")
    LottieAnimation(
        composition = composition,
        progress = {
            updateProgress(progress)
            progress },
    )
}