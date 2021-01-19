package com.example.worldscrawl

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class WorldsViewHolder: RecyclerView.ViewHolder {


    private var questionText:TextView = itemView.findViewById(R.id.question_text)
    lateinit var adapter:WorldsAdapter
    lateinit var context: Context

    constructor(itemView: View, adapter:WorldsAdapter, context: Context):super(itemView){
        this.context = context
        this.adapter = adapter


        //update position, etc
    }

    fun bind(mathFact: MathFact){
        //update view to show question strings :)
        questionText?.text = mathFact.getReview()

        Log.i("New Review","Adapter Position is added at  ${adapterPosition}")

    }



}