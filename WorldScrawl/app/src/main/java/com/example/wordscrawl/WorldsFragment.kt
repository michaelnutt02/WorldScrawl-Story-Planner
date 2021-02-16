package com.example.wordscrawl

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import android.widget.Toolbar
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wordscrawl.SwipeToDeleteCallbacks.ProfileSwipeToDeleteCallback
import com.example.wordscrawl.outlines.Outline
import com.example.wordscrawl.profilecategory.Profile
import com.example.wordscrawl.profilecategory.ProfileCardAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class WorldsFragment() : Fragment() {


    lateinit var adapter: ProfileCardAdapter
    lateinit var recycleView: RecyclerView

    private var listener : OnProfileSelectedListener? = null

    private lateinit var con:Context
    private lateinit var uid: String
    private lateinit var mainActivity: MainActivity


    constructor(context: Context, uid: String, mainActivity: MainActivity) : this() {

        this.con = context
        this.uid = uid
        this.mainActivity = mainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity.setSupportActionBar(mainActivity.findViewById(R.id.toolbar))
        setHasOptionsMenu(true)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_worlds, container, false)

        adapter = ProfileCardAdapter(con, listener, "WORLD", uid)
        recycleView = layout.findViewById(R.id.worlds_recycler_view)
        recycleView.layoutManager = LinearLayoutManager(con)
        recycleView.adapter = adapter

        //code adapted from https://medium.com/@zackcosborn/step-by-step-recyclerview-swipe-to-delete-and-undo-7bbae1fce27e
        recycleView.setAdapter(adapter)
        recycleView.setLayoutManager(LinearLayoutManager(con))
        val itemTouchHelper = ItemTouchHelper(ProfileSwipeToDeleteCallback(adapter, con))
        itemTouchHelper.attachToRecyclerView(recycleView)

        layout.findViewById<FloatingActionButton>(R.id.addFAB).setOnClickListener{
            var newprofile = Profile(uid, "WORLD","ENTER NAME")
            adapter.add(newprofile)
            var size = adapter.itemCount
            Log.i("Adding Profile","In Worlds, number of profiles are $size")
        }
        layout.findViewById<BottomNavigationView>(R.id.nav_view).setOnNavigationItemSelectedListener {
            listener?.onNavPressed(it.itemId)
            true
        }

//        layout.findViewById<FloatingActionButton>(R.id.logoutFAB).setOnClickListener{
//            mainActivity.signout()
//        }
        layout.findViewById<ImageButton>(R.id.logoutFAB).setOnClickListener{
            mainActivity.signout()
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




    interface OnProfileSelectedListener{
        fun onProfileSelected(profile:Profile)
        fun onOutlineSelected(outline:Outline)
        fun onNavPressed(id:Int)

    }


}