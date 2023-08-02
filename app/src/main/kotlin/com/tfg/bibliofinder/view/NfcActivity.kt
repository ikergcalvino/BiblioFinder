package com.tfg.bibliofinder.view

import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.tfg.bibliofinder.viewmodel.RegisterNfcViewModel

class NfcActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val nfcAdapter: NfcAdapter? = NfcAdapter.getDefaultAdapter(this)
        if (nfcAdapter != null) {
            handleNfcIntent(intent)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleNfcIntent(intent)
    }

    private fun handleNfcIntent(intent: Intent) {
        if (NfcAdapter.ACTION_TECH_DISCOVERED == intent.action) {
            val tag: Tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG) ?: return
            // Extract and log NFC data here
            val tagId = tag.id.joinToString(":") { it.toString(16).padStart(2, '0') }
            Log.d("NFC_TAG", "Tag ID: $tagId")

            // Update ViewModel with NFC data
            val viewModel = ViewModelProvider(this)[RegisterNfcViewModel::class.java]
            viewModel.setNfcData("Tag ID: $tagId")
        }
    }
}