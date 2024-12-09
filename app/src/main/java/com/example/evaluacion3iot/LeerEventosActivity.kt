package com.example.evaluacion3iot

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*


class LeerEventosActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var eventAdapter: EventoAdapter
    private lateinit var eventList: ArrayList<Evento>
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leer_eventos)

        // Configuración inicial
        recyclerView = findViewById(R.id.recyclerViewEventos)
        recyclerView.layoutManager = LinearLayoutManager(this)
        eventList = ArrayList()
        eventAdapter = EventoAdapter(eventList)
        recyclerView.adapter = eventAdapter

        // Referencia a la base de datos Firebase
        database = FirebaseDatabase.getInstance().getReference("eventos")

        // Obtener los eventos desde Firebase
        fetchEventosFromFirebase()
    }

    private fun fetchEventosFromFirebase() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                eventList.clear()
                for (eventoSnapshot in snapshot.children) {
                    val evento = eventoSnapshot.getValue(Evento::class.java)
                    evento?.let {
                        it.id = eventoSnapshot.key ?: "" // Asignar el ID único del evento
                        eventList.add(it)
                    }
                }
                eventAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@LeerEventosActivity, "Error al leer eventos: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
