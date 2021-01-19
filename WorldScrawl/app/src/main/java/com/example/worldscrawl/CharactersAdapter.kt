package com.example.worldscrawl

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class CharactersAdapter(var context:Context):RecyclerView.Adapter<CharactersViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.single_question, parent, false)
        return CharactersViewHolder(view, this, context)
    }

    override fun onBindViewHolder(holder: CharactersViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {

        return 0

    }


}