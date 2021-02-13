package com.example.wordscrawl.SwipeToDeleteCallbacks

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.wordscrawl.R
import com.example.wordscrawl.profiletag.ProfileTagAdapter

class TagSwipeToDeleteCallback(val adapter: ProfileTagAdapter, val context: Context): ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

        adapter.remove(viewHolder.adapterPosition)

    }


}