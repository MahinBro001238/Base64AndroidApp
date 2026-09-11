package com.mahin.base64
import android.content.ClipData
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import android.util.Base64
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.ClipboardManager
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        var mode = "Encode"
        var output_string = ""
        val output = findViewById<TextView>(R.id.Output)
        val encode = findViewById<Button>(R.id.Encode)
        val decode = findViewById<Button>(R.id.Decode)
        val source = findViewById<EditText>(R.id.Source)
        val convert = findViewById<Button>(R.id.Convert)
        val copy = findViewById<Button>(R.id.Copy)
        val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val LengthErrorDialog = AlertDialog.Builder(this)
            .setTitle("Invalid Text")
            .setMessage("Please enter some text")
            .setPositiveButton("Ok", null)
            .create()
        val Base64ErrorDialog = AlertDialog.Builder(this)
            .setTitle("Invalid base64")
            .setMessage("Please enter a valid base64 text")
            .setPositiveButton("Ok", null)
            .create()
        encode.setOnClickListener {
            encode.backgroundTintList = android.content.res.ColorStateList.valueOf(
                android.graphics.Color.parseColor("#FF432F67")
            )
            decode.backgroundTintList = android.content.res.ColorStateList.valueOf(
                android.graphics.Color.parseColor("#FF6750A4")
            )
            output.text = "The output will be displayed here"
            source.hint = "Enter your text"
            source.text.clear()
            mode = "Encode"
            copy.visibility = View.GONE
        }
        decode.setOnClickListener {
            decode.backgroundTintList = android.content.res.ColorStateList.valueOf(
                android.graphics.Color.parseColor("#FF432F67")
            )
            encode.backgroundTintList = android.content.res.ColorStateList.valueOf(
                android.graphics.Color.parseColor("#FF6750A4")
            )
            output.text = "The output will be displayed here"
            source.hint = "Enter your base64 text"
            source.text.clear()
            mode = "Decode"
            copy.visibility = View.GONE
        }
        convert.setOnClickListener() {
            if (source.text.toString().isEmpty()) {
                LengthErrorDialog.show()
            }
            else {
                if (mode == "Encode") {
                    output_string = Base64.encodeToString(source.text.toString().toByteArray(Charsets.UTF_8), Base64.NO_WRAP)
                    if (output_string.length > 340) {
                        output.text = output_string.take(340) + "..."
                    }
                    else {
                        output.text = output_string
                    }
                    copy.visibility = View.VISIBLE
                }
                else {
                    try {
                        output_string = String(Base64.decode(source.text.toString(), Base64.NO_WRAP), Charsets.UTF_8)
                        if (output_string.length > 340) {
                            output.text = output_string.take(340) + "..."
                        }
                        else {
                            output.text = output_string
                        }
                        copy.visibility = View.VISIBLE
                    }
                    catch (e: Exception) {
                        Base64ErrorDialog.show()
                    }
                }
            }
        }
        copy.setOnClickListener() {
            clipboard.setPrimaryClip(ClipData.newPlainText("Base64", output_string))
            Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show()
        }
    }
}