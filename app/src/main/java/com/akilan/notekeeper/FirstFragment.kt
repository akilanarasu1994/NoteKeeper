package com.akilan.notekeeper

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_first.*
import timber.log.Timber

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {
    private var notePosition = POSITION_NOT_SET

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.getInt(NOTE_POSITION, POSITION_NOT_SET)?.let {
            notePosition = it
        }
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
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_first).setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        val adapterCourses = ArrayAdapter<CourseInfo>(requireContext(),
            android.R.layout.simple_spinner_item,
            DataManager.courses.values.toList())
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerCourses.adapter = adapterCourses

        if (notePosition != POSITION_NOT_SET) {
            displayNote()
        } else {
            DataManager.notes.add(NoteInfo())
            notePosition = DataManager.notes.lastIndex
        }
    }

    private fun displayNote() {
        val note = DataManager.notes[notePosition]
        textNoteTitle.setText(note.title)
        textNoteText.setText(note.text)

        val coursePosition = DataManager.courses.values.indexOf(note.course)
        spinnerCourses.setSelection(coursePosition)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        if (notePosition >= DataManager.notes.lastIndex) {
            val menuItem = menu.findItem(R.id.action_next)
            menuItem.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_block_white_24dp)
            menuItem.isEnabled = false
            notePosition = 0
        }
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            R.id.action_next -> {
                moveNext()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun moveNext() {
//        notePosition = (notePosition + 1) % DataManager.notes.size
        ++notePosition
        displayNote()
        Timber.d("moveNext(): notePosition: $notePosition")
        requireActivity().invalidateOptionsMenu()
    }

    override fun onPause() {
        super.onPause()
        saveNote()
    }

    private fun saveNote() {
        if (notePosition != POSITION_NOT_SET) {
            val note = DataManager.notes[notePosition]
            note.title = textNoteTitle.text.toString()
            note.text = textNoteText.text.toString()
            note.course = spinnerCourses.selectedItem as CourseInfo
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(NOTE_POSITION, notePosition)
        Timber.d("Saved note position: $notePosition")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        notePosition = savedInstanceState?.getInt(NOTE_POSITION, POSITION_NOT_SET) ?: arguments?.getInt(
            NOTE_POSITION, POSITION_NOT_SET) ?: POSITION_NOT_SET
        Timber.d("Restored Note position: $notePosition")
    }
}
