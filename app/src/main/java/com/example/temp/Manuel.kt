package com.example.temp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Manuel : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.manuel)


        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewManuel)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val adapter = ManuelAdapter(Manuel_pages)

        adapter.setOnItemClickListener(object : ManuelAdapter.OnItemClickListener {
            override fun onItemClick(listItem: ListPage) {
                val currentPosition = adapter.getItemList().indexOf(listItem)
                val nextPosition = (currentPosition + 1)

                // Afficher l'élément suivant
                recyclerView.scrollToPosition(nextPosition)
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

        var Manuel_pages = listOf(listItem1, listItem2, listItem3, listItem4, listItem5, listItem6, listItem7)
    }

}

