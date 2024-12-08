package com.nieves.melanie.laboratoriocalificado04

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

// Activity principal que gestiona la cámara, captura fotos y navega a la galería
class MainActivity : AppCompatActivity() {

    private lateinit var previewView: PreviewView // Vista previa de la cámara
    private lateinit var imgCaptureBtn: Button // Botón para capturar una foto
    private lateinit var switchBtn: Button // Botón para cambiar entre cámara frontal y trasera
    private lateinit var galleryBtn: Button // Botón para abrir la galería
    private var imageCapture: ImageCapture? = null // Objeto para capturar imágenes
    private var isUsingFrontCamera = false // Controla si se está usando la cámara frontal o trasera
    private var cameraProvider: ProcessCameraProvider? = null // Proveedor de cámara para gestionar los casos de uso de la cámara

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Establece el layout para la actividad

        // Inicializa las vistas (PreviewView, botones)
        previewView = findViewById(R.id.preview)
        imgCaptureBtn = findViewById(R.id.img_capture_btn)
        switchBtn = findViewById(R.id.switch_btn)
        galleryBtn = findViewById(R.id.gallery_btn)

        // Inicia la cámara
        startCamera()

        // Configura los listeners de los botones
        imgCaptureBtn.setOnClickListener {
            takePhoto() // Captura una foto cuando se presiona el botón
        }

        switchBtn.setOnClickListener {
            switchCamera() // Cambia entre la cámara frontal y trasera cuando se presiona el botón
        }

        galleryBtn.setOnClickListener {
            openGallery() // Abre la galería cuando se presiona el botón
        }
    }

    // Inicia la cámara y configura el proveedor de cámara
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get() // Obtiene el proveedor de cámara
            bindCameraUseCases() // Asocia los casos de uso (cámara, vista previa, captura)
        }, ContextCompat.getMainExecutor(this))
    }

    // Configura los casos de uso de la cámara (vista previa y captura de imágenes)
    private fun bindCameraUseCases() {
        val cameraProvider = cameraProvider ?: return // Si no se obtiene el proveedor de cámara, retorna

        val preview = androidx.camera.core.Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider) // Establece la vista de la cámara
        }

        imageCapture = ImageCapture.Builder().build() // Inicializa el objeto para capturar imágenes

        // Selecciona la cámara (frontal o trasera)
        val cameraSelector = if (isUsingFrontCamera) {
            CameraSelector.DEFAULT_FRONT_CAMERA
        } else {
            CameraSelector.DEFAULT_BACK_CAMERA
        }

        try {
            cameraProvider.unbindAll() // Desvincula cualquier cámara previamente vinculada
            cameraProvider.bindToLifecycle(
                this,
                cameraSelector,
                preview,
                imageCapture
            ) // Vincula la cámara al ciclo de vida de la actividad
        } catch (e: Exception) {
            // Muestra un mensaje de error si no se puede iniciar la cámara
            Toast.makeText(this, "Error starting camera: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    // Toma una foto usando ImageCapture
    private fun takePhoto() {
        val imageCapture = imageCapture ?: return // Si no se ha inicializado ImageCapture, retorna

        // Crea el directorio de salida si no existe
        val outputDir = File(getExternalFilesDir(null), "images")
        if (!outputDir.exists()) {
            outputDir.mkdirs() // Crea el directorio si no existe
        }

        // Genera un nombre de archivo para la imagen
        val fileName = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(System.currentTimeMillis()) + ".jpg"
        val outputFile = File(outputDir, fileName)

        // Configura las opciones de salida para la imagen
        val outputOptions = ImageCapture.OutputFileOptions.Builder(outputFile).build()
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                // Callback para cuando la imagen se guarda correctamente
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    Toast.makeText(this@MainActivity, "Photo saved: ${outputFile.absolutePath}", Toast.LENGTH_SHORT).show()
                }

                // Callback para cuando ocurre un error al capturar la foto
                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(this@MainActivity, "Error capturing photo: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    // Cambia entre la cámara frontal y trasera
    private fun switchCamera() {
        isUsingFrontCamera = !isUsingFrontCamera // Alterna el valor de isUsingFrontCamera
        bindCameraUseCases() // Vuelve a vincular los casos de uso con la cámara seleccionada
    }

    // Abre la actividad de la galería
    private fun openGallery() {
        val intent = Intent(this, GalleryActivity::class.java)
        startActivity(intent) // Inicia la actividad de la galería
    }
}
