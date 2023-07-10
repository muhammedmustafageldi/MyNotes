package com.swanky.mynotes.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import com.swanky.mynotes.databinding.ActivityAddNoteBinding
import com.swanky.mynotes.model.Note
import com.swanky.mynotes.roomdatabase.MyAppDatabase
import com.swanky.mynotes.roomdatabase.MyDao
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddNoteActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAddNoteBinding
    private lateinit var database : MyAppDatabase
    private lateinit var dao: MyDao
    private lateinit var compositeDisposable : CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

        binding.saveButton.setOnClickListener{
            val noteString = binding.addNoteTxt.text.toString()
            if (noteString == ""){
                binding.addNoteTxt.error = "Please enter your note."
            }else{
                //Get date
                val currentTimeMillis = System.currentTimeMillis()
                val date = Date(currentTimeMillis)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(date)
                addNote(noteString, formattedDate)
            }
        }
    }

    private fun init() {
        database = Room.databaseBuilder(applicationContext, MyAppDatabase::class.java, "MyDatabase")
            .build()
        dao = database.getDao()
        compositeDisposable = CompositeDisposable()
    }

    private fun addNote(note : String, saveDate : String){
        compositeDisposable = CompositeDisposable(dao.insert(Note(note, saveDate))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this@AddNoteActivity :: noteAdded))
    }

    private fun noteAdded(){
        val intent = Intent(this@AddNoteActivity, MainActivity :: class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable?.let {

        }
    }

}
