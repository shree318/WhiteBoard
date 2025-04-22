package com.example.whiteboardassignment

import android.graphics.Color
import android.os.Bundle
import android.os.FileUtils
import android.widget.SeekBar
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.whiteboardassignment.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: WhiteboardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.drawingView.setViewModel(viewModel)

        binding.btnSave.setOnClickListener {
            // Save logic goes here (already implemented via FileUtils)
            com.example.whiteboardassignment.FileUtils.saveToJson(this, viewModel.getAllData())
        }

        binding.btnLoad.setOnClickListener {
            // Load logic goes here (already implemented via FileUtils)
            com.example.whiteboardassignment.FileUtils.loadFromJson(this)?.let {
                viewModel.loadData(it)
            }
        }

        binding.btnClear.setOnClickListener {
            viewModel.clearCanvas()
        }

        // Stroke width SeekBar
        binding.strokeSeekBar.max = 50
        binding.strokeSeekBar.progress = 5
        binding.strokeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                viewModel.strokeWidth = progress.toFloat().coerceAtLeast(1f)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        // Color palette
        val colorButtons = listOf(
            binding.btnColorBlack to Color.BLACK,
            binding.btnColorRed to Color.RED,
            binding.btnColorGreen to Color.GREEN,
            binding.btnColorBlue to Color.BLUE,
            binding.btnColorYellow to Color.YELLOW,
            binding.btnColorCyan to Color.CYAN
        )
        colorButtons.forEach { (button, color) ->
            button.setOnClickListener { viewModel.strokeColor = color }
        }

        // Mode buttons
        binding.btnDraw.setOnClickListener { viewModel.currentMode = Mode.DRAW }
        binding.btnErase.setOnClickListener { viewModel.currentMode = Mode.ERASE }
        binding.btnRectangle.setOnClickListener { viewModel.currentMode = Mode.RECTANGLE }
        binding.btnCircle.setOnClickListener { viewModel.currentMode = Mode.CIRCLE }
        binding.btnLine.setOnClickListener { viewModel.currentMode = Mode.LINE }
        binding.btnPolygon.setOnClickListener { viewModel.currentMode = Mode.POLYGON }
    }
}

