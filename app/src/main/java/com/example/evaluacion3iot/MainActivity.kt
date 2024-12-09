package com.example.evaluacion3iot

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Conectar el botón Crear desde el diseño XML
        val btnCrear = findViewById<Button>(R.id.btnCrear)
        val btnLeer = findViewById<Button>(R.id.btnLeer)
        val btnActualizar = findViewById<Button>(R.id.btnActualizar)
        val btnEliminar = findViewById<Button>(R.id.btnEliminar)

        // Lógica para el botón Crear
        btnCrear.setOnClickListener {
            val evento = HashMap<String, String>()
            evento["nombre"] = "Evento Ejemplo" // Puedes usar campos dinámicos si los tienes
            evento["fecha"] = "2024-12-09"
            evento["descripcion"] = "Este es un evento de prueba"

            val database = FirebaseDatabase.getInstance()
            val myRef = database.reference.child("eventos").push() // 'push()' genera un ID único

            // Guardar datos en la base de datos
            myRef.setValue(evento).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Mostrar mensaje de éxito
                    Toast.makeText(this, "Evento creado correctamente", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, CrearEventoActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // En caso de error
                    Toast.makeText(this, "Error al crear el evento", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // botón Leer
        btnLeer.setOnClickListener {
            val database = FirebaseDatabase.getInstance()
            val myRef = database.reference.child("eventos")
            val intent = Intent(this, LeerEventosActivity::class.java)
            startActivity(intent)

            // Obtener los datos de la base de datos
            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (eventoSnapshot in snapshot.children) {
                        val evento = eventoSnapshot.getValue(Evento::class.java)
                        if (evento != null) {
                            Log.d("LeerEvento", "Nombre: ${evento.nombre}, Fecha: ${evento.fecha}")
                            Toast.makeText(applicationContext, "Evento: ${evento.nombre}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(applicationContext, "Error al leer eventos", Toast.LENGTH_SHORT).show()
                }
            })
        }

        // botón Actualizar
        btnActualizar.setOnClickListener {
            val eventoId = "id_del_evento"
            val database = FirebaseDatabase.getInstance()
            val myRef = database.reference.child("eventos").child(eventoId)

            // Datos a actualizar
            val updatedEvento = mapOf<String, Any>(
                "nombre" to "Evento Actualizado",
                "fecha" to "2024-12-10",
                "descripcion" to "Descripción del evento actualizado"
            )

            // Actualizar en Firebase
            myRef.updateChildren(updatedEvento).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Evento actualizado", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error al actualizar evento", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Eliminar el evento
        btnEliminar.setOnClickListener {
            val eventoId = "id_del_evento"
            val database = FirebaseDatabase.getInstance()
            val myRef = database.reference.child("eventos").child(eventoId)

            myRef.removeValue().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Evento eliminado", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error al eliminar evento", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val btnLogout = findViewById<Button>(R.id.btnLogout)

        btnLogout.setOnClickListener {
            // Cerrar sesión de Firebase
            FirebaseAuth.getInstance().signOut()

            // Mostrar mensaje de confirmación
            Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()

            // Redirigir a la pantalla de inicio de sesión (LoginActivity)
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()  // Cierra la actividad actual
        }
    }
}
