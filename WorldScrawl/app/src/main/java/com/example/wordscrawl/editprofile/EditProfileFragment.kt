package com.example.wordscrawl.editprofile

/*Camera code adapted from catch and kit lab*/

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wordscrawl.*
import com.example.wordscrawl.outlines.Outline
import com.example.wordscrawl.outlines.StoryOutlinesFragment
import com.example.wordscrawl.profilecategory.Profile
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream
import kotlin.random.Random

private const val RC_CHOOSE_PICTURE = 2

class EditProfileFragment() : Fragment(), WorldsFragment.OnProfileSelectedListener {

    lateinit var adapter: EditProfileAdapter
    lateinit var recycleView: RecyclerView

    lateinit var profile:Profile
    lateinit var mainActivity: MainActivity

    private val profilesRef = FirebaseFirestore
            .getInstance()
            .collection("profiles")

    private val storageRef = FirebaseStorage
        .getInstance()
        .reference
        .child("images")

    private lateinit var con: Context

    constructor(context: Context, profile: Profile, mainActivity: MainActivity) : this() {

        this.con = context
        this.profile = profile
        this.mainActivity = mainActivity

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_edit_profile, container, false)
        adapter = EditProfileAdapter(con, profile, mainActivity, this)
        recycleView = view.findViewById(R.id.edit_profile_recycler)
        recycleView.layoutManager = LinearLayoutManager(con)
        recycleView.adapter = adapter


        //set default text to profile's name
        var editTitle:EditText = view.findViewById(R.id.edit_Title)
        editTitle.setText(profile.name)

        if(profile.type.equals("STORY")){
            view.findViewById<FloatingActionButton>(R.id.addFAB).hide()
        }

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

            //make it switch depending on what type
            var switchTo:Fragment? = null
            if(profile.type.equals("STORY")){
                switchTo = StoryOutlinesFragment(con,profile, mainActivity)
            }else{
                switchTo = ProfileFragment(con, profile, mainActivity)
            }

            val ft = getActivity()?.supportFragmentManager?.beginTransaction()
            if (ft != null) {
                //go straight back to profile page
                getActivity()?.supportFragmentManager?.popBackStack(getString(R.string.skip_edit_page),FragmentManager.POP_BACK_STACK_INCLUSIVE)

                ft.replace(R.id.fragment_container, switchTo)
                ft.addToBackStack(getString(R.string.back_to_tabs))
                ft.commit()
            }
        }

        view.findViewById<ImageButton>(R.id.cameraButton).setOnClickListener{
            launchChooseIntent()
        }



        return view
    }

    fun scrollToPosition(pos: Int) {
        recycleView.scrollToPosition(pos)
    }

    override fun onProfileSelected(profile: Profile) {
        adapter.addTag(profile)
    }

    override fun onOutlineSelected(outline: Outline) {
        TODO("Not yet implemented")
    }

    override fun onNavPressed(id: Int) {
        TODO("Not yet implemented")
    }

    private fun launchChooseIntent() {
        // https://developer.android.com/guide/topics/providers/document-provider
        val choosePictureIntent = Intent(
                Intent.ACTION_OPEN_DOCUMENT,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        choosePictureIntent.addCategory(Intent.CATEGORY_OPENABLE)
        choosePictureIntent.type = "image/*"
        if (choosePictureIntent.resolveActivity(this.con.packageManager) != null) {
            startActivityForResult(choosePictureIntent, RC_CHOOSE_PICTURE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            sendGalleryPhotoToAdapter(data)
        }
    }

    private fun sendGalleryPhotoToAdapter(data: Intent?) {
        if (data != null && data.data != null) {
            val location = data.data!!.toString()
            addPhoto(location)
        }
    }
    fun addPhoto(localPath: String){
        //before we add a photo, let's delete the existing photo if it exists
        //we need to delete the image in storage
        if(profile.picture.isNotEmpty()){
            //delete file in storage
            val imageRef = storageRef.storage.getReferenceFromUrl(profile.picture)
            // Delete the file
            imageRef.delete().addOnSuccessListener {
//            Log.d(Constants.TAG, "image deleted successfully")
            }.addOnFailureListener {
                // Uh-oh, an error occurred!
//            Log.d(Constants.TAG, "ERROR: image did not delete")
            }

        }

        Log.i("adding", "About to rescale :)")
        ImageRescaleTask(localPath).execute()
    }

    // Could save a smaller version to Storage to save time on the network.
    // But if too small, recognition accuracy can suffer.
    inner class ImageRescaleTask(val localPath: String) : AsyncTask<Void, Void, Bitmap>() {
        override fun doInBackground(vararg p0: Void?): Bitmap? {
            // Reduces length and width by a factor (currently 2).
            val ratio = 2
            return BitmapUtils.rotateAndScaleByRatio(con, localPath, ratio)
        }

        override fun onPostExecute(bitmap: Bitmap?) {
            // https://firebase.google.com/docs/storage/android/upload-files
            Log.i("adding", "About to add to storage :)")
            storageAdd(localPath, bitmap)
        }
    }

    fun storageAdd(localPath:String, bitmap: Bitmap?){
        Log.i("adding", "Adding the picture to storage hopefully :)")
        val baos = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG,100, baos)
        val data = baos.toByteArray()
        val id = Math.abs(Random.nextLong()).toString()
        var uploadTask = storageRef.child(id).putBytes(data)

        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }

        uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>>{ task ->
            if(!task.isSuccessful){
                task.exception?.let{
                    throw it
                }
            }
            return@Continuation storageRef.child(id).downloadUrl
        }).addOnCompleteListener{ task ->
            if(task.isSuccessful){
                val downloadUri = task.result
                profile.picture = downloadUri.toString()
                //update the profile to have a picture
                Log.i("adding", "The download uri is ${profile.picture} :)")
                profilesRef.document(profile.id).set(profile)
//                Log.d(Constants.TAG,"Image download URL succeeded: $downloadUri")
            }else{
                //Handle failures
                // ...
//                Log.d(Constants.TAG,"Image download URL failed")
            }

        }

    }


}