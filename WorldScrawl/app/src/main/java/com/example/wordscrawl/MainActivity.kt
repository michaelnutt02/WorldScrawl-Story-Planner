package com.example.wordscrawl

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.wordscrawl.outlines.Outline
import com.example.wordscrawl.outlines.OutlineFragment
import com.example.wordscrawl.outlines.StoryOutlinesFragment
import com.example.wordscrawl.profilecategory.Profile
import com.firebase.ui.auth.AuthUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity(),WorldsFragment.OnProfileSelectedListener, SplashFragment.OnLoginButtonPressedListener, ProfileFragment.OnHomeButtonPressedListener {

    private val WRITE_EXTERNAL_STORAGE_PERMISSION = 2
    private val auth = FirebaseAuth.getInstance()
    lateinit var authListener: FirebaseAuth.AuthStateListener
    private var uid: String? = null
    private val RC_SIGN_IN = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        checkPermissions()
        initializeListeners()


        //based this code off of https://stackoverflow.com/a/44190200 , which is recommended in the "hint" section of the lab


//        //set the fragment to characters initially
//        var switchTo: Fragment? = CharactersFragment(this)
//        //pop off everything up to and including the current tab
//        supportFragmentManager.popBackStack(getString(R.string.back_to_tabs), FragmentManager.POP_BACK_STACK_INCLUSIVE)
//
//        //add new tab fragment
//        val ft = supportFragmentManager.beginTransaction()
//        ft.replace(R.id.fragment_container, switchTo!!)
//        ft.commit()


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
                switchTo = StoryOutlinesFragment(this, profile, this)
            }
            else -> {
                switchTo = ProfileFragment(this, profile, this)
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
                switchTo = uid?.let { CharactersFragment(this, it, this) }
            }
            R.id.navigation_worlds -> {
                switchTo = uid?.let { WorldsFragment(this, it, this) }
            }
            R.id.navigation_stories -> {
                switchTo = uid?.let { StoriesFragment(this, it, this) }

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

    override fun onLoginButtonPressed() {
        launchLoginUI()
    }

    private fun launchLoginUI() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build())


        val loginIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setLogo(R.drawable.ic_launcher_custom)
            .build()
        // Create and launch sign-in intent
        startActivityForResult(
            loginIntent,
            RC_SIGN_IN)
    }


    private fun initializeListeners() {
        authListener = FirebaseAuth.AuthStateListener { auth ->
            val user = auth.currentUser
            Log.d("PB", "In auth listener, User: $user")
            if (user != null) {
                Log.d("PB", "UID: ${user.uid}")
                this.uid = user.uid
                Log.d("PB", "Name: ${user.displayName}")
                // plus email, photoUrl, phoneNumber
                switchToCharacterFragment()
            } else {
                switchToSplashFragment()
            }
        }
    }

    // Android’s security policy requires permissions to be requested
    // before some features are used.
    private fun checkPermissions() {
        // Check to see if we already have permissions
        if (ContextCompat
                .checkSelfPermission(
                    this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            // If we do not, request them from the user
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                WRITE_EXTERNAL_STORAGE_PERMISSION
            )
        }
    }

    override fun onStart() {
        super.onStart()
        auth.addAuthStateListener(authListener)
    }

    override fun onStop() {
        super.onStop()
        auth.removeAuthStateListener(authListener)
    }

    override fun switchToCharacterFragment(){
        //set the fragment to characters initially
        var switchTo: Fragment? = CharactersFragment(this, uid!!, this)
        //pop off everything up to and including the current tab
        supportFragmentManager.popBackStack(getString(R.string.back_to_tabs), FragmentManager.POP_BACK_STACK_INCLUSIVE)

        //add new tab fragment
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment_container, switchTo!!)
        ft.commit()
    }

    private fun switchToSplashFragment() {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment_container, SplashFragment())
        ft.commit()
    }

    fun signout() {
        auth.signOut()
    }

}