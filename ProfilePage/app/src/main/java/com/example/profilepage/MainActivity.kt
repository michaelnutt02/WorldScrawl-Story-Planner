package com.example.profilepage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    lateinit var adapter: ProfileDetailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_with_picture)

        adapter = ProfileDetailAdapter(this)
        val profileRecycler = findViewById<RecyclerView>(R.id.profile_detail_recycler)
        profileRecycler.layoutManager = LinearLayoutManager(this)
        profileRecycler.setHasFixedSize(true)
        profileRecycler.adapter = adapter
    }
}