package com.example.wordscrawl.editprofile

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wordscrawl.ProfileFragment
import com.example.wordscrawl.R
import com.example.wordscrawl.WorldsFragment
import com.example.wordscrawl.outlines.Outline
import com.example.wordscrawl.profilecategory.Profile
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore


class EditProfileFragment() : Fragment(), WorldsFragment.OnProfileSelectedListener {

    lateinit var adapter: EditProfileAdapter
    lateinit var recycleView: RecyclerView

    lateinit var profile:Profile

    private val profilesRef = FirebaseFirestore
            .getInstance()
            .collection("profiles")

    private lateinit var con: Context

    constructor(context: Context, profile: Profile) : this() {

        this.con = context
        this.profile = profile

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        //set

        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_edit_profile, container, false)
        adapter = EditProfileAdapter(con, profile, this)
        recycleView = view.findViewById(R.id.edit_profile_recycler)
        recycleView.layoutManager = LinearLayoutManager(con)
        recycleView.adapter = adapter

        //TODO: Hide the nav bar unless you're on the home screen (probably should hide/show it in MainActivity one time). (After back button is fixed below)
        //hide and show navigation bar
//        var navBar:BottomNavigationView = requireActivity().findViewById(R.id.nav_view)
//        navBar.visibility = View.GONE
//        navBar.visibility = View.VISIBLE

        //set default text to profile's name
        var editTitle:EditText = view.findViewById(R.id.edit_Title)
        editTitle.setText(profile.name)

        view.findViewById<FloatingActionButton>(R.id.addFAB).setOnClickListener {
            if (adapter != null) {
                adapter.add()
            }
        }
        view.findViewById<ImageButton>(R.id.saveButton).setOnClickListener{
            //DONE: update details in firestore with adapter function
            if(profile.id.equals("")){
                Log.i("profileId", "profile.id is an empty string")
            }else{
                Log.i("profileId", "profile.id not empty string")
            }
            adapter.updateFirestore()

            //update profile name with what's in edit title
            if(editTitle.text.toString().equals("")){
                profile.name = getString(R.string.default_character_name)
            }else{
                profile.name = editTitle.text.toString()
            }

            profilesRef.document(profile.id).set(profile)

            //TODO: need to make sure that profile call goes back to characters list rather than edit page, right now doesn't do that
            val profileFragment = ProfileFragment(con, profile)
            val ft = getActivity()?.supportFragmentManager?.beginTransaction()
            if (ft != null) {
                ft.replace(R.id.fragment_container, profileFragment)
                ft.addToBackStack("detail")
//                ft.disallowAddToBackStack()
                ft.commit()
            }
        }



        return view
    }

    override fun onProfileSelected(profile: Profile) {
        adapter.addTag(profile)
    }

    override fun onOutlineSelected(outline: Outline) {
        TODO("Not yet implemented")
    }


}