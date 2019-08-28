package com.akki.tictock

import android.annotation.SuppressLint
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity(), View.OnClickListener {

    var player1 = true
    var player1Count = 0
    var player2Count = 0
    var isAnyWin = false
    private val TAG = "MainActivity"

    @SuppressLint("SetTextI18n")
    override fun onClick(v: View?) {
        if (v is Button) {
            if (isAnyWin) return
            if (!TextUtils.isEmpty(v.text.toString())) return
            if (player1) {
                v.text = "X"
                player1 = false
            } else {
                v.text = "0"
                player1 = true
            }

            // check if anybody wins
            if (checkIfAnybodyWins()) {
                isAnyWin = true
                var message: String? = null
                if (!player1) {
                    ++player1Count
                    tv_player_a.text = "Player X : $player1Count"
                    message = "Player X wins"
                    //  Toast.makeText(this, "Player X Wins", Toast.LENGTH_SHORT).show()
                } else {
                    ++player2Count
                    tv_player_b.text = "Player 0 : $player2Count"
                    message = "Player 0 wins"
                    // Toast.makeText(this, "Player 0 Wins", Toast.LENGTH_LONG).show()
                }

                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
                mMediaPlayer.start()
              //  val handler = Handler()
                openPopup(message)
              //  handler.postDelayed({ resetGrid() }, 2000)

            } else {
                isAnyWin = false
            }

        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: ")
    }

    private fun openPopup(message: String) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton(android.R.string.yes) { dialog, which ->
                // Continue with delete operation
                resetGrid()
                dialog.cancel()
            }
            .show()
    }

    var stringarray = Array(3) { Array(3) { String() } }
    private fun checkIfAnybodyWins(): Boolean {
        for (i in 0 until 3) {
            for (j in 0 until 3) {
                stringarray[i][j] = (button[i][j] as Button).text.toString()
            }
        }

        // for rows
        for (i in 0 until 3) {
            if (stringarray[i][0] == stringarray[i][1] && stringarray[i][0] == stringarray[i][2] && !TextUtils.isEmpty(
                    stringarray[i][0]
                )
            ) {
                (button[i][0] as Button).setBackgroundColor(resources.getColor(R.color.colorPrimary))
                (button[i][1] as Button).setBackgroundColor(resources.getColor(R.color.colorPrimary))
                (button[i][2] as Button).setBackgroundColor(resources.getColor(R.color.colorPrimary))
                return true
            }
        }

        // for columns
        for (i in 0 until 3) {
            if (stringarray[0][i] == stringarray[1][i] && stringarray[0][i] == stringarray[2][i] && !TextUtils.isEmpty(
                    stringarray[0][i]
                )
            ) {
                (button[0][i] as Button).setBackgroundColor(resources.getColor(R.color.colorPrimary))
                (button[1][i] as Button).setBackgroundColor(resources.getColor(R.color.colorPrimary))
               (button[2][i] as Button).setBackgroundColor(resources.getColor(R.color.colorPrimary))
                return true
            }
        }

        // for diagonal
        if (stringarray[0][0] == stringarray[1][1] && stringarray[0][0] == stringarray[2][2] && !TextUtils.isEmpty(
                stringarray[0][0]
            )
        ) {
            (button[0][0] as Button).setBackgroundColor(resources.getColor(R.color.colorPrimary))
            (button[1][1] as Button).setBackgroundColor(resources.getColor(R.color.colorPrimary))
            (button[2][2] as Button).setBackgroundColor(resources.getColor(R.color.colorPrimary))
            return true
        }

        // for diagonal
        if (stringarray[0][2] == stringarray[1][1] && stringarray[0][2] == stringarray[2][0] && !TextUtils.isEmpty(
                stringarray[0][2]
            )
        ) {
            (button[0][2] as Button).setBackgroundColor(resources.getColor(R.color.colorPrimary))
            (button[1][1] as Button).setBackgroundColor(resources.getColor(R.color.colorPrimary))
            (button[2][0] as Button).setBackgroundColor(resources.getColor(R.color.colorPrimary))
            return true
        }

        return false


    }

    val button = Array(3) { Array(3) { Any() } }
    lateinit var mMediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        mMediaPlayer = MediaPlayer.create(this, R.raw.game_win)
        for (i in 0 until 3) {
            for (j in 0 until 3) {
                val stringId = "btn_$i$j"
                val id = resources.getIdentifier(stringId, "id", packageName)
                button[i][j] = findViewById<Button>(id)
                (button[i][j] as Button).setOnClickListener(this)
            }
        }

        btn_reset.setOnClickListener { resetGrid() }


    }

    private fun resetGrid() {
        isAnyWin = false
        for (i in 0 until 3) {
            for (j in 0 until 3) {
                stringarray[i][j] = ""
                (button[i][j] as Button).text = ""
                (button[i][j] as Button).setBackgroundColor(resources.getColor(android.R.color.transparent))
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
