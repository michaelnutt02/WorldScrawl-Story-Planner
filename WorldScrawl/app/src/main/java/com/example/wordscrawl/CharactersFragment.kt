package com.example.wordscrawl

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import com.example.wordscrawl.profilecategory.Profile
import com.example.wordscrawl.profilecategory.ProfileCardAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton


class CharactersFragment() : Fragment() {

    lateinit var adapter: ProfileCardAdapter
    lateinit var recycleView: RecyclerView

    private var listener : WorldsFragment.OnProfileSelectedListener? = null

    private lateinit var con: Context


    constructor(context: Context) : this() {

        this.con = context
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)



    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val layout = inflater.inflate(R.layout.fragment_worlds, container, false)
        //figured out how to do this from https://stackoverflow.com/questions/59864600/recyclerview-still-not-showing-items-on-fragment
        adapter = ProfileCardAdapter(con, listener, Profile.TYPE.CHARACTER)
        recycleView = layout.findViewById(R.id.worlds_recycler_view)
        recycleView.layoutManager = LinearLayoutManager(con)
        recycleView.adapter = adapter


        layout.findViewById<FloatingActionButton>(R.id.addFAB).setOnClickListener{
            var newprofile = Profile(Profile.TYPE.CHARACTER,"Mary Sue")
            adapter.add(newprofile)
//            var size = adapter.itemCount
//            Log.i("adding profile","In Characters, number of profiles are $size")
            val editProfileFragment = EditProfileFragment(con, newprofile)
            val ft = getActivity()?.supportFragmentManager?.beginTransaction()
            if (ft != null) {
                ft.replace(R.id.fragment_container, editProfileFragment)
                ft.addToBackStack("detail")
                ft.commit()
            }

        }


        return layout


    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)
        //tie recycleview and adapter here
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