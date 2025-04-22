package com.example.whiteboardassignment

import android.graphics.Color
import android.graphics.PointF
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

enum class Mode {
    DRAW, ERASE, RECTANGLE, CIRCLE, LINE, POLYGON
}

class WhiteboardViewModel : ViewModel() {

    private val _paths = MutableLiveData<MutableList<Stroke>>(mutableListOf())
    val paths: LiveData<MutableList<Stroke>> get() = _paths

    private val _shapes = MutableLiveData<MutableList<Shape>>(mutableListOf())
    val shapes: LiveData<MutableList<Shape>> get() = _shapes

    var currentMode: Mode = Mode.DRAW
    var strokeColor: Int = Color.BLACK
    var strokeWidth: Float = 5f

    private var currentStroke: Stroke? = null
    var currentShape: Shape? = null
        private set

    fun startStroke(x: Float, y: Float) {
        when (currentMode) {
            Mode.DRAW, Mode.ERASE -> {
                val color = if (currentMode == Mode.ERASE) Color.WHITE else strokeColor
                currentStroke = Stroke(mutableListOf(PointF(x, y)), color, strokeWidth)
            }
            Mode.RECTANGLE, Mode.CIRCLE, Mode.LINE, Mode.POLYGON -> {
                currentShape = Shape(
                    type = currentMode.name.lowercase(),
                    start = PointF(x, y),
                    end = PointF(x, y),
                    color = strokeColor,
                    width = strokeWidth
                )
            }
        }
    }

    fun addPoint(x: Float, y: Float) {
        when (currentMode) {
            Mode.DRAW, Mode.ERASE -> {
                currentStroke?.points?.add(PointF(x, y))
            }
            Mode.RECTANGLE, Mode.CIRCLE, Mode.LINE, Mode.POLYGON -> {
                currentShape?.end = PointF(x, y)
            }
        }
    }

    fun endStroke() {
        when (currentMode) {
            Mode.DRAW, Mode.ERASE -> {
                currentStroke?.let {
                    _paths.value?.add(it)
                    _paths.value = _paths.value
                }
                currentStroke = null
            }
            Mode.RECTANGLE, Mode.CIRCLE, Mode.LINE, Mode.POLYGON -> {
                currentShape?.let {
                    _shapes.value?.add(it)
                    _shapes.value = _shapes.value
                }
                currentShape = null
            }
        }
    }

    fun clearCanvas() {
        _paths.value?.clear()
        _shapes.value?.clear()
        _paths.value = _paths.value
        _shapes.value = _shapes.value
    }

    fun getAllData(): List<Stroke> = _paths.value ?: listOf()
    fun loadData(data: List<Stroke>) {
        _paths.value = data.toMutableList()
    }
}

