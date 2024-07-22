package com.yozyyy.circlemusicwaveform

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yozyyy.circlemusicwaveform.ui.theme.CircleMusicWaveformTheme
import com.yozyyy.waveform.CircleMusicCover
import com.yozyyy.waveform.CircleWaveform

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CircleMusicWaveformTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=1080px,height=2244px,dpi=480"
)
@Composable
fun GreetingPreview() {
    CircleMusicWaveformTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val isPlaying = remember {
                mutableStateOf(false)
            }
            Box(modifier = Modifier.wrapContentSize(), contentAlignment = Alignment.Center) {
                val barHeights = remember {
                    mutableStateOf(List<Int>(360 / 5) { 16 })
                }
                CircleWaveform(
                    Modifier
                        .width(320.dp)
                        .height(320.dp)
                        .background(Color.Black),
                    radius = 110.dp,
                    barCornerRadius = 6.dp,
                    barWidth = 6.dp,
                    barHeights = barHeights.value,
                    barNumber = 72
                )
                CircleMusicCover(
                    Modifier
                        .width(210.dp)
                        .height(210.dp),
                    R.drawable.img_music_cover,
                    cornerRadius = 210.dp
                )
            }
            Text(text = "click me", Modifier.clickable(enabled = true) {
                isPlaying.value = !isPlaying.value
            })
        }
    }
}