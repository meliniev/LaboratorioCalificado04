package com.nieves.melanie.laboratoriocalificado04

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.io.File

// Adapter para mostrar imágenes en un RecyclerView
class GalleryAdapter(
    private val context: Context, // Contexto de la aplicación
    private val imageFiles: List<File> // Lista de archivos de imágenes
) : RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>() {

    // Crea una nueva vista para cada ítem del RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        // Infla el layout de cada item (list_item_img.xml) y lo devuelve en un ViewHolder
        val view = LayoutInflater.from(context).inflate(R.layout.list_item_img, parent, false)
        return GalleryViewHolder(view)
    }

    // Asocia los datos del archivo de imagen al ImageView en el ViewHolder
    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val imageFile = imageFiles[position]
        // Carga la imagen en el ImageView usando Glide, asegurando que la imagen se ajuste correctamente
        Glide.with(context)
            .load(imageFile) // Carga la imagen desde el archivo
            .centerCrop() // Asegura que la imagen cubra el espacio del ImageView sin distorsionarse
            .into(holder.imageView) // Coloca la imagen en el ImageView
    }

    // Retorna el número de elementos en la lista de imágenes
    override fun getItemCount(): Int = imageFiles.size

    // ViewHolder que mantiene la referencia al ImageView en cada item
    class GalleryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.local_img) // Referencia al ImageView
    }
}
