package com.example.wordscrawl

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wordscrawl.SwipeToDeleteCallbacks.ProfileSwipeToDeleteCallback
import com.example.wordscrawl.editprofile.EditProfileFragment
import com.example.wordscrawl.profilecategory.Profile
import com.example.wordscrawl.profilecategory.ProfileCardAdapter
import com.google.android.gms.tasks.Tasks
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot


class CharactersFragment() : Fragment() {

    lateinit var adapter: ProfileCardAdapter
    lateinit var recycleView: RecyclerView

    private var listener : WorldsFragment.OnProfileSelectedListener? = null

    val profilesRef = FirebaseFirestore
            .getInstance()
            .collection("profiles")

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
        adapter = ProfileCardAdapter(con, listener, "CHARACTER")
        recycleView = layout.findViewById(R.id.worlds_recycler_view)
        recycleView.layoutManager = LinearLayoutManager(con)
        recycleView.adapter = adapter

        //code adapted from https://medium.com/@zackcosborn/step-by-step-recyclerview-swipe-to-delete-and-undo-7bbae1fce27e
        recycleView.setAdapter(adapter)
        recycleView.setLayoutManager(LinearLayoutManager(con))
        val itemTouchHelper = ItemTouchHelper(ProfileSwipeToDeleteCallback(adapter, con))
        itemTouchHelper.attachToRecyclerView(recycleView)

        layout.findViewById<FloatingActionButton>(R.id.addFAB).setOnClickListener{
            //NOTE: When we only want values once, use .get().addOn listener. It only does it once and is more lightweight, less errors.
            var newprofile = Profile("CHARACTER","Mary Sue")
            adapter.add(newprofile)

            //find the newest profile added and load its edit detail page.
            profilesRef.orderBy(Profile.LAST_TOUCHED_KEY, Query.Direction.DESCENDING).limit(1).get().addOnSuccessListener {
                Log.i("adding", "we are getting here :)))")
                for(doc in it){
                    newprofile = Profile.fromSnapshot(doc)
                    val editProfileFragment = EditProfileFragment(con, newprofile)
                    val ft = getActivity()?.supportFragmentManager?.beginTransaction()
                    if (ft != null) {
                        ft.replace(R.id.fragment_container, editProfileFragment)
                        ft.addToBackStack(getString(R.string.skip_edit_page))
                        ft.commit()
                }
            }}

        }

        layout.findViewById<BottomNavigationView>(R.id.nav_view).setOnNavigationItemSelectedListener {
            listener?.onNavPressed(it.itemId)
            true
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