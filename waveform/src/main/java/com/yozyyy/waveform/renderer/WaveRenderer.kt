package com.yozyyy.waveform.renderer

import com.yozyyy.waveform.view.customize.WaveCustomize

interface WaveRenderer {
    val customize: WaveCustomize
    fun render(data: List<Int>)

    companion object {
        const val duration: Long = 180L
    }
}