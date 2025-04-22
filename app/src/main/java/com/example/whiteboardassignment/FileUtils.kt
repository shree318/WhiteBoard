package com.example.whiteboardassignment

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

object FileUtils {
    private const val FILENAME = "whiteboard.json"
    private val gson = Gson()

    fun saveToJson(context: Context, data: List<Stroke>) {
        val json = gson.toJson(data)
        val file = File(context.getExternalFilesDir(null), FILENAME)
        file.writeText(json)
        Toast.makeText(context, "Saved to ${file.path}", Toast.LENGTH_SHORT).show()
        Log.e("SavedPath",file.path)
    }

    fun loadFromJson(context: Context): List<Stroke>? {
        val file = File(context.getExternalFilesDir(null), FILENAME)
        if (!file.exists()) return null
        val json = file.readText()
        val type = object : TypeToken<List<Stroke>>() {}.type
        return gson.fromJson(json, type)
    }
}
