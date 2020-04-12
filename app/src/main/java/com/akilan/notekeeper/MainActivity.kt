package com.akilan.notekeeper

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.findNavController

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_first.*
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    private var notePosition = POSITION_NOT_SET

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        notePosition = intent.getIntExtra(EXTRA_NOTE_POSITION, POSITION_NOT_SET)

        if (notePosition != POSITION_NOT_SET) {
            displayNote()
        }
    }

    private fun displayNote() {
        findNavController(R.id.nav_host_fragment).setGraph(R.navigation.nav_graph, intent.extras)
    }


}
