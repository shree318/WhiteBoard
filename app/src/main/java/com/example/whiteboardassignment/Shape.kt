package com.example.whiteboardassignment

import android.graphics.PointF

data class Shape(
    val type: String,              // rectangle, circle, line, polygon
    var start: PointF,            // starting point (touch down)
    var end: PointF,              // ending point (drag or release)
    val color: Int,               // shape color
    val width: Float = 5f         // stroke width
)