package com.yozyyy.waveform

class CircleWaveformCustomize(
    override val barMinHeight: Int,
    override val barMaxHeight: Int,
    override val barNumber: Int, // TODO: this number must be greater than 8
    override val barWidth: Int,
    override val barCornerRadius: Int
) : WaveCustomize {
}