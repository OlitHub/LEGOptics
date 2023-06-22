package com.example.temp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.annotations.SerializedName
import com.example.temp.R


data class ListPage(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("imagePath") val imagePath: String
)

data class List_Man(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("imagePath") val imagePath: String,
    @SerializedName("pages") val pages: List<ListPage>
)


class ManuelAdapter(private val itemList: List<ListPage>) : RecyclerView.Adapter<ManuelAdapter.ViewHolder>() {


    private var onItemClickListener: OnItemClickListener? = null

    // Définit une interface pour gérer les événements de clic
    interface OnItemClickListener {
        fun onItemClick(listItem: ListPage)
    }

    // Méthode pour définir le listener de clic
    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.nameTextView)
        val imageView: ImageView = view.findViewById(R.id.imageView)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val listItem = itemList[position]
                    onItemClickListener?.onItemClick(listItem)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.page_man, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.nameTextView.text = item.name
        val resourceId = getDrawableResourceId(item.imagePath)
        if (resourceId != 0) {
            holder.imageView.setImageResource(resourceId)
        } else {
            // Gérer le cas où l'identifiant de ressource est introuvable
            // par exemple, afficher une image par défaut ou laisser l'image vide
            holder.imageView.setImageResource(R.drawable.m1_1)
        }
    }

    private fun getDrawableResourceId(imageName: String): Int {
        return try {
            val field = R.drawable::class.java.getDeclaredField(imageName)
            field.getInt(null)
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun getItemList(): List<ListPage> {
        return itemList
    }
}

class ListManuelAdapter(private val itemList: List<List_Man>) : RecyclerView.Adapter<ListManuelAdapter.ViewHolder>() {


    private var onItemClickListener: OnItemClickListener? = null

    // Définit une interface pour gérer les événements de clic
    interface OnItemClickListener {
        fun onItemClick(listItem: List_Man)
    }

    // Méthode pour définir le listener de clic
    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.nameTextView)
        val imageView: ImageView = view.findViewById(R.id.imageView)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val listItem = itemList[position]
                    onItemClickListener?.onItemClick(listItem)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.liste_man, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.nameTextView.text = item.name
        val resourceId = getDrawableResourceId(item.imagePath)
        if (resourceId != 0) {
            holder.imageView.setImageResource(resourceId)
        } else {
            // Gérer le cas où l'identifiant de ressource est introuvable
            // par exemple, afficher une image par défaut ou laisser l'image vide
            holder.imageView.setImageResource(R.drawable.m1_1)
        }
    }

    private fun getDrawableResourceId(imageName: String): Int {
        return try {
            val field = R.drawable::class.java.getDeclaredField(imageName)
            field.getInt(null)
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun getItemList(): List<List_Man> {
        return itemList
    }
}
