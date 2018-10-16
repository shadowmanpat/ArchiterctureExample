package com.nickagas.architerctureexample

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.widget.ArrayAdapter
import android.widget.ListView
import kotlinx.android.synthetic.main.activity_main.*
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper


class MainActivity : AppCompatActivity() {
    private var mDrawerList: ListView? = null
    private var mAdapter: ArrayAdapter<String>? = null
    lateinit var noteViewModel: NoteViewModel
    private var mDrawerToggle: ActionBarDrawerToggle? = null
    private var mDrawerLayout: DrawerLayout? = null
    private var mActivityTitle: String? = null

    companion object {
        const val ADD_NOTE_REQUEST= 1
        const val EDIT_NOTE_REQUEST= 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.setHasFixedSize(true)
        var adapter = NoteAdapter();
        recycler.adapter = adapter;

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel::class.java)
//        noteViewModel.allNotes.observe(this, Observer<List<Note>>(){
//                    adapter.setNotes(it)
//        })
        noteViewModel.allNotes.observe(this, Observer { notes -> adapter.setNotes(notes) })

        buttton_add_note.setOnClickListener {
            var intent = Intent(applicationContext, AddEditNoteActivity::class.java)
            startActivityForResult(intent, ADD_NOTE_REQUEST)
        }

        val itemTouchCallback = object: ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT or  ItemTouchHelper.RIGHT ) {
            override fun onMove(p0: RecyclerView, p1: RecyclerView.ViewHolder, p2: RecyclerView.ViewHolder): Boolean {
               return false
            }

            override fun onSwiped(p0: RecyclerView.ViewHolder, p1: Int) {
                noteViewModel.delete(adapter.getNoteAt(p0.adapterPosition));
                Toast.makeText(this@MainActivity,"Note Deleted", Toast.LENGTH_SHORT).show()

            }

        }
        val itemTouchHelper = ItemTouchHelper(itemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recycler)

        adapter.setItemOnClicklistener { NoteAdapter.OnItemClickListener {
            var intent = Intent(this,AddEditNoteActivity::class.java)
            intent.putExtra(AddEditNoteActivity.EXTRA_ID, it.id)
            intent.putExtra(AddEditNoteActivity.EXTRA_TITLE, it.title)
            intent.putExtra(AddEditNoteActivity.EXTRA_DESCRIPTION, it.description)
            intent.putExtra(AddEditNoteActivity.EXTRA_PRIORITY, it.priority)
            startActivityForResult(intent, EDIT_NOTE_REQUEST)

        } }

//        val toolbar: Toolbar = findViewById(R.id.toolbar)
//        setSupportActionBar(toolbar)
//        val actionbar: ActionBar? = supportActionBar
//        actionbar?.apply {
//            setDisplayHomeAsUpEnabled(true)
//            setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp)
//        }
//
//
////        (mDrawerToggle as ActionBarDrawerToggle).setDrawerIndicatorEnabled(true);
////        mDrawerLayout?.setDrawerListener(mDrawerToggle);
////
//        mDrawerList = findViewById<ListView>(R.id.navList);
//        mDrawerLayout = findViewById(R.id.drawer_layout);
//        mActivityTitle = getTitle().toString();
//
//        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel::class.java)
//        noteViewModel.allNotes.observe(this, Observer {
//
//            Toast.makeText(this,"onchanged",Toast.LENGTH_LONG).show()
//
//        })
////        mDrawerList = (findViewById<ListView>(R.id.navList))
//
//        addDrawerItems()
//        setupDrawer()
//
//        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar()?.setHomeButtonEnabled(true);

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == ADD_NOTE_REQUEST && resultCode == Activity.RESULT_OK){
            var title = data?.getStringExtra(AddEditNoteActivity.EXTRA_TITLE)
            var description = data?.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION)
            var pririty = data?.getIntExtra(AddEditNoteActivity.EXTRA_DESCRIPTION, 1)
            var note = pririty?.run {
                Note(title,description, this)

            }


            noteViewModel.insert(note)
            Toast.makeText(this,"Note Saved", Toast.LENGTH_SHORT).show()

        }else if(requestCode == EDIT_NOTE_REQUEST && resultCode == Activity.RESULT_OK){
            var id = data?.getIntExtra(AddEditNoteActivity.EXTRA_ID, -1)
            if(id == -1){
                Toast.makeText(this,"Note cant be updated", Toast.LENGTH_SHORT).show()
                return;
            }
            var title = data?.getStringExtra(AddEditNoteActivity.EXTRA_TITLE)
            var description = data?.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION)
            var pririty = data?.getIntExtra(AddEditNoteActivity.EXTRA_DESCRIPTION, 1)
            var note = pririty?.run {
                Note(title,description, this)

            }
            note?.id = id!!
            noteViewModel.update(note)

        }else {
            Toast.makeText(this,"Note Not Saved", Toast.LENGTH_SHORT).show()
        }
    }

//    private fun setupDrawer() {
//        mDrawerToggle = object : ActionBarDrawerToggle(
//                this,
//                drawer_layout_main,
//                R.string.drawer_open,
//                R.string.drawer_close
//        ) {
//            override fun onDrawerClosed(view: View) {
//                super.onDrawerClosed(view);
//                getSupportActionBar()?.setTitle(mActivityTitle);
//                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
////                Toast.makeText(this,"Closed", Toast.LENGTH_SHORT).show
//
//            }
//
//            override fun onDrawerOpened(drawerView: View) {
//                super.onDrawerOpened(drawerView)
//                supportActionBar!!.setTitle("Navigation!")
//                invalidateOptionsMenu() // creates call to onPrepareOptionsMenu()
//            }
//        }
//
//        mDrawerToggle?.setDrawerIndicatorEnabled(true);
//        mDrawerLayout?.setDrawerListener(mDrawerToggle)
//    }

//    private fun addDrawerItems() {
//        val osArray = arrayOf("Android", "iOS", "Windows", "OS X", "Linux")
//        mAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, osArray)
//        navList?.setAdapter(mAdapter)
//    }


}
