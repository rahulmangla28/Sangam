package com.example.sangam

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContentProviderCompat.requireContext

class MusicService : Service() , AudioManager.OnAudioFocusChangeListener {
    private var myBinder = MyBinder()
    var mediaPlayer : MediaPlayer ?= null
    private lateinit var mediaSession : MediaSessionCompat
    private lateinit var runnable: Runnable
    lateinit var audioManager: AudioManager

    override fun onBind(p0: Intent?): IBinder {
        mediaSession = MediaSessionCompat(baseContext,"My Music")
        return myBinder
    }

    inner class MyBinder : Binder() {
        fun currentService() : MusicService {
            return this@MusicService
        }
    }

    fun showNotification(playPauseBtn : Int) {
        val intent  = Intent(baseContext,MainActivity::class.java)
        val contentIntent = PendingIntent.getActivity(this,0,intent, 0)

        val prevIntent = Intent(baseContext, NotificationReceiver::class.java).setAction(ApplicationClass.PREVIOUS)
        val prevPendingIntent = PendingIntent.getBroadcast(baseContext,0,prevIntent,PendingIntent.FLAG_UPDATE_CURRENT)

        val playIntent = Intent(baseContext, NotificationReceiver::class.java).setAction(ApplicationClass.PLAY)
        val playPendingIntent = PendingIntent.getBroadcast(baseContext,0,playIntent,PendingIntent.FLAG_UPDATE_CURRENT)

        val nextIntent = Intent(baseContext, NotificationReceiver::class.java).setAction(ApplicationClass.NEXT)
        val nextPendingIntent = PendingIntent.getBroadcast(baseContext,0,nextIntent,PendingIntent.FLAG_UPDATE_CURRENT)

        val exitIntent = Intent(baseContext, NotificationReceiver::class.java).setAction(ApplicationClass.EXIT)
        val exitPendingIntent = PendingIntent.getBroadcast(baseContext,0,exitIntent,PendingIntent.FLAG_UPDATE_CURRENT)

        val imgArt = getImgArt(PlayerActivity.musicListPA[PlayerActivity.songPosition].path)
        val image = if(imgArt!=null)  {
            BitmapFactory.decodeByteArray(imgArt,0,imgArt.size)
        }else {
            BitmapFactory.decodeResource(resources,R.drawable.sangam_logo)
        }

        val notification = NotificationCompat.Builder(baseContext,ApplicationClass.CHANNEL_ID)
            .setContentIntent(contentIntent)
            .setContentTitle(PlayerActivity.musicListPA[PlayerActivity.songPosition].title)
            .setContentText(PlayerActivity.musicListPA[PlayerActivity.songPosition].artist)
            .setSmallIcon(R.drawable.ic_playlist)
            .setLargeIcon(image)
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle().setMediaSession(mediaSession.sessionToken))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)
            .addAction(R.drawable.ic_previous, "Previous",prevPendingIntent)
            .addAction(playPauseBtn, "Play",playPendingIntent)
            .addAction(R.drawable.ic_next, "Next",nextPendingIntent)
            .addAction(R.drawable.ic_exit, "Exit",exitPendingIntent)
            .build()

        startForeground(1,notification)
    }

    fun createMediaPlayer() {
        try {
            if(PlayerActivity.musicService!!.mediaPlayer == null) PlayerActivity.musicService!!.mediaPlayer = MediaPlayer()
            PlayerActivity.musicService!!.mediaPlayer!!.reset()
            PlayerActivity.musicService!!.mediaPlayer!!.setDataSource(PlayerActivity.musicListPA[PlayerActivity.songPosition].path)
            PlayerActivity.musicService!!.mediaPlayer!!.prepare()
            PlayerActivity.binding.playPauseBtnPA.setIconResource(R.drawable.ic_pause)
            PlayerActivity.musicService!!.showNotification(R.drawable.ic_pause)
            PlayerActivity.binding.tvSeekbarStart.text = formatDuration(PlayerActivity.musicService!!.mediaPlayer!!.currentPosition.toLong())
            PlayerActivity.binding.tvSeekbarEnd.text = formatDuration(PlayerActivity.musicService!!.mediaPlayer!!.duration.toLong())
            PlayerActivity.binding.seekbarPA.progress = 0
            PlayerActivity.binding.seekbarPA.max = PlayerActivity.musicService!!.mediaPlayer!!.duration
            PlayerActivity.nowPlayingID = PlayerActivity.musicListPA[PlayerActivity.songPosition].id
        }catch (e : Exception){return}
    }

    fun seekbarSetup() {
        runnable = Runnable {
            PlayerActivity.binding.tvSeekbarStart.text = formatDuration(PlayerActivity.musicService!!.mediaPlayer!!.currentPosition.toLong())
            PlayerActivity.binding.seekbarPA.progress = mediaPlayer!!.currentPosition
            Handler(Looper.getMainLooper()).postDelayed(runnable,200)
        }
        Handler(Looper.getMainLooper()).postDelayed(runnable,0)
    }

    override fun onAudioFocusChange(focusChange: Int) {
        if(focusChange <= 0)  {
            // pause music
            PlayerActivity.binding.playPauseBtnPA.setIconResource(R.drawable.ic_play)
            NowPlaying.binding.playPauseBtnNP.setIconResource(R.drawable.ic_play)
            PlayerActivity.musicService!!.showNotification(R.drawable.ic_play)
            PlayerActivity.isPlaying = false
            PlayerActivity.musicService!!.mediaPlayer!!.pause()
        }else {
            // play music
            PlayerActivity.binding.playPauseBtnPA.setIconResource(R.drawable.ic_pause)
            NowPlaying.binding.playPauseBtnNP.setIconResource(R.drawable.ic_pause)
            PlayerActivity.musicService!!.showNotification(R.drawable.ic_pause)
            PlayerActivity.isPlaying = true
            PlayerActivity.musicService!!.mediaPlayer!!.start()
        }
    }
}