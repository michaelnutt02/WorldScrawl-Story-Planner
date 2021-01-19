package com.example.worldscrawl

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView


class CharactersViewHolder: RecyclerView.ViewHolder {

    private var questionText: TextView? = null
    private var viewCard:View = itemView.findViewById(R.id.single_question)
    lateinit var adapter:CharactersAdapter
    lateinit var context:Context


    constructor(itemView:View, adapter:CharactersAdapter, context: Context):super(itemView){
        this.context = context
        this.adapter = adapter
        questionText = itemView.findViewById(R.id.question_text)


    }

    fun bind(mathFact: MathFact){
        //update view to show question strings :)
        questionText?.text = mathFact.getQuestion()



    }


}