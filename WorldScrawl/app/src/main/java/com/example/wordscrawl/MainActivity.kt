package com.example.wordscrawl

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.wordscrawl.outlines.Outline
import com.example.wordscrawl.outlines.OutlineFragment
import com.example.wordscrawl.outlines.StoryOutlinesFragment
import com.example.wordscrawl.profilecategory.Profile
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(),WorldsFragment.OnProfileSelectedListener {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))


        //based this code off of https://stackoverflow.com/a/44190200 , which is recommended in the "hint" section of the lab


        //set the fragment to characters initially
        var switchTo: Fragment? = CharactersFragment(this)
        //pop off everything up to and including the current tab
        supportFragmentManager.popBackStack(getString(R.string.back_to_tabs), FragmentManager.POP_BACK_STACK_INCLUSIVE)

        //add new tab fragment
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment_container, switchTo!!)
        ft.commit()


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
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

    override fun onProfileSelected(profile: Profile) {

        var switchTo: Fragment? = null

        when(profile.type){
            "STORY" -> {
                switchTo = StoryOutlinesFragment(this, profile)
            }
            else -> {
                switchTo = ProfileFragment(this, profile)
            }
        }


        //load proper fragment in based off of navigation
        if(switchTo != null){
            val ft = supportFragmentManager.beginTransaction()
            ft.replace(R.id.fragment_container,switchTo)
            ft.addToBackStack(getString(R.string.skip_edit_page))
            ft.commit()

        }


    }

    override fun onOutlineSelected(outline: Outline) {
        Log.i("outline selected", "opening fragment")
        val profileFragment = OutlineFragment(this, outline)
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment_container, profileFragment)
        ft.addToBackStack(null)
        ft.commit()
    }

    override fun onNavPressed(id: Int) {
        var switchTo:Fragment? = null
        when(id) {
            R.id.navigation_characters -> {
                switchTo = CharactersFragment(this)
            }
            R.id.navigation_worlds -> {
                switchTo = WorldsFragment(this)
            }
            R.id.navigation_stories -> {
                switchTo = StoriesFragment(this)

            }
        }

            //load proper fragment in based off of navigation
            if(switchTo != null){

                //pop off everything up to and including the current tab
                supportFragmentManager.popBackStack(getString(R.string.back_to_tabs), FragmentManager.POP_BACK_STACK_INCLUSIVE)

                //add new tab fragment
                val ft = supportFragmentManager.beginTransaction()
                ft.replace(R.id.fragment_container, switchTo!!)
                ft.commit()

            }
    }

}