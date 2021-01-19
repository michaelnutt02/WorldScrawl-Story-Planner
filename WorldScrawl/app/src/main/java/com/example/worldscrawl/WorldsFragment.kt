package com.example.worldscrawl

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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class WorldsFragment() : Fragment() {


    lateinit var adapter: WorldsAdapter
    lateinit var recycleView: RecyclerView
    lateinit var addProfileButton:FloatingActionButton

    private lateinit var con:Context

    constructor(context: Context, addProfileButton: FloatingActionButton) : this() {

        this.con = context
        this.addProfileButton = addProfileButton

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_worlds, container, false)

        adapter = WorldsAdapter(con)
        recycleView = layout.findViewById(R.id.worlds_recycler_view)
        recycleView.layoutManager = LinearLayoutManager(con)
        recycleView.adapter = adapter

        this.addProfileButton.setOnClickListener{
            Log.i("New Profile","Pressing FAB")
            adapter.add()
        }

        return layout
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onDetach() {
        super.onDetach()

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


}