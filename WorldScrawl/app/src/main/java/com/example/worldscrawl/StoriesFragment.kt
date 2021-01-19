package com.example.worldscrawl

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class StoriesFragment() : Fragment() {

    lateinit var adapter: WorldsAdapter
    lateinit var recycleView: RecyclerView
    lateinit var  addButton:FloatingActionButton

    private lateinit var con:Context

    constructor(context: Context, addButton:FloatingActionButton) : this() {

        this.con = context
        this.addButton = addButton

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val layout = inflater.inflate(R.layout.fragment_stories, container, false)

        adapter = WorldsAdapter(con)
        recycleView = layout.findViewById(R.id.stories_recycler_view)
        recycleView.layoutManager = LinearLayoutManager(con)
        recycleView.adapter = adapter


        addButton.setOnClickListener{
            adapter.add()
            Log.i("Adding Profile","In Stories")
        }

        return layout
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onDetach() {
        super.onDetach()

    }




}

