package com.example.temp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

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
                // par exemple :
                val intent = Intent(this@ListMan, Manuel::class.java)

                startActivity(intent)
            }
        })

        recyclerView.adapter = adapter

    }


    companion object {
        val listItem1 = List_Man(1, "Manuel 1", "poule")
        val listItem2 = List_Man(1, "Manuel 2", "etape2")
        val listItem3 = List_Man(1, "Manuel 3", "etape3")

        var Manuel_pages = listOf(listItem1, listItem2, listItem3)
    }

}

