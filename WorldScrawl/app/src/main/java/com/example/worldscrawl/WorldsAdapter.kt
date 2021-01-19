package com.example.worldscrawl

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class WorldsAdapter(var context: Context): RecyclerView.Adapter<WorldsViewHolder>() {

    var list= arrayListOf<MathFact>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorldsViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.single_question, parent, false)

        return WorldsViewHolder(view, this, context)
    }

    override fun onBindViewHolder(holder: WorldsViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun add(){
        val fact = MathFact(10.toDouble(),2.toDouble())

        list.add(fact)
    }







}