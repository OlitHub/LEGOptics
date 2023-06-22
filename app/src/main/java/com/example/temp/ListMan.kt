package com.example.temp

import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import android.os.Handler

class ListMan : AppCompatActivity(){

    private lateinit var speechRecognizer: SpeechRecognizer
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.listes_mans)


        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewMans)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = ListManuelAdapter(Manuel_pages)

        adapter.setOnItemClickListener(object : ListManuelAdapter.OnItemClickListener {
            override fun onItemClick(listItem: List_Man) {
                // Lancer l'activité Manuel.kt avec les données de l'élément cliqué
                val gson = Gson()
                val json = gson.toJson(listItem.pages)
                // par exemple :
                val intent = Intent(this@ListMan, Manuel::class.java)

                intent.putExtra("pages", json)

                startActivity(intent)
            }
        })

        recyclerView.adapter = adapter







//////////////////////////////////////////////////////////////////////////////////////////

        // Initialisez SpeechRecognizer
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            // ...

            override fun onReadyForSpeech(params: Bundle?) {
                // Implémentation de la méthode onReadyForSpeech
                Log.i("Speech2", "onReadyForSpeech")
            }

            override fun onBeginningOfSpeech() {
                // Implémentation de la méthode onBeginningOfSpeech

                Log.i("Speech2", "onBeginningOfSpeech")
            }

            override fun onRmsChanged(rmsdB: Float) {
                // Implémentation de la méthode onRmsChanged
                //Log.i("Speech2", "onRmsChanged")
            }

            override fun onBufferReceived(buffer: ByteArray?) {
                // Implémentation de la méthode onBufferReceived
                Log.i("Speech2", "onBufferReceived")
            }

            override fun onEndOfSpeech() {
                // Implémentation de la méthode onEndOfSpeech
                Log.i("Speech2", "onEndOfSpeech")
                //startSpeechRecognition()
            }

            override fun onError(error: Int) {
                // Implémentation de la méthode onError
                Log.i("Speech2", "onError")
                pauseBeforeStartSpeechRecognition()
            }

            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)

                if (!matches.isNullOrEmpty()) {
                    Log.i("Speech2", matches.toString())
                    val voiceCommand = matches[0]
                    if (voiceCommand.equals("Manuel poulet", ignoreCase = true)) {
                        // Lancer l'activité ListMan
                        val gson = Gson()
                        var json = gson.toJson(listMan1.pages)
                        val intent = Intent(this@ListMan, Manuel::class.java)
                        intent.putExtra("pages", json)
                        speechRecognizer.destroy()
                        startActivity(intent)
                    }

                }
                startSpeechRecognition()

            }

            override fun onPartialResults(partialResults: Bundle?) {
                // Implémentation de la méthode onPartialResults
                Log.i("Speech2", "onPartialResults")
            }

            override fun onEvent(eventType: Int, params: Bundle?) {
                // Implémentation de la méthode onEvent
                Log.i("Speech2", "onEvent")
            }

        })


        startSpeechRecognitionWithDelay(200)

        //////////////////////////////////////////////////////////////////////////////////////////

    }


    companion object {
        val listItem1 = ListPage(1, "Etape 1", "etape1")
        val listItem2 = ListPage(1, "Etape 2", "etape2")
        val listItem3 = ListPage(1, "Etape 3", "etape3")
        val listItem4 = ListPage(1, "Etape 4", "etape4")
        val listItem5 = ListPage(1, "Etape 5", "etape5")
        val listItem6 = ListPage(1, "Etape 6", "etape6")
        val listItem7 = ListPage(1, "Etape 7", "etape7")

        var pages_man = listOf(listItem1, listItem2, listItem3, listItem4, listItem5, listItem6, listItem7)

        val listMan1 = List_Man(1, "Manuel 1", "poule", pages_man)
        val listMan2 = List_Man(1, "Manuel 2", "etape2", pages_man)
        val listMan3 = List_Man(1, "Manuel 3", "etape3", pages_man)

        var Manuel_pages = listOf(listMan1, listMan2, listMan3)
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
    }

    override fun onDestroy() {
        super.onDestroy()

        // Libérez les ressources de SpeechRecognizer
        //speechRecognizer.destroy()
        speechRecognizer.destroy()
    }

    private fun startSpeechRecognitionWithDelay(delayMillis: Long) {
        handler.postDelayed({
            startSpeechRecognition()
        }, delayMillis)
    }

    private fun pauseBeforeStartSpeechRecognition() {
        try {
            Thread.sleep(500) // Pause de 2 secondes
            startSpeechRecognitionWithDelay(100) // Appel de startSpeechRecognition() après la pause
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }


}

