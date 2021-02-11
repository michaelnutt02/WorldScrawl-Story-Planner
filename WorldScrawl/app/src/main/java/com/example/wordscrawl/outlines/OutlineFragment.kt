package com.example.wordscrawl.outlines

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.print.PrintAttributes
import android.print.PrintManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.wordscrawl.R
import com.google.firebase.firestore.FirebaseFirestore
import jp.wasabeef.richeditor.RichEditor
import java.io.File

/**
 * Library used for rich-text editor: https://github.com/wasabeef/richeditor-android
 * This code is heavily based off of the library sample example
 * here: https://github.com/wasabeef/richeditor-android/blob/master/sample/src/main/java/jp/wasabeef/sample/MainActivity.java
 *
 *The printing code is adapted from this documentation: https://developer.android.com/training/printing/html-docs
 */
class OutlineFragment(context: Context, outline: Outline) : Fragment() {

    private val outlinesRef = FirebaseFirestore
            .getInstance()
            .collection("outlines")

    private var outline = outline
    private var con = context

    private var mWebView: WebView? = null


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_outline, container, false)

//        var navBar: BottomNavigationView = requireActivity().findViewById(R.id.nav_view)
//        navBar.visibility = View.GONE
//        navBar.visibility = View.VISIBLE

        //set title to outline title
        var editTitle = view.findViewById<EditText>(R.id.edit_Title)
        if(!outline.title.isEmpty()){
            editTitle.setText(outline.title)
        }




        var mEditor = view.findViewById<RichEditor>(R.id.editor);
        //Initialize editing height
        mEditor.setEditorHeight(200);
        //Initialize font size
        mEditor.setEditorFontSize(22);
        //Initialize font color
        mEditor.setEditorFontColor(R.color.black);
        //mEditor.setEditorBackgroundColor(Color.BLUE);

        //Initialize the padding
        mEditor.setPadding(10, 10, 10, 10);
        //Set the background of the edit box, which can be a network picture
        // mEditor.setBackground("https://raw.githubusercontent.com/wasabeef/art/master/chip.jpg");
        // mEditor.setBackgroundColor(Color.BLUE);
//        mEditor.setBackgroundResource(R.drawable.bob);
        //Set the default display statement

        mEditor.setPlaceholder("Insert text here...");

        //set the Text here if firebase isn't null
        if(outline.body.isEmpty()){
            //set html to hold type of outline template
        }else{
            mEditor.html = outline.body
        }


        //Set whether the editor is available
        mEditor.setInputEnabled(true);

        //look at all buttons on the menu :)
        view.findViewById<ImageButton>(R.id.italicsButton).setOnClickListener{
            mEditor.focusEditor()
            mEditor.setItalic()
            toggleButtonColor(R.id.italicsButton)

        }

        view.findViewById<ImageButton>(R.id.boldButton).setOnClickListener{
            mEditor.focusEditor()
            mEditor.setBold()
        }

        view.findViewById<ImageButton>(R.id.underlineButton).setOnClickListener{
            mEditor.focusEditor();
            mEditor.setUnderline()
        }
        view.findViewById<ImageButton>(R.id.strikeThroughButton).setOnClickListener{
            mEditor.focusEditor();
            mEditor.setStrikeThrough()
        }

        view.findViewById<ImageButton>(R.id.bulletButton).setOnClickListener{
            mEditor.focusEditor();
            mEditor.setBullets()
        }

        view.findViewById<ImageButton>(R.id.numberedListButton).setOnClickListener{
            mEditor.focusEditor();
            mEditor.setNumbers()
        }
        view.findViewById<ImageButton>(R.id.indentButton).setOnClickListener{
            mEditor.focusEditor();
            mEditor.setIndent()
        }
        view.findViewById<ImageButton>(R.id.leftalignButton).setOnClickListener{
            mEditor.focusEditor();
            mEditor.setAlignLeft()
        }
        view.findViewById<ImageButton>(R.id.centeralignButton).setOnClickListener{
            mEditor.focusEditor();
            mEditor.setAlignCenter()
        }
        view.findViewById<ImageButton>(R.id.rightalignButton).setOnClickListener{
            mEditor.focusEditor();
            mEditor.setAlignRight()
        }

        view.findViewById<ImageButton>(R.id.superscriptButton).setOnClickListener{
            mEditor.focusEditor();
            mEditor.setSuperscript()
        }
        view.findViewById<ImageButton>(R.id.subscriptButton).setOnClickListener{
            mEditor.focusEditor();
            mEditor.setSubscript()
            Log.i("editor", "this is the content ${mEditor.html}")
        }
        view.findViewById<ImageButton>(R.id.checkboxButton).setOnClickListener{
            mEditor.focusEditor();
            mEditor.insertTodo()
        }

        //make save Button
        view.findViewById<ImageButton>(R.id.saveButton).setOnClickListener{
            if(!editTitle.text.isEmpty()){
                outline.setDetailTitle(editTitle.text.toString())
            }
            if(mEditor.html != null){
                outline.setDetailBody(mEditor.html)
            }else{
                outline.setDetailBody("")
            }

            //update firestore
            outlinesRef.document(outline.id).set(outline)

            //make a toast letting user know that outline is saved
            Toast.makeText(con, getString(R.string.savedOutlineToast), Toast.LENGTH_SHORT).show()
        }

        //make export Button
        view.findViewById<ImageButton>(R.id.printButton).setOnClickListener{
            doWebViewPrint(mEditor.html)

        }

        
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    fun toggleButtonColor(id: Int){

        val button = view?.findViewById<ImageButton>(id)
        if (button != null) {
            //toggle selected boolean
            button.isSelected = !button.isSelected
            Log.i("adding", "button is ${button.isSelected}")
            if(button.isSelected){
                Log.i("adding", "making white")
                button?.setColorFilter(con.getColor(R.color.white))
            }else{
                Log.i("adding", "making grey")
                button?.setColorFilter(con.getColor(R.color.dark_grey))
            }

        }

    }

    /*This printing code was adapted from https://developer.android.com/training/printing/html-docs*/
    private fun doWebViewPrint(htmlDocument:String) {
        // Create a WebView object specifically for printing
        val webView = WebView(con)
        webView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest) = false

            override fun onPageFinished(view: WebView, url: String) {
//                Log.i(TAG, "page finished loading $url")
                createWebPrintJob(view)
                mWebView = null
            }
        }

        // Generate an HTML document on the fly:
//        val htmlDocument =
//                "<html><body><h1>Test Content</h1><p>Testing, testing, testing...</p></body></html>"
        webView.loadDataWithBaseURL(null, htmlDocument, "text/HTML", "UTF-8", null)

        // Keep a reference to WebView object until you pass the PrintDocumentAdapter
        // to the PrintManager
        mWebView = webView
    }

    private fun createWebPrintJob(webView: WebView) {

        // Get a PrintManager instance
        (activity?.getSystemService(Context.PRINT_SERVICE) as? PrintManager)?.let { printManager ->

            val jobName = "${getString(R.string.app_name)} Document"

            // Get a print adapter instance
            val printAdapter = webView.createPrintDocumentAdapter(jobName)

            // Create a print job with name and adapter instance

            printManager.print(
                    jobName,
                    printAdapter,
                    PrintAttributes.Builder().build()
            ).also { printJob ->

                // Save the job object for later status checking
                printManager.printJobs += printJob
            }
        }
    }




}