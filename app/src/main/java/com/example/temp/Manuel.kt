package com.example.temp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

class Manuel : AppCompatActivity(){

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
                val currentPosition = adapter.getItemList().indexOf(listItem)
                val nextPosition = (currentPosition + 1)

                // Afficher l'élément suivant
                recyclerView.scrollToPosition(nextPosition)
            }
        })

        recyclerView.adapter = adapter

    }


}

