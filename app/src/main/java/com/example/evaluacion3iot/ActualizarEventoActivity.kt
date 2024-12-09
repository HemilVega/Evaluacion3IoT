package com.example.evaluacion3iot

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class ActualizarEventoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_evento)

        // Enlazar los elementos del layout
        val etNombreActualizar = findViewById<EditText>(R.id.etNombreActualizar)
        val etFechaActualizar = findViewById<EditText>(R.id.etFechaActualizar)
        val etDescripcionActualizar = findViewById<EditText>(R.id.etDescripcionActualizar)
        val btnGuardarCambios = findViewById<Button>(R.id.btnGuardarCambios)

        // Obtener los datos enviados desde la actividad anterior
        val eventoId = intent.getStringExtra("eventoId") ?: ""
        val nombre = intent.getStringExtra("nombre") ?: ""
        val fecha = intent.getStringExtra("fecha") ?: ""
        val descripcion = intent.getStringExtra("descripcion") ?: ""

        // Llenar los campos con los datos actuales
        etNombreActualizar.setText(nombre)
        etFechaActualizar.setText(fecha)
        etDescripcionActualizar.setText(descripcion)

        // Guardar los cambios al presionar el botón
        btnGuardarCambios.setOnClickListener {
            val nuevoNombre = etNombreActualizar.text.toString()
            val nuevaFecha = etFechaActualizar.text.toString()
            val nuevaDescripcion = etDescripcionActualizar.text.toString()

            if (nuevoNombre.isNotEmpty() && nuevaFecha.isNotEmpty() && nuevaDescripcion.isNotEmpty()) {
                // Actualizar en Firebase
                val database = FirebaseDatabase.getInstance()
                val myRef = database.reference.child("eventos").child(eventoId)

                val updatedEvento = mapOf(
                    "nombre" to nuevoNombre,
                    "fecha" to nuevaFecha,
                    "descripcion" to nuevaDescripcion
                )

                myRef.updateChildren(updatedEvento).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Evento actualizado correctamente", Toast.LENGTH_SHORT).show()
                        finish() // Cerrar la actividad y regresar
                    } else {
                        Toast.makeText(this, "Error al actualizar evento", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
