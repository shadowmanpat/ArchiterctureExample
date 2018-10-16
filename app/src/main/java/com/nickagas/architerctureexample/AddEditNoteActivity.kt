package com.nickagas.architerctureexample

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_note.*

class AddEditNoteActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_ID = "com.nickagas.architerctureexample.EXTRA_ID"
        const val EXTRA_TITLE = "com.nickagas.architerctureexample.EXTRA_TITLE"
        const val EXTRA_DESCRIPTION = "com.nickagas.architerctureexample.EXTRA_DESCRIPTION"
        const val EXTRA_PRIORITY = "com.nickagas.architerctureexample.EXTRA_PRIORITY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        number_picker_priority.minValue = 1
        number_picker_priority.maxValue =10
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)

        var intent = intent
        if(intent.hasExtra(EXTRA_ID)){
            setTitle("Edit Note")
            edit_text_title.setText(intent.getStringExtra(EXTRA_TITLE))
            edit_text_description.setText(intent.getStringExtra(EXTRA_DESCRIPTION))
            number_picker_priority.value = intent.getIntExtra(EXTRA_PRIORITY, 1)

        } else {
            setTitle("Add Note")
        }


        save_fab.setOnClickListener {
            saveNote()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        var menuInflater = menuInflater
        menuInflater.inflate(R.menu.add_note_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if(item?.itemId==R.id.save_note){
            saveNote()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun saveNote() {
        var title = edit_text_title.text.toString()
        var description = edit_text_description.text.toString()
        var priority = number_picker_priority.value

        if(title.isEmpty() || description.isEmpty()){
            Toast.makeText(this, "Please insert a tiitle and a description", Toast.LENGTH_SHORT).show()
            return
        }

        val data = Intent()
        data.putExtra(EXTRA_TITLE, title)
        data.putExtra(EXTRA_DESCRIPTION, description)
        data.putExtra(EXTRA_PRIORITY, priority)

        var id =intent.getIntExtra(EXTRA_ID, -1)
        if (id !=-1){
            data.putExtra(EXTRA_ID,id)
        }
        setResult(Activity.RESULT_OK,data)
        finish()

     }
}
