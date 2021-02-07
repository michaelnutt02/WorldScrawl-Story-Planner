package com.example.wordscrawl.outlines

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.example.wordscrawl.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import jp.wasabeef.richeditor.RichEditor

/**
 * An example full-screen fragment that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class OutlineFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_outline, container, false)

        var navBar: BottomNavigationView = requireActivity().findViewById(R.id.nav_view)
        navBar.visibility = View.GONE
//        navBar.visibility = View.VISIBLE

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
        mEditor.html = "<ul><li>Bullets</li><li>Bullets</li><li>Bullets</li></ul><div><br></div><div>So all we need to do is save to firebaze blush <strike>syrikthrough</strike></div><div><strike><br></strike></div><div><input type=\"checkbox\" name=\"1612419546314\" value=\"1612419546314\">checkbox&nbsp;&nbsp;<br></div>"

        //Set whether the editor is available
        mEditor.setInputEnabled(true);

        //look at all buttons on the menu :)
        view.findViewById<ImageButton>(R.id.italicsButton).setOnClickListener{
            mEditor.focusEditor();
            mEditor.setItalic()
        }

        view.findViewById<ImageButton>(R.id.boldButton).setOnClickListener{
            mEditor.focusEditor();
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

        
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }



}