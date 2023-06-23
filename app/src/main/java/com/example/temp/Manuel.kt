package com.example.temp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.temp.ar.ArVisuActivity
import com.google.gson.Gson

class Manuel : AppCompatActivity(){

    private lateinit var speechRecognizer: SpeechRecognizer

    private var currentPagePosition: Int = 0

    private val handler = Handler()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.manuel)

        val gson = Gson()
        val json = intent.getStringExtra("pages")
        if (json != null) {
            Log.i("json", json)
        }

        var Manuel_page = gson.fromJson(json, Array<ListPage>::class.java).toList()

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewManuel)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val adapter = ManuelAdapter(Manuel_page)

        adapter.setOnItemClickListener(object : ManuelAdapter.OnItemClickListener {
            override fun onItemClick(listItem: ListPage) {
                currentPagePosition = adapter.getItemList().indexOf(listItem)
                val nextPosition = (currentPagePosition + 1)

                // Afficher l'élément suivant
                recyclerView.scrollToPosition(nextPosition)
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
                Log.i("Speech3", "onReadyForSpeech")
            }

            override fun onBeginningOfSpeech() {
                // Implémentation de la méthode onBeginningOfSpeech

                Log.i("Speech3", "onBeginningOfSpeech")
            }

            override fun onRmsChanged(rmsdB: Float) {
                // Implémentation de la méthode onRmsChanged
                //Log.i("Speech2", "onRmsChanged")
            }

            override fun onBufferReceived(buffer: ByteArray?) {
                // Implémentation de la méthode onBufferReceived
                Log.i("Speech3", "onBufferReceived")
            }

            override fun onEndOfSpeech() {
                // Implémentation de la méthode onEndOfSpeech
                Log.i("Speech3", "onEndOfSpeech")
                //startSpeechRecognition()
            }

            override fun onError(error: Int) {
                // Implémentation de la méthode onError
                Log.i("Speech3", "onError")
                pauseBeforeStartSpeechRecognition()
            }

            override fun onResults(results: Bundle?) {
                // Les résultats de la reconnaissance vocale sont disponibles
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)

                if (!matches.isNullOrEmpty()) {
                    Log.i("Speech3", matches.toString())
                    val voiceCommand = matches[0]
                    if (voiceCommand.equals("Suivant", ignoreCase = true)) {
                        // Lancer l'activité ListMan
                        runOnUiThread {
                            val nextPosition = (currentPagePosition + 1)
                            if (nextPosition < adapter.getItemList().size) {
                                currentPagePosition = nextPosition
                                recyclerView.scrollToPosition(nextPosition)
                            }
                        }
                    }

                    if (voiceCommand.equals("Précédent", ignoreCase = true)) {
                        // Lancer l'activité ListMan
                        runOnUiThread {
                            val nextPosition = (currentPagePosition - 1)
                            if (nextPosition < adapter.getItemList().size) {
                                currentPagePosition = nextPosition
                                recyclerView.scrollToPosition(nextPosition)
                            }
                        }
                    }

                    if (voiceCommand.equals("Retour", ignoreCase = true)) {
                        // Lancer l'activité ListMan
                        val intent = Intent(this@Manuel, ListMan::class.java)
                        speechRecognizer.destroy()
                        startActivity(intent)
                    }

                    if (voiceCommand.equals("quitter", ignoreCase = true)) {
                        // Lancer l'activité ListMan
                        val intent = Intent(this@Manuel, Manuel::class.java)
                        speechRecognizer.destroy()
                        startActivity(intent)
                    }


                    if (voiceCommand.equals("3D", ignoreCase = true)) {
                        // Lancer l'activité ListMan
                        val intent = Intent(this@Manuel, ArVisuActivity::class.java)
                        startActivity(intent)
                    }
                }
                startSpeechRecognition()
            }

            override fun onPartialResults(partialResults: Bundle?) {
                // Implémentation de la méthode onPartialResults
                Log.i("Speech3", "onPartialResults")
            }

            override fun onEvent(eventType: Int, params: Bundle?) {
                // Implémentation de la méthode onEvent
                Log.i("Speech3", "onEvent")
            }

        })

        startSpeechRecognitionWithDelay(200)

        //////////////////////////////////////////////////////////////////////////////////////////



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
                speechRecognizer.destroy()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


}

