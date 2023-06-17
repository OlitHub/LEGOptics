package com.example.temp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

class ListMan : AppCompatActivity(){

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

}

