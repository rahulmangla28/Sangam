package com.example.sangam

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sangam.databinding.ActivityPlaylistBinding
import com.example.sangam.databinding.AddPlaylistDialogBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PlaylistActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPlaylistBinding
    private lateinit var adapter : PlaylistViewAdapter

    companion object {
        var musicPlaylist : MusicPlaylist = MusicPlaylist()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.coolPink)

        binding = ActivityPlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tmpList = ArrayList<String>()
        binding.playlistRV.setHasFixedSize(true)
        binding.playlistRV.setItemViewCacheSize(12)
        binding.playlistRV.layoutManager = GridLayoutManager(this,2)
        adapter = PlaylistViewAdapter(this, playlistList = musicPlaylist.ref)
        binding.playlistRV.adapter = adapter

        binding.backBtnPLA.setOnClickListener { finish() }
        binding.addPlaylistBtn.setOnClickListener {
            customAlertDialog()
        }
    }

    private fun customAlertDialog() {
        val customDialog = LayoutInflater.from(this).inflate(R.layout.add_playlist_dialog,binding.root,false)
        val binder = AddPlaylistDialogBinding.bind(customDialog)
        val builder = MaterialAlertDialogBuilder(this)
        builder.setView(customDialog)
            .setTitle("Playlist Details")
            .setMessage("Do you want to close Sangam ?")
            .setPositiveButton("Add") { dialog, _ ->
                val playlistName = binder.playlistName.text
                val createdBy = binder.yourName.text
                if(playlistName != null && createdBy != null) {
                    if(playlistName.isNotEmpty() && createdBy.isNotEmpty()) {
                        addPlaylist(playlistName.toString(),createdBy.toString())
                    }
                }
                dialog.dismiss()
            }
            .show()
    }

    private fun addPlaylist(name : String, createdBy: String) {
        var playlistExist = false
        for(i in musicPlaylist.ref) {
            if(name.equals(i.name)) {
                playlistExist = true
                break
            }
        }
        if(playlistExist) Toast.makeText(this, "Playlist Exists !!",Toast.LENGTH_SHORT).show()
        else {
            // to get current date
            val calendar = Calendar.getInstance().time
            val sdf = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
            // assigning values to playlist
            val tmpPlaylist = Playlist()
            tmpPlaylist.name = name
            tmpPlaylist.playlist = ArrayList()
            tmpPlaylist.createdBy = createdBy
            tmpPlaylist.createdOn = sdf.format(calendar)
            musicPlaylist.ref.add(tmpPlaylist)
            adapter.refreshPlaylist()
        }
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }
}