package com.tfg.bibliofinder.screens.activities

import android.app.Activity
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.tech.Ndef
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.widget.Toast
import com.tfg.bibliofinder.R
import com.tfg.bibliofinder.databinding.ActivityNfcBinding

class NfcActivity : Activity() {

    private var nfcAdapter: NfcAdapter? = null
    private lateinit var binding: ActivityNfcBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNfcBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val isNightMode =
            (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

        if (isNightMode) binding.nfcReader.setAnimation(R.raw.nfc_reader_night)
        else binding.nfcReader.setAnimation(R.raw.nfc_reader_day)

        binding.nfcReader.playAnimation()

        checkNfcStatus()
    }

    override fun onResume() {
        super.onResume()

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            PendingIntent.FLAG_MUTABLE
        )
        val intentFiltersArray = arrayOf(IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED))
        val techListsArray = arrayOf(arrayOf(Ndef::class.java.name))
        nfcAdapter?.enableForegroundDispatch(
            this, pendingIntent, intentFiltersArray, techListsArray
        )
    }

    private fun checkNfcStatus() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        if (nfcAdapter == null) {
            Toast.makeText(this, "Este dispositivo no admite NFC", Toast.LENGTH_SHORT).show()
            finish()
        }
        if (!nfcAdapter!!.isEnabled) {
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

                        binding.nfcReader.setAnimation(R.raw.nfc_success)
                        binding.nfcReader.playAnimation()

                        binding.nfcReader.setAnimation(R.raw.nfc_fail)
                        binding.nfcReader.playAnimation()

                        Handler().postDelayed({
                            val intentMain = Intent(this, MainActivity::class.java)
                            startActivity(intentMain)
                            finish()
                        }, 5000)
                    }
                }
            }
        }
    }
}