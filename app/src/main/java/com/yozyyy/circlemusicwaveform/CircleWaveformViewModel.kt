package com.yozyyy.circlemusicwaveform

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.yozyyy.waveform.engine.WaveEngine
import com.yozyyy.waveform.renderer.WaveRenderer
import com.yozyyy.waveform.view.CircleWaveCustomize
import com.yozyyy.waveform.view.customize.WaveCustomize

class CircleWaveformViewModel : ViewModel(), WaveRenderer, MediaPlayer.OnCompletionListener {
    private lateinit var waveEngine: WaveEngine
    private lateinit var mediaPlayer: MediaPlayer

    private var _isPlaying by mutableStateOf(false)
    var isPlaying: Boolean
        get() = _isPlaying
        set(value) {
            _isPlaying = value
        }

    private var _isEnd by mutableStateOf(false)
    val isEnd: Boolean
        get() = _isEnd

    private lateinit var _waveData: MutableState<List<Int>>
    val waveData: MutableState<List<Int>>
        get() = _waveData

    private lateinit var _waveCustomize: WaveCustomize
    override val customize: WaveCustomize
        get() = _waveCustomize

    var isGranted by mutableStateOf(true)  // True means microphone permission is granted

    fun init(context: Context) {
        _waveCustomize  = CircleWaveCustomize(12, 30, 72, 6, 6)
        _waveData = mutableStateOf(List(_waveCustomize.barNumber) { _waveCustomize.barMinHeight })
        mediaPlayer = MediaPlayer.create(context, R.raw.breathing)
        mediaPlayer.setOnCompletionListener(this)
//        waveEngine = WaveEngine(mediaPlayer.audioSessionId, this@CircleWaveformViewModel)
    }

    fun startWaveEngine() {
        waveEngine = WaveEngine(mediaPlayer.audioSessionId, this@CircleWaveformViewModel)
    }

    fun play() {
        _isPlaying = true
        if (_isEnd) _isEnd = false
        mediaPlayer.start()
        waveEngine.active = true
    }

    fun pause() {
        _isPlaying = false
        mediaPlayer.pause()
        waveEngine.active = false
    }

    override fun onCleared() {
        super.onCleared()
        isPlaying = false
        mediaPlayer.release()
        waveEngine.active = false
        waveEngine.release()
    }

    override fun render(data: List<Int>) {
        _waveData.value = data
    }

    override fun onCompletion(mp: MediaPlayer?) {
        waveEngine.active = false
        _isPlaying = false
        _isEnd = true
    }
}