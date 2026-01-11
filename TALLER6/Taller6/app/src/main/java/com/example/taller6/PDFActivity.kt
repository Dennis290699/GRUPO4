package com.example.taller6

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

class PDFActivity : AppCompatActivity() {

    // Vista que se muestra cuando aún no se ha abierto ningún PDF
    private lateinit var noDocumentView: ViewGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Carga el layout XML de esta pantalla
        setContentView(R.layout.activity_main_real)

        // Referencia al layout que muestra el mensaje "no hay documento"
        noDocumentView = findViewById(R.id.no_document_view)

        // Botón que permite seleccionar un archivo PDF
        findViewById<Button>(R.id.open_file).setOnClickListener {
            abrirSelectorPDF()   // Llama al explorador de archivos
        }
        // Botón para salir y volver al login (otro usuario)
        findViewById<Button>(R.id.logout).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Cierra esta actividad
        }

        // Manejo del botón "back" que soporta gestos
        val backCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (supportFragmentManager.backStackEntryCount > 0) {
                    supportFragmentManager.popBackStack()
                    noDocumentView.visibility = View.VISIBLE
                } else {
                    // Desactiva este callback y delega al comportamiento por defecto
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, backCallback)
    }

    // Abre el explorador de archivos para escoger un PDF
    private fun abrirSelectorPDF() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "application/pdf"              // Solo permite PDFs
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        startActivityForResult(intent, 200)      // Código 200 identifica esta petición
    }

    // Se ejecuta cuando el usuario selecciona un archivo
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Verifica que venga de nuestro selector y que todo esté correcto
        if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                mostrarPDF(uri)                 // Abre el PDF seleccionado
            }
        }
    }

    // Muestra el PDF dentro de un Fragment
    private fun mostrarPDF(uri: Uri) {
        val fragment = ActionOpenDocumentFragment.newInstance(uri)
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack("pdf_view") // Le damos un nombre al estado
            .commit()

        // Ocultamos el botón inicial
        noDocumentView.visibility = View.GONE
    }

}
