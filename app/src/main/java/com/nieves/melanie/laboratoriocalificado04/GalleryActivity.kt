package com.nieves.melanie.laboratoriocalificado04

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import java.io.File

// Activity que muestra una galería de imágenes usando ViewPager2
class GalleryActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2 // ViewPager2 para navegar entre las imágenes
    private lateinit var galleryAdapter: GalleryAdapter // Adaptador para cargar las imágenes en el ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery) // Establece el layout para la actividad

        // Inicializa el ViewPager2
        viewPager = findViewById(R.id.view_pager)

        // Obtiene la lista de archivos de imagen (Se debe reemplazar con la ruta correcta de la carpeta)
        val imageFiles = getImagesFromDirectory()

        // Configura el adaptador con la lista de imágenes
        galleryAdapter = GalleryAdapter(this, imageFiles)
        viewPager.adapter = galleryAdapter // Asocia el adaptador al ViewPager
    }

    // Función para obtener los archivos de imagen de un directorio específico
    private fun getImagesFromDirectory(): List<File> {
        // Ruta de la carpeta "images" en el almacenamiento externo de la aplicación
        val imagesDir = File(getExternalFilesDir(null), "images") // Ruta de ejemplo
        if (!imagesDir.exists()) {
            imagesDir.mkdirs() // Crea el directorio si no existe
        }
        // Retorna una lista de archivos que son imágenes (con extensiones jpg o png)
        return imagesDir.listFiles()?.filter { it.isFile && it.extension in listOf("jpg", "png") } ?: emptyList()
    }
}
