package com.example.wordscrawl

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wordscrawl.outlines.Outline
import com.example.wordscrawl.profilecategory.Profile
import com.example.wordscrawl.profilecategory.ProfileCardAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton


class WorldsFragment() : Fragment() {


    lateinit var adapter: ProfileCardAdapter
    lateinit var recycleView: RecyclerView

    private var listener : OnProfileSelectedListener? = null

    private lateinit var con:Context

    constructor(context: Context) : this() {

        this.con = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_worlds, container, false)

        adapter = ProfileCardAdapter(con, listener, "WORLD")
        recycleView = layout.findViewById(R.id.worlds_recycler_view)
        recycleView.layoutManager = LinearLayoutManager(con)
        recycleView.adapter = adapter

        layout.findViewById<FloatingActionButton>(R.id.addFAB).setOnClickListener{
            var newprofile = Profile("WORLD","Hogwarts")
            adapter.add(newprofile)
            var size = adapter.itemCount
            Log.i("Adding Profile","In Worlds, number of profiles are $size")
        }

        return layout
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnProfileSelectedListener){
            listener = context
        }else{
            throw RuntimeException(context.toString()+ "must implement OnWorldSelected")
        }

    }

    override fun onDetach() {
        super.onDetach()
        listener = null

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                Log.i("Clear","Pressed trash can")
                true
            }


            else -> super.onOptionsItemSelected(item)
        }

    }

    interface OnProfileSelectedListener{
        fun onProfileSelected(profile:Profile)
        fun onOutlineSelected(outline:Outline)
    }


}