package com.swanky.mynotes.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.swanky.mynotes.databinding.RecyclerRowNotesBinding
import com.swanky.mynotes.model.Note
import com.swanky.mynotes.roomdatabase.MyAppDatabase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class NotesAdapter(private var noteList: List<Note>, private val context: Context) :
    RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    private var compositeDisposable : CompositeDisposable? = null

    class ViewHolder(val binding: RecyclerRowNotesBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            RecyclerRowNotesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.noteTxtRecycler.text = noteList[position].noteBody
        holder.binding.recyclerDateTxt.text = noteList[position].saveDate

        holder.binding.recyclerDeleteIcon.setOnClickListener {
            showAlertDialog(context, position)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showAlertDialog(context: Context, position: Int) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle("Notu Sil")
        alertDialogBuilder.setMessage("Seçilen notu silmek istediğinizden emin misiniz?")

        alertDialogBuilder.setPositiveButton("Evet") { _, _ ->
            val database =
                Room.databaseBuilder(context, MyAppDatabase::class.java, "MyDatabase").build()
            val dao = database.getDao()
            compositeDisposable = CompositeDisposable()
            compositeDisposable!!.add(
                dao.delete(noteList[position])
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe())
            val mutableList = noteList.toMutableList()
            mutableList.remove(noteList[position])
            noteList = mutableList
            notifyItemRemoved(position)
        }

        alertDialogBuilder.setNegativeButton("İptal") { _, _ -> }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }


    fun clearMemory() {
        compositeDisposable?.clear()
    }

}