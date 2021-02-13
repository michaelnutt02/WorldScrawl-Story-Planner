package com.example.wordscrawl.SwipeToDeleteCallbacks

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.wordscrawl.R
import com.example.wordscrawl.outlines.OutlineAdapter
import com.example.wordscrawl.profilecategory.ProfileCardAdapter

class OutlineSwipeToDeleteCallback(val adapter: OutlineAdapter, val context: Context): ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

        //save profile so we can get name
        val savedOutline = adapter.getOutline(viewHolder.adapterPosition)

        //DONE: Make dialog box here to ensure that we really want to delete a profile.
        val builder = AlertDialog.Builder(context)
        builder.setTitle(context.getString(R.string.delete_profile))
        builder.setMessage("Are you sure you want to delete ${savedOutline.title}? \n This cannot be undone.")

        builder.setPositiveButton(android.R.string.ok) {_,_->
            adapter.remove(viewHolder.adapterPosition)
        }
        builder.setNegativeButton(android.R.string.cancel){_,_->
            adapter.notifyItemChanged(viewHolder.adapterPosition)
        }
        builder.create().show()

    }


}