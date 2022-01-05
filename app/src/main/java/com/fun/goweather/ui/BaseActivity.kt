package com.`fun`.goweather.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.R
import android.app.Dialog
import android.view.Window
import android.widget.Button
import android.widget.TextView
import dagger.android.AndroidInjection

abstract class BaseActivity : AppCompatActivity() {
    abstract val contentResourceId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(contentResourceId)
        AndroidInjection.inject(this)
    }

    fun setUpActionBar(title: String) {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = title
    }

    fun showErrorDialog(message: String) {
        val dlg: AlertDialog.Builder = AlertDialog.Builder(this)
        dlg.setTitle("Note")
        dlg.setMessage(message)
        dlg.setPositiveButton(
            getString(R.string.ok)
        ) { _, _ ->
            finish()
        }.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}