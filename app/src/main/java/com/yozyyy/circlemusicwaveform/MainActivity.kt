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
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yozyyy.circlemusicwaveform.ui.theme.BlueGrey
import com.yozyyy.circlemusicwaveform.ui.theme.CircleMusicWaveformTheme
import com.yozyyy.circlemusicwaveform.ui.theme.WhiteAlpha50
import com.yozyyy.waveform.view.CircleMusicCover
import com.yozyyy.waveform.view.CircleWaveform
import com.zedalpha.shadowgadgets.compose.clippedShadow

class MainActivity : ComponentActivity() {
    private val waveformViewModel by viewModels<CircleWaveformViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        waveformViewModel.init(this)
        setContent {
            CircleMusicWaveformTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting(viewModel = waveformViewModel)
                }
            }
        }
    }
}

@Composable
fun Greeting(
    viewModel: CircleWaveformViewModel
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.bg_gradient),
            contentDescription = "",
            contentScale = ContentScale.FillBounds
        )
        Column(
            modifier = Modifier
                .width(297.dp)
                .height(499.dp)
                .clippedShadow(
                    shape = RoundedCornerShape(16.dp),
                    elevation = 30.dp,
                    spotColor = Color(0x40000000),
                    ambientColor = Color(0x40000000)
                )
                .clip(RoundedCornerShape(16.dp))
                .background(color = WhiteAlpha50)
                .padding(top = 24.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val provider = remember {
                GoogleFont.Provider(
                    providerAuthority = "com.google.android.gms.fonts",
                    providerPackage = "com.google.android.gms",
                    certificates = R.array.com_google_android_gms_fonts_certs
                )
            }
            val fontFamily = remember {
                FontFamily(
                    Font(googleFont = GoogleFont("Archivo Black"), fontProvider = provider)
                )
            }
            Text(
                text = "Circle Music Waveform",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight(400),
                    color = Color(0xFF887EC0),
                    textAlign = TextAlign.Center,
                )
            )
            Box(
                modifier = Modifier.wrapContentSize(),
                contentAlignment = Alignment.Center
            ) {
                // ============== The main UI part ================
                CircleWaveform(
                    Modifier
                        .fillMaxWidth()
                        .height(320.dp),
                    radius = 110.dp,
                    barCornerRadius = viewModel.customize.barCornerRadius.dp,
                    barWidth = viewModel.customize.barWidth.dp,
                    barHeights = viewModel.waveData.value,
                    barMinHeight = viewModel.customize.barMinHeight.dp,
                    barMaxHeight = viewModel.customize.barMaxHeight.dp,
                    barNumber = viewModel.customize.barNumber,
                    color = BlueGrey,
                    isPlaying = viewModel.isPlaying,
                    isEnd = viewModel.isEnd
                )
                CircleMusicCover(
                    Modifier
                        .width(210.dp)
                        .height(210.dp),
                    R.drawable.img_music_cover,
                    cornerRadius = 210.dp,
                    isPlaying = viewModel.isPlaying
                )
                // ============== The main UI part ================
            }
            val interactionSource = remember {
                MutableInteractionSource()
            }
            Image(
                modifier = Modifier
                    .width(72.dp)
                    .height(72.dp)
                    .clip(RoundedCornerShape(72.dp))
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null // disable the touch ripple effect
                    ) {
                        viewModel.isPlaying = !viewModel.isPlaying
                        if (viewModel.isPlaying) {
                            viewModel.play()
                        } else {
                            viewModel.pause()
                        }
                    },
                painter = painterResource(id = if (!viewModel.isPlaying) R.drawable.ic_player_pause else R.drawable.ic_player_start),
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
//    val waveformViewModel = CircleWaveformViewModel()
//    Greeting(viewModel = waveformViewModel)
}