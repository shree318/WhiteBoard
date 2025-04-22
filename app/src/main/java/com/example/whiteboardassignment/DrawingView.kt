package com.example.whiteboardassignment

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

class DrawingView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private lateinit var viewModel: WhiteboardViewModel
    private val paint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 5f
        style = Paint.Style.STROKE
        isAntiAlias = true
    }

    fun setViewModel(vm: WhiteboardViewModel) {
        viewModel = vm
        (context as? LifecycleOwner)?.let { owner ->
            viewModel.paths.observe(owner, Observer { invalidate() })
            viewModel.shapes.observe(owner, Observer { invalidate() })
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw all strokes
        for (stroke in viewModel.paths.value ?: emptyList()) {
            paint.color = stroke.color
            paint.strokeWidth = stroke.width
            for (i in 1 until stroke.points.size) {
                canvas.drawLine(
                    stroke.points[i - 1].x,
                    stroke.points[i - 1].y,
                    stroke.points[i].x,
                    stroke.points[i].y,
                    paint
                )
            }
        }

        // Draw all shapes
        for (shape in viewModel.shapes.value ?: emptyList()) {
            drawShape(canvas, shape)
        }

        // Draw active shape
        viewModel.currentShape?.let {
            paint.color = it.color
            paint.strokeWidth = it.width
            drawShape(canvas, it)
        }
    }

    private fun drawShape(canvas: Canvas, shape: Shape) {
        val left = Math.min(shape.start.x, shape.end.x)
        val top = Math.min(shape.start.y, shape.end.y)
        val right = Math.max(shape.start.x, shape.end.x)
        val bottom = Math.max(shape.start.y, shape.end.y)
        val rect = RectF(left, top, right, bottom)

        paint.color = shape.color
        paint.strokeWidth = shape.width
        paint.style = Paint.Style.STROKE

        when (shape.type) {
            "rectangle" -> canvas.drawRect(rect, paint)
            "circle" -> canvas.drawOval(rect, paint)
            "line" -> canvas.drawLine(shape.start.x, shape.start.y, shape.end.x, shape.end.y, paint)
            "polygon" -> drawPolygon(canvas, shape)
        }
    }

    private fun drawPolygon(canvas: Canvas, shape: Shape) {
        val path = Path()
        val sides = 5 // Can be parameterized
        val cx = (shape.start.x + shape.end.x) / 2
        val cy = (shape.start.y + shape.end.y) / 2
        val radius = Math.min(
            Math.abs(shape.end.x - shape.start.x),
            Math.abs(shape.end.y - shape.start.y)
        ) / 2
        for (i in 0..sides) {
            val angle = Math.toRadians((360.0 / sides) * i)
            val x = (cx + radius * Math.cos(angle)).toFloat()
            val y = (cy + radius * Math.sin(angle)).toFloat()
            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        path.close()
        canvas.drawPath(path, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                viewModel.startStroke(x, y)
                invalidate()
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                viewModel.addPoint(x, y)
                invalidate()
                return true
            }
            MotionEvent.ACTION_UP -> {
                viewModel.endStroke()
                invalidate()
                return true
            }
        }
        return false
    }
}

