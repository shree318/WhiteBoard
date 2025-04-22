import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

import java.util.Collections.emptyList
import com.example

class DrawingView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private lateinit var viewModel: WhiteboardViewModel
    private val paint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 5f
        style = Paint.Style.STROKE
        isAntiAlias = true
    }
    private val path = Path()

    fun setViewModel(vm: WhiteboardViewModel) {
        viewModel = vm
        vm.paths.observe(context as LifecycleOwner) {
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
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
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                viewModel.startStroke(x, y)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                viewModel.addPoint(x, y)
                invalidate()
                return true
            }
            MotionEvent.ACTION_UP -> {
                viewModel.endStroke()
                return true
            }
        }
        return false
    }
}
