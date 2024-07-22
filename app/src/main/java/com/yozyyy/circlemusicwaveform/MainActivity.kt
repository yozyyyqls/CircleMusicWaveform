package com.yozyyy.circlemusicwaveform

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yozyyy.circlemusicwaveform.ui.theme.BlueGrey
import com.yozyyy.circlemusicwaveform.ui.theme.CircleMusicWaveformTheme
import com.yozyyy.circlemusicwaveform.ui.theme.WhiteAlpha50
import com.yozyyy.waveform.CircleMusicCover
import com.yozyyy.waveform.CircleWaveform
import com.yozyyy.waveform.CircleWaveformCustomize
import com.yozyyy.waveform.WaveCustomize
import com.zedalpha.shadowgadgets.compose.clippedShadow

class MainActivity : ComponentActivity() {
    private val waveformViewModel by viewModels<CircleWaveformViewModel>()
    private lateinit var waveformCustomize: WaveCustomize
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        setContent {
            CircleMusicWaveformTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting(viewModel = waveformViewModel, waveCustomize = waveformCustomize)
                }
            }
        }
    }

    private fun init() {
        waveformCustomize = CircleWaveformCustomize(12, 30, 72, 6, 6)
        waveformViewModel.initMusic(this, waveformCustomize)
    }

    override fun onDestroy() {
        super.onDestroy()
        waveformViewModel
    }
}

@Composable
fun Greeting(
    modifier: Modifier = Modifier,
    viewModel: CircleWaveformViewModel,
    waveCustomize: WaveCustomize
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.bg_gradient),
            contentDescription = "",
            contentScale = ContentScale.FillBounds
        )
        Column(
            modifier = Modifier
                .width(300.dp)
                .height(500.dp)
                .clippedShadow(shape = RoundedCornerShape(16.dp), elevation = 30.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(color = WhiteAlpha50)
                .padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Circle Music Waveform")
            Box(
                modifier = Modifier.wrapContentSize(),
                contentAlignment = Alignment.Center
            ) {
                CircleWaveform(
                    Modifier
                        .width(320.dp)
                        .height(320.dp),
                    radius = 110.dp,
                    barCornerRadius = waveCustomize.barCornerRadius.dp,
                    barWidth = waveCustomize.barWidth.dp,
                    barHeights = viewModel.waveData.value,
                    barNumber = waveCustomize.barNumber,
                    color = BlueGrey,
                    isPlaying = viewModel.isPlaying.value
                )
                CircleMusicCover(
                    Modifier
                        .width(210.dp)
                        .height(210.dp),
                    R.drawable.img_music_cover,
                    cornerRadius = 210.dp,
                    isPlaying = viewModel.isPlaying.value
                )
            }
            val interactionSource = remember {
                MutableInteractionSource()
            }
            Image(
                modifier = Modifier
                    .width(72.dp)
                    .height(72.dp)
                    .clip(RoundedCornerShape(72.dp))
                    .clickable(interactionSource = interactionSource, indication = null) { // disable the touch ripple effect
                        viewModel.isPlaying.value = !viewModel.isPlaying.value
                        if (viewModel.isPlaying.value) {
                            viewModel.play()
                        } else {
                            viewModel.pause()
                        }
                    },
                painter = painterResource(id = if (!viewModel.isPlaying.value) R.drawable.ic_player_pause else R.drawable.ic_player_start),
                contentDescription = "",
            )
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=1080px,height=2244px,dpi=480"
)
@Composable
fun GreetingPreview() {
    val waveformViewModel = CircleWaveformViewModel()
    val waveformCustomize = CircleWaveformCustomize(12, 30, 72, 6, 6)
    Greeting(viewModel = waveformViewModel, waveCustomize = waveformCustomize)
}