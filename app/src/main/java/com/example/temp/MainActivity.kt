package com.example.temp


import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private lateinit var editTextPseudo: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var preferences: SharedPreferences

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
}
