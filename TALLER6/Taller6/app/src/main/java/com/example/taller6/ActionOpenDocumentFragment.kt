package com.example.taller6

import android.graphics.pdf.PdfRenderer              // Clase para leer PDFs
import android.net.Uri                               // Manejo de rutas de archivos
import android.os.Bundle
import android.os.ParcelFileDescriptor               // Acceso al archivo físico
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.core.graphics.createBitmap            // Para crear la imagen del PDF

class ActionOpenDocumentFragment : Fragment() {

    private var uriString: String? = null             // Guarda la ruta del PDF
    private var pdfRenderer: PdfRenderer? = null      // Objeto que renderiza el PDF
    private var currentPage: PdfRenderer.Page? = null// Página actual del PDF
    private var parcelFileDescriptor: ParcelFileDescriptor? = null // Archivo abierto
    private var pageIndex: Int = 0                    // Número de página (empieza en 0)

    companion object {
        private const val ARG_URI = "arg_uri"         // Clave para pasar la URI, (URI es la dirección de un archivo o recurso)

        fun newInstance(uri: Uri): ActionOpenDocumentFragment {
            val f = ActionOpenDocumentFragment()      // Crea el fragmento
            val args = Bundle()                       // Crea un paquete de datos
            args.putString(ARG_URI, uri.toString())   // Guarda la ruta del PDF
            f.arguments = args                       // Asigna los datos al fragmento
            return f                                 // Retorna el fragmento listo
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uriString = arguments?.getString(ARG_URI)     // Recupera la ruta del PDF
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val root = inflater.inflate(R.layout.fragment_pdf_renderer_basic, container, false)
        // Carga el diseño del visor

        val imageView = root.findViewById<ImageView>(R.id.image)     // Imagen donde se ve el PDF
        val btnNext = root.findViewById<Button>(R.id.next)          // Botón siguiente
        val btnPrev = root.findViewById<Button>(R.id.previous)     // Botón anterior

        try {
            val uri = Uri.parse(uriString)          // Convierte texto a URI

            parcelFileDescriptor =
                requireContext().contentResolver.openFileDescriptor(uri, "r")
            // Abre el archivo en modo lectura

            if (parcelFileDescriptor != null) {

                pdfRenderer = PdfRenderer(parcelFileDescriptor!!)
                // Inicializa el lector del PDF

                showPage(pageIndex, imageView)     // Muestra la primera página

                btnNext.setOnClickListener {       // Botón SIGUIENTE
                    if (pageIndex + 1 < pdfRenderer!!.pageCount) {
                        pageIndex++                // Avanza página
                        showPage(pageIndex, imageView)
                    } else {
                        Toast.makeText(context, "Última página", Toast.LENGTH_SHORT).show()
                    }
                }

                btnPrev.setOnClickListener {       // Botón ANTERIOR
                    if (pageIndex > 0) {
                        pageIndex--                // Retrocede página
                        showPage(pageIndex, imageView)
                    } else {
                        Toast.makeText(context, "Primera página", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        } catch (e: Exception) {
            Toast.makeText(context, "Error al abrir PDF", Toast.LENGTH_SHORT).show()
        }

        return root
    }

    private fun showPage(index: Int, imageView: ImageView) {

        currentPage?.close()                        // Cierra página anterior
        currentPage = pdfRenderer!!.openPage(index)// Abre nueva página

        val bitmap = createBitmap(
            currentPage!!.width,
            currentPage!!.height
        )                                          // Crea imagen del tamaño del PDF

        currentPage!!.render(
            bitmap,
            null,
            null,
            PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
        )                                          // Dibuja el PDF en la imagen

        imageView.setImageBitmap(bitmap)           // Muestra la imagen en pantalla
    }

    override fun onDestroy() {
        currentPage?.close()                       // Libera página
        pdfRenderer?.close()                       // Cierra lector PDF
        parcelFileDescriptor?.close()              // Cierra archivo
        super.onDestroy()
    }
}
