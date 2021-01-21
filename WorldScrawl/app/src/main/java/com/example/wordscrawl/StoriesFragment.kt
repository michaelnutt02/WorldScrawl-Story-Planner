package com.example.wordscrawl

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wordscrawl.profilecategory.Profile
import com.example.wordscrawl.profilecategory.ProfileCardAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton


class StoriesFragment() : Fragment() {

    lateinit var adapter: ProfileCardAdapter
    lateinit var recycleView: RecyclerView
    lateinit var  addButton:FloatingActionButton

    private var listener : WorldsFragment.OnProfileSelectedListener? = null

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

        adapter = ProfileCardAdapter(con, listener)
        recycleView = layout.findViewById(R.id.stories_recycler_view)
        recycleView.layoutManager = LinearLayoutManager(con)
        recycleView.adapter = adapter


        addButton.setOnClickListener{
            var newprofile = Profile("Mary Sue",arrayListOf(), R.drawable.harry_potter)
            adapter.add(newprofile)
            var size = adapter.itemCount
            Log.i("Adding Profile","In Stories, number of profiles are $size")
        }

        return layout
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is WorldsFragment.OnProfileSelectedListener){
            listener = context
        }else{
            throw RuntimeException(context.toString()+ "must implement OnProfileSelected")
        }

    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }




}

