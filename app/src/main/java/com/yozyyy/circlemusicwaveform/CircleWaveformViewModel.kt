package com.yozyyy.circlemusicwaveform

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.yozyyy.waveform.WaveCustomize
import com.yozyyy.waveform.WaveEngine
import com.yozyyy.waveform.WaveRenderer

class CircleWaveformViewModel : ViewModel(), WaveRenderer, MediaPlayer.OnCompletionListener {
    private lateinit var waveEngine: WaveEngine
    private lateinit var mediaPlayer: MediaPlayer
    private val _isPlaying: MutableState<Boolean> = mutableStateOf(false)
    val isPlaying: MutableState<Boolean>
        get() = _isPlaying

    private lateinit var _waveData: MutableState<List<Int>>
    val waveData: MutableState<List<Int>>
        get() = _waveData

    private lateinit var _waveCustomize: WaveCustomize
    override val customize: WaveCustomize
        get() = _waveCustomize

    fun initMusic(context: Context, waveCustomize: WaveCustomize) {
        _waveCustomize = waveCustomize
        _waveData = mutableStateOf(List(waveCustomize.barNumber) { waveCustomize.barMinHeight })
        mediaPlayer = MediaPlayer.create(context, R.raw.breathing)
        mediaPlayer.setOnCompletionListener(this)
        waveEngine = WaveEngine(mediaPlayer.audioSessionId, this)
    }

    fun play() {
        _isPlaying.value = true
        mediaPlayer.start()
        waveEngine.active = true

    }

    fun pause() {
        _isPlaying.value = false
        mediaPlayer.pause()
        waveEngine.active = false
    }

    override fun onCleared() {
        super.onCleared()
        isPlaying.value = false
        mediaPlayer.release()
        waveEngine.active = false
        waveEngine.release()
    }

    override fun render(data: List<Int>) {
        _waveData.value = data
    }

    override fun onCompletion(mp: MediaPlayer?) {
        waveEngine.active = false
        _isPlaying.value = false
    }
}