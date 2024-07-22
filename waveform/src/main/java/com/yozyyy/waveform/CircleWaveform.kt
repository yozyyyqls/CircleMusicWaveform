package com.yozyyy.waveform

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CircleWaveform(
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    alpha: Float = 0.5f,
    radius: Dp = 120.dp, // the radius of circle waveform
    barWidth: Dp = 12.dp,
    barCornerRadius: Dp = 6.dp, // the corner radius of each bar
    barNumber: Int,
    barHeights: List<Int>,
    isPlaying: Boolean = false
) {
    require(barNumber > 8) { "barNumber must be greater than 8" }
    require(barHeights.isNotEmpty()) { "barHeights list cannot be empty" }
    require(barHeights.size >= barNumber) { "barHeights list must have at least $barNumber elements" }
    require(radius >= 0.dp) { "radius must be greater than 0" }
    require(barWidth > 0.dp) { "barWidth must be greater than 0" }
    require(barCornerRadius >= 0.dp) { "barCornerRadius cannot be negative" }

    // create the animation of changing of bar height
    val animateHeight = remember { mutableStateListOf<State<Int>>() }
    repeat(barNumber) { index ->
        val animate = animateIntAsState(
            targetValue = if (isPlaying) barHeights[index] else animateHeight.getOrNull(index)?.value
                ?: barHeights[index],
            label = "",
            animationSpec = tween(WaveRenderer.duration.toInt(), easing = FastOutSlowInEasing)
        )
        animateHeight.add(animate)
    }

    // precompute the angles to avoid repeated calculations
    val angleBetweenBar = 360f / barNumber.toFloat()
    val angles = remember(barNumber) { List(barNumber) { index -> index * angleBetweenBar } }
    Canvas(
        modifier = modifier
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val centerX = canvasWidth / 2
        val centerY = canvasHeight / 2

        // drawing each bar based on the given height
        barHeights.take(barNumber).forEachIndexed { index, _ ->
            val degree = angles[index]
            val height = animateHeight[index].value.dp
            rotate(degrees = degree, pivot = Offset(centerX, centerY)) {
                drawRoundRect(
                    color = color,
                    topLeft = Offset(
                        centerX - barWidth.toPx() / 2,
                        centerY - radius.toPx() - height.toPx()
                    ),
                    size = Size(barWidth.toPx(), height.toPx()),
                    cornerRadius = CornerRadius(
                        barCornerRadius.toPx(),
                        barCornerRadius.toPx()
                    ),
                    alpha = alpha
                )
            }
        }
    }
}

@Composable
fun CircleMusicCover(
    modifier: Modifier = Modifier,
    @DrawableRes imageId: Int,
    cornerRadius: Dp = 0.dp,
    duration: Int = 8000,
    isPlaying: Boolean = false,
    rotatable: Boolean = true // if enable it, the image will rotate infinitely
) {
    val angle = remember { Animatable(0f) }
    if (rotatable) {
        // launcher a coroutine when isPlaying changes
        LaunchedEffect(isPlaying) {
            if (isPlaying) {
                angle.animateTo(
                    targetValue = angle.value + 360f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(duration, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    )
                )
            } else {
                angle.stop()
            }
        }
    }

    Image(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .rotate(angle.value),
        painter = painterResource(id = imageId),
        contentDescription = ""
    )
}
