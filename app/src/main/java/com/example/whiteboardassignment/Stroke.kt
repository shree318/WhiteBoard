package com.example.whiteboardassignment

import android.graphics.Color
import android.graphics.PointF

data class Stroke(
    val points: MutableList<PointF>,
    val color: Int = Color.BLACK,
    val width: Float = 5f
)
