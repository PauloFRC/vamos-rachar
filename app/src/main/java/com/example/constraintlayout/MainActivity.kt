package com.example.constraintlayout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import java.util.*

class MainActivity : AppCompatActivity() , TextWatcher, TextToSpeech.OnInitListener {
    private lateinit var tts: TextToSpeech
    private lateinit var edtConta: EditText
    private lateinit var edtPessoas: EditText
    private lateinit var textValue: TextView
    private var ttsSucess: Boolean = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edtConta = findViewById(R.id.edtConta)
        edtPessoas = findViewById(R.id.edtPessoas)
        textValue = findViewById(R.id.textValue)

        edtConta.addTextChangedListener(this)
        edtPessoas.addTextChangedListener(this)


        // Initialize TTS engine
        tts = TextToSpeech(this, this)
    }

    fun shareTextValue(v: View) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, textValue.text.toString())
        }
        startActivity(Intent.createChooser(shareIntent, "Share via"))
    }


    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
       Log.d("PDM24","Antes de mudar")

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        Log.d("PDM24","Mudando")
    }

    override fun afterTextChanged(s: Editable?) {
        val contaText = edtConta.text.toString()
        val pessoasText = edtPessoas.text.toString()

        val conta = contaText.toDoubleOrNull() ?: 0.0
        val pessoas = pessoasText.toDoubleOrNull() ?: 0.0

        val result = if (pessoas != 0.0) {
            conta / pessoas
        } else {
            0.0
        }
        textValue.text = String.format(Locale.getDefault(), "R$ %.2f", result)
    }

    fun clickFalar(v: View){
        if (tts.isSpeaking) {
            tts.stop()
        }
        if(ttsSucess) {
            Log.d ("PDM23", tts.language.toString())
            tts.speak(textValue.text, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }
    override fun onDestroy() {
            // Release TTS engine resources
            tts.stop()
            tts.shutdown()
            super.onDestroy()
        }

    override fun onInit(status: Int) {
            if (status == TextToSpeech.SUCCESS) {
                // TTS engine is initialized successfully
                tts.language = Locale.getDefault()
                ttsSucess=true
                Log.d("PDM23","Sucesso na Inicialização")
            } else {
                // TTS engine failed to initialize
                Log.e("PDM23", "Failed to initialize TTS engine.")
                ttsSucess=false
            }
        }
}

