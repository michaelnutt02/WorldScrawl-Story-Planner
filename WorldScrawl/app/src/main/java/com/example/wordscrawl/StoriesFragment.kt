package com.example.wordscrawl

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wordscrawl.SwipeToDeleteCallbacks.ProfileSwipeToDeleteCallback
import com.example.wordscrawl.profilecategory.Profile
import com.example.wordscrawl.profilecategory.ProfileCardAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class StoriesFragment() : Fragment() {

    lateinit var adapter: ProfileCardAdapter
    lateinit var recycleView: RecyclerView

    private var listener : WorldsFragment.OnProfileSelectedListener? = null

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


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val layout = inflater.inflate(R.layout.fragment_worlds, container, false)

        adapter = ProfileCardAdapter(con, listener, "STORY", uid)
        recycleView = layout.findViewById(R.id.worlds_recycler_view)
        recycleView.layoutManager = LinearLayoutManager(con)
        recycleView.adapter = adapter

        //code adapted from https://medium.com/@zackcosborn/step-by-step-recyclerview-swipe-to-delete-and-undo-7bbae1fce27e
        recycleView.setAdapter(adapter)
        recycleView.setLayoutManager(LinearLayoutManager(con))
        val itemTouchHelper = ItemTouchHelper(ProfileSwipeToDeleteCallback(adapter, con))
        itemTouchHelper.attachToRecyclerView(recycleView)


        layout.findViewById<FloatingActionButton>(R.id.addFAB).setOnClickListener{
            //TODO: Add a dialog box to input the story name
            addStoryDialog()

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

    fun addStoryDialog(){

        val builder = AlertDialog.Builder(con)
        //Set options
        builder.setTitle(getString(R.string.add_a_story))

        // Content is message, view, or list of items
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_add_title, null, false)
        builder.setView(view)



        builder.setPositiveButton(android.R.string.ok){ _, _->
            val characterName = view.findViewById<EditText>(R.id.name_edit_text).text.toString()
            var newprofile = Profile(uid, "STORY","ENTER NAME")

            if(!characterName.isEmpty()){
                newprofile = Profile(uid,"STORY",characterName)
            }

            adapter.add(newprofile)

        }
        builder.setNegativeButton(android.R.string.cancel, null)

        builder.create().show()

    }




}

