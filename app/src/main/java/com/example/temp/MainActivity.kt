package com.example.temp


import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.speech.RecognitionListener
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import com.google.gson.Gson


class MainActivity : AppCompatActivity() {

    private lateinit var editTextPseudo: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var preferences: SharedPreferences

    private lateinit var speechRecognizer: SpeechRecognizer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            // La permission n'est pas accordée, demande à l'utilisateur de l'accorder
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), 123)
        }

        editTextPseudo = findViewById(R.id.usernameEditText)
        editTextPassword = findViewById(R.id.passwordEditText)
        val buttonOK: Button = findViewById(R.id.okButton)

        // Charger le dernier pseudo sauvegardé
        preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val lastPseudo = preferences.getString("pseudo", "")
        editTextPseudo.setText(lastPseudo)

        val lastPassword = preferences.getString("password", "")
        editTextPassword.setText(lastPassword)



        buttonOK.setOnClickListener {
            val pseudo = editTextPseudo.text.toString()
            val password = editTextPassword.text.toString()

            // Sauvegarder le pseudo dans les préférences partagées
            val editor = preferences.edit()
            editor.putString("pseudo", pseudo)
            editor.putString("password", password)
            editor.apply()

            if(pseudo=="vlad" && password=="pmr"){
                val intent = Intent(this, ListMan::class.java)
                intent.putExtra("pseudo", pseudo)
                intent.putExtra("password", password)
                startActivity(intent)
            }

            else{
                Toast.makeText(this, "Mauvaise connexion", Toast.LENGTH_SHORT).show()
            }
        }



//////////////////////////////////////////////////////////////////////////////////////////
        // Initialisez SpeechRecognizer
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)

        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            // ...

            override fun onReadyForSpeech(params: Bundle?) {
                // Implémentation de la méthode onReadyForSpeech
                Log.i("Speech", "onReadyForSpeech")
            }

            override fun onBeginningOfSpeech() {
                // Implémentation de la méthode onBeginningOfSpeech

                Log.i("Speech", "onBeginningOfSpeech")
            }

            override fun onRmsChanged(rmsdB: Float) {
                // Implémentation de la méthode onRmsChanged
                //Log.i("Speech", "onRmsChanged")
            }

            override fun onBufferReceived(buffer: ByteArray?) {
                // Implémentation de la méthode onBufferReceived
                Log.i("Speech", "onBufferReceived")
            }

            override fun onEndOfSpeech() {
                // Implémentation de la méthode onEndOfSpeech
                Log.i("Speech", "onEndOfSpeech")
                //startSpeechRecognition()
            }

            override fun onError(error: Int) {
                // Implémentation de la méthode onError
                Log.i("Speech", "onError")
                startSpeechRecognition()
            }

            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                Log.i("Speech", "onResults")
                if (!matches.isNullOrEmpty()) {
                    Log.i("Speech1", matches.toString())
                    val voiceCommand = matches[0]
                    if (voiceCommand.equals("Connexion", ignoreCase = true)) {
                        // Lancer l'activité ListMan
                        val intent = Intent(this@MainActivity, ListMan::class.java)
                        speechRecognizer.destroy()
                        startActivity(intent)
                    }

                }
                startSpeechRecognition()

            }

            override fun onPartialResults(partialResults: Bundle?) {
                // Implémentation de la méthode onPartialResults
                Log.i("Speech", "onPartialResults")
            }

            override fun onEvent(eventType: Int, params: Bundle?) {
                // Implémentation de la méthode onEvent
                Log.i("Speech", "onEvent")
            }

        })

        startSpeechRecognition()

    }






    private fun startSpeechRecognition() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)

        // Définissez la langue de reconnaissance vocale si nécessaire
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "fr-FR")

        // Lancez la reconnaissance vocale
        speechRecognizer.startListening(intent)
    }

    private fun stopSpeechRecognition() {
        speechRecognizer.stopListening()
        speechRecognizer.destroy()
    }

    override fun onDestroy() {
        super.onDestroy()

        // Libérez les ressources de SpeechRecognizer
        //speechRecognizer.destroy()
        speechRecognizer.destroy()
    }




}
