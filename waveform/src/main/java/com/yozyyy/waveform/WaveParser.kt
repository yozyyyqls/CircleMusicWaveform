package com.yozyyy.waveform

import android.util.Log
import io.vacco.savitzkygolay.SgFilter
import kotlin.math.roundToInt
import kotlin.math.sqrt

/**
 * @author CHIU
 * Parse the raw data from Visualizer based on the config in WaveRenderer
 */
class WaveParser {

    fun parse(toParse: ByteArray, viewMin: Int, viewMax: Int, chunkNum: Int): List<Int> {
        if (chunkNum <= 0) {
            throw IllegalArgumentException("chunkNum must be greater than 0")
        }
        if (toParse.isEmpty()) {
            return emptyList()
        }
        // 1. Chunk the raw data given by the visualizer
        val chunked = chunk(toParse, chunkNum)
        Log.d(TAG, "chunked=${chunked.toList()}")

        // 2. Smooth the data
        val xs = FloatArray(chunked.size)
        val out = FloatArray(chunked.size)
        for (i in chunked.indices) {
            xs[i] = i.toFloat()
        }
        SgFilter(9).process(chunked, xs, out)
        Log.d(TAG, "sg-filter=${out.toList()}")

        // 3. Makes each raw data falls into the range of [viewMin, viewMax]
        val parsed: List<Int> = normalizeData(out, viewMin, viewMax)
        Log.d(TAG, "waveform=$parsed")
        return parsed
    }

    private fun chunk(toParse: ByteArray, chunkNum: Int): FloatArray {
        if (toParse.isEmpty()) {
            return FloatArray(0)
        }

        val result = FloatArray(chunkNum)
        val chunkSize = toParse.size / chunkNum
        for (i in 0 until chunkNum) {
            var sum = 0f
            val index = i * chunkSize
            for (j in index until index + chunkSize) {
                sum += toParse[j].toFloat()
            }
            val p0 = toParse[index].toFloat()
            val p1 = sum / chunkSize
            result[i] = (p0 + (p1 - p0) * sqrt(0.25f))
        }
        return result
    }

    private fun normalizeData(data: FloatArray, viewMin: Int, viewMax: Int): List<Int> {
        val rawMin = -128f
        val rawMax = 127f

        // Normalize the data to the [viewMin, viewMax] range
        return data.map { value ->
            ((value - rawMin) / (rawMax - rawMin) * (viewMax.toFloat() - viewMin.toFloat()) + viewMin.toFloat()).roundToInt()
        }
    }

    companion object {
        val TAG = WaveParser::class.simpleName
    }
}