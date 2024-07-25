package com.yozyyy.waveform.view

import com.yozyyy.waveform.view.customize.WaveCustomize

class CircleWaveCustomize(
    override val barMinHeight: Int,
    override val barMaxHeight: Int,
    override val barNumber: Int,
    override val barWidth: Int,
    override val barCornerRadius: Int
) : WaveCustomize {
    init {
        require(barNumber > 8) { "barNumber must be greater than 8"}
    }
}