package com.example.harrypotterapi.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.harrypotterapi.R
import com.example.harrypotterapi.data.Varita

class VaritaAdapter(context: Context, private val resource: Int, objects: List<Varita>):
    ArrayAdapter<Varita>(context, resource, objects){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater = LayoutInflater.from(context)
        val view: View = convertView ?: layoutInflater.inflate(R.layout.item_mago, parent, false)

        val varita = getItem(position)

        val maderaNucleo = view.findViewById<TextView>(R.id.tvMaderaNucleo)
        val madera = varita?.madera
        val nucleo = varita?.nucleo
        maderaNucleo.text = formatearCampo(madera, nucleo)
        
        val mago = view.findViewById<TextView>(R.id.tvMago)
        val nombreMago = varita?.mago
        mago.text = formatearCampo(nombreMago)

        return view
    }

    private fun formatearCampo(madera: String?, nucleo: String?): String {
        return "Madera: $madera, Nucleo: $nucleo"
    }
    private fun formatearCampo(nameMago: String?): String{
        return "Personaje: $nameMago"
    }
}