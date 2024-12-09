package com.example.evaluacion3iot

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase

class EventoAdapter(private val eventList: List<Evento>) : RecyclerView.Adapter<EventoAdapter.EventoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_evento, parent, false)
        return EventoViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventoViewHolder, position: Int) {
        val evento = eventList[position]
        holder.nombreTextView.text = evento.nombre
        holder.fechaTextView.text = evento.fecha
        holder.descripcionTextView.text = evento.descripcion

        // Lógica para actualizar evento
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, ActualizarEventoActivity::class.java)
            intent.putExtra("eventoId", evento.id)
            intent.putExtra("nombre", evento.nombre)
            intent.putExtra("fecha", evento.fecha)
            intent.putExtra("descripcion", evento.descripcion)
            holder.itemView.context.startActivity(intent)
        }

        // Lógica para eliminar evento
        holder.eliminarButton.setOnClickListener {
            val database = FirebaseDatabase.getInstance().getReference("eventos").child(evento.id)
            database.removeValue().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(holder.itemView.context, "Evento eliminado", Toast.LENGTH_SHORT).show()
                    (eventList as MutableList).removeAt(position)
                    notifyItemRemoved(position)
                } else {
                    Toast.makeText(holder.itemView.context, "Error al eliminar el evento", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    // EventoViewHolder: Vincula los elementos de item_evento.xml
    class EventoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombreTextView: TextView = view.findViewById(R.id.tvNombreEvento)
        val fechaTextView: TextView = view.findViewById(R.id.tvFechaEvento)
        val descripcionTextView: TextView = view.findViewById(R.id.tvDescripcionEvento)
        val eliminarButton: Button = view.findViewById(R.id.btnEliminarEvento)
    }
}

