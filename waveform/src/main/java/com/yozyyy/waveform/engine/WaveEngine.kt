package com.yozyyy.waveform.engine

import android.media.audiofx.Visualizer
import com.yozyyy.waveform.renderer.WaveRenderer

class WaveEngine(
    audioSession: Int,
    vararg targets: WaveRenderer
) : Visualizer.OnDataCaptureListener {
    private var visualizer: Visualizer
    private var waveRenderers: ArrayList<WaveRenderer> // the interface that makes views receive data and update the view
    private val waveParser: WaveParser // parse the raw data from Visualizer based on the config in WaveRenderer
    private var lastTime = 0L

    init {
        visualizer = Visualizer(audioSession).apply {
            setDataCaptureListener(
                this@WaveEngine,
                Visualizer.getMaxCaptureRate(),
                true,
                false
            )
        }
        waveRenderers = ArrayList<WaveRenderer>().apply {
            addAll(targets)
        }
        waveParser = WaveParser()
    }

    var active = false
        set(value) {
            field = value
            visualizer.enabled = value
        }

    fun appendTargets(vararg targets: WaveRenderer): WaveEngine {
        waveRenderers.addAll(targets)
        return this
    }

    fun release() {
        visualizer.apply {
            enabled = false
            release()
        }
        waveRenderers = ArrayList()
    }

    override fun onWaveFormDataCapture(
        visualizer: Visualizer?,
        waveform: ByteArray?,
        samplingRate: Int
    ) {
        if (active) {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastTime > WaveRenderer.duration) {
                lastTime = currentTime
                if (waveform != null && waveform.isNotEmpty()) {
                    for (waveRenderer in waveRenderers) {
                        val data = waveParser.parse(
                            waveform,
                            waveRenderer.customize.barMinHeight,
                            waveRenderer.customize.barMaxHeight,
                            waveRenderer.customize.barNumber
                        )
                        waveRenderer.render(data)
                    }
                }
            }
        }
    }

    override fun onFftDataCapture(visualizer: Visualizer?, fft: ByteArray?, samplingRate: Int) {}
}