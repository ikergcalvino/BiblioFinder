package com.tfg.bibliofinder.screens.activities

import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.tech.Ndef
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tfg.bibliofinder.databinding.ActivityNfcBinding

class NfcActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNfcBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNfcBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()

        checkNfcStatus()

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            PendingIntent.FLAG_MUTABLE
        )

        val intentFiltersArray = arrayOf(IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED))

        val techListsArray = arrayOf(arrayOf(Ndef::class.java.name))
        getNfcAdapter()?.enableForegroundDispatch(
            this, pendingIntent, intentFiltersArray, techListsArray
        )
    }

    private fun getNfcAdapter(): NfcAdapter? {
        return NfcAdapter.getDefaultAdapter(this)
    }

    private fun checkNfcStatus() {
        val nfcAdapter = getNfcAdapter()
        if (nfcAdapter == null) {
            Toast.makeText(this, "Este dispositivo no admite NFC", Toast.LENGTH_SHORT).show()
            finish()
        } else if (!nfcAdapter.isEnabled) {
            showNfcEnableDialog()
        }
    }

    private fun showNfcEnableDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("NFC desactivado")
        builder.setMessage("Para utilizar esta aplicación, debes habilitar NFC en tu dispositivo.")
        builder.setPositiveButton("Configuración") { _, _ ->
            val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
            startActivity(intent)
        }
        builder.setNegativeButton("Cancelar") { _, _ -> finish() }
        builder.setCancelable(false)
        builder.show()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (intent.action == NfcAdapter.ACTION_TECH_DISCOVERED) {
            val rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            if (rawMessages != null) {
                val messages = arrayOfNulls<NdefMessage>(rawMessages.size)
                for (i in rawMessages.indices) {
                    messages[i] = rawMessages[i] as NdefMessage
                }
                if (messages.isNotEmpty()) {
                    val records = messages[0]?.records
                    if (!records.isNullOrEmpty()) {
                        val payload = records[0].payload
                        val payloadWithoutHeader = payload.copyOfRange(3, payload.size)
                        val text = String(payloadWithoutHeader, Charsets.UTF_8)
                        Log.d("NFC", "Texto recibido: $text")
                    }
                }
            }
        }
    }
}