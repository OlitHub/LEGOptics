package com.example.temp


import android.content.Intent
import android.content.SharedPreferences
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


class MainActivity : AppCompatActivity() {

    private lateinit var editTextPseudo: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var preferences: SharedPreferences

    private lateinit var speechRecognizer: SpeechRecognizer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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



        val voiceButton: Button = findViewById(R.id.voiceButton)

        // Configurez le bouton pour écouter le clic et activer la reconnaissance vocale
        voiceButton.setOnClickListener {
            startSpeechRecognition()
            Log.i("MainActivity", "Bouton de la voix cliqué")
        }

        // Initialisez SpeechRecognizer
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        Log.i("MainActivity", "Bouton de la voix cliqué")



        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                // La reconnaissance vocale est prête à écouter
            }

            override fun onBeginningOfSpeech() {
                // Le début de l'entrée vocale a été détecté
            }

            override fun onRmsChanged(rmsdB: Float) {
                // Le niveau de signal audio a changé
            }

            override fun onBufferReceived(buffer: ByteArray?) {
                // Les données audio sont en cours de réception
            }

            override fun onEndOfSpeech() {
                // La fin de l'entrée vocale a été détectée
            }

            override fun onError(error: Int) {
                // Une erreur s'est produite lors de la reconnaissance vocale
            }

            override fun onResults(results: Bundle?) {
                // Les résultats de la reconnaissance vocale sont disponibles
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)

                if (!matches.isNullOrEmpty()) {
                    val voiceCommand = matches[0]
                    if (voiceCommand.equals("Connexion", ignoreCase = true)) {
                        // Lancer l'activité ListMan
                        val intent = Intent(this@MainActivity, ListMan::class.java)
                        startActivity(intent)
                    }
                }
            }

            override fun onPartialResults(partialResults: Bundle?) {
                // Les résultats partiels de la reconnaissance vocale sont disponibles
            }

            override fun onEvent(eventType: Int, params: Bundle?) {
                // Des événements supplémentaires sont disponibles
            }
        })


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_preferences -> {
                //val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.menu_3d ->{
                startActivity(Intent(this, ArVisuActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun startSpeechRecognition() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)

        // Définissez la langue de reconnaissance vocale si nécessaire
        // intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "fr-FR")

        // Lancez la reconnaissance vocale
        speechRecognizer.startListening(intent)
    }

    override fun onDestroy() {
        super.onDestroy()

        // Libérez les ressources de SpeechRecognizer
        speechRecognizer.destroy()
    }


}
