package com.example.wordscrawl

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wordscrawl.profilecategory.Profile
import com.example.wordscrawl.profilecategory.ProfileCardAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton


class EditProfileFragment() : Fragment() {

    lateinit var adapter: EditProfileAdapter
    lateinit var recycleView: RecyclerView

    lateinit var profile:Profile

    private lateinit var con: Context

    constructor(context: Context, profile: Profile) : this() {

        this.con = context
        this.profile = profile
        Log.i("profileId", "profile.id is ${profile.id}")

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_edit_profile, container, false)
        adapter = EditProfileAdapter(con, profile.id)
        recycleView = view.findViewById(R.id.edit_profile_recycler)
        recycleView.layoutManager = LinearLayoutManager(con)
        recycleView.adapter = adapter

        view.findViewById<FloatingActionButton>(R.id.addFAB).setOnClickListener {
            if (adapter != null) {
                adapter.add()
            }
        }
        view.findViewById<ImageButton>(R.id.saveButton).setOnClickListener{
            //TODO: update details in firestore with adapter function
            if(profile.id.equals("")){
                Log.i("profileId", "profile.id is an empty string")
            }else{
                Log.i("profileId", "profile.id not empty string")
            }
            adapter.updateFirestore()

            //TODO: need to make sure that profile call goes back to characters list, right now doesn't do that
            val profileFragment = ProfileFragment(con, profile)
            val ft = getActivity()?.supportFragmentManager?.beginTransaction()
            if (ft != null) {
                ft.replace(R.id.fragment_container, profileFragment)
                ft.addToBackStack("detail")
                ft.commit()
            }
        }
        return view
    }


}