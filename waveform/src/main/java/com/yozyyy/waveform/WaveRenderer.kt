package com.yozyyy.waveform

import com.yozyyy.waveform.WaveCustomize

interface WaveRenderer {
    val customize: WaveCustomize
    fun render(data: List<Int>)

    companion object {
        const val duration: Long = 180L
    }
}