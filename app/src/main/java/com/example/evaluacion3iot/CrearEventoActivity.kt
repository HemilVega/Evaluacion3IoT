package com.example.evaluacion3iot

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class CrearEventoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_evento)  // Aquí se carga el layout

        // Enlazar los EditText con las vistas del layout
        val etNombreEvento = findViewById<EditText>(R.id.etNombreEvento)
        val etFechaEvento = findViewById<EditText>(R.id.etFechaEvento)
        val etDescripcionEvento = findViewById<EditText>(R.id.etDescripcionEvento)
        val btnCrearEvento = findViewById<Button>(R.id.btnCrearEvento)

        // Lógica para el botón Crear Evento
        btnCrearEvento.setOnClickListener {
            // Obtener los valores de los EditText
            val nombre = etNombreEvento.text.toString()
            val fecha = etFechaEvento.text.toString()
            val descripcion = etDescripcionEvento.text.toString()

            // Verificar que los campos no estén vacíos
            if (nombre.isNotEmpty() && fecha.isNotEmpty() && descripcion.isNotEmpty()) {
                // Crear un mapa con los datos
                val evento = mapOf(
                    "nombre" to nombre,
                    "fecha" to fecha,
                    "descripcion" to descripcion
                )

                val database = FirebaseDatabase.getInstance()
                val myRef = database.reference.child("eventos").push() // 'push()' genera un ID único

                // Guardar los datos en Firebase
                myRef.setValue(evento).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Mostrar mensaje de éxito
                        Toast.makeText(this, "Evento creado correctamente", Toast.LENGTH_SHORT).show()

                        // Regresar a la pantalla principal (MainActivity)
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()  // Cierra la actividad actual (CrearEventoActivity)
                    } else {
                        // Manejar error
                        Toast.makeText(this, "Error al crear el evento", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
