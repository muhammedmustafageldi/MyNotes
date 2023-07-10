package com.swanky.mynotes.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.swanky.mynotes.R
import com.swanky.mynotes.adapter.NotesAdapter
import com.swanky.mynotes.databinding.ActivityMainBinding
import com.swanky.mynotes.model.Note
import com.swanky.mynotes.roomdatabase.MyAppDatabase
import com.swanky.mynotes.roomdatabase.MyDao
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: MyAppDatabase
    private lateinit var dao: MyDao
    private lateinit var compositeDisposable: CompositeDisposable
    private var adapter : NotesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        getAllNotes()
    }

    private fun init() {
        database = Room.databaseBuilder(applicationContext, MyAppDatabase::class.java, "MyDatabase")
            .build()
        dao = database.getDao()
        compositeDisposable = CompositeDisposable()

        binding.addNoteFab.setOnClickListener {
            startActivity(Intent(this@MainActivity, AddNoteActivity::class.java))
        }
    }

    private fun getAllNotes() {
        compositeDisposable.add(
            dao.getAll().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse)
        )
    }

    private fun handleResponse(noteList: List<Note>) {
        val recyclerView: RecyclerView = binding.notesRecycler
        if (noteList.isNotEmpty()) {
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)

            adapter = NotesAdapter(noteList, this@MainActivity)
            recyclerView.adapter = adapter

            recyclerView.visibility = View.VISIBLE
            recyclerItemAnimation(recyclerView)
            binding.notFoundLayout.visibility = View.GONE
        } else {
            recyclerView.visibility = View.GONE
            binding.notFoundLayout.visibility = View.VISIBLE
        }
    }

    private fun recyclerItemAnimation(recyclerView: RecyclerView) {
        val context = recyclerView.context
        val controller =
            AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down)
        recyclerView.layoutAnimation = controller
        recyclerView.scheduleLayoutAnimation()
    }


    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
        binding.notFoundAnim.cancelAnimation()
        adapter?.clearMemory()
    }




}