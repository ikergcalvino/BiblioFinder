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
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
import com.tfg.bibliofinder.R
import com.tfg.bibliofinder.databinding.ActivityNfcBinding
import com.tfg.bibliofinder.services.WorkstationService
import com.tfg.bibliofinder.services.exceptions.WorkstationNotAvailableException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class NfcActivity : Activity() {

    private lateinit var binding: ActivityNfcBinding

    private var nfcAdapter: NfcAdapter? = null
    private val workstationService: WorkstationService by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNfcBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.nfcText.setText(R.string.scan_nfc_tag)

        if ((resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) {
            binding.nfcIcon.setAnimation(R.raw.nfc_reader_night)
        } else {
            binding.nfcIcon.setAnimation(R.raw.nfc_reader_day)
        }

        binding.nfcIcon.playAnimation()

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

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        if (intent.action == NfcAdapter.ACTION_TECH_DISCOVERED) {
            intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)?.also { rawMessages ->
                val messages: List<NdefMessage> = rawMessages.map { it as NdefMessage }

                if (messages.isNotEmpty() && !messages[0].records.isNullOrEmpty()) {
                    val payload = messages[0].records[0].payload

                    val payloadWithoutHeader = payload.copyOfRange(3, payload.size)
                    val workstationId = String(payloadWithoutHeader, Charsets.UTF_8).toLongOrNull()

                    CoroutineScope(Dispatchers.Main).launch {
                        try {
                            workstationId?.let { workstationService.occupyWorkstation(it) }

                            binding.nfcText.setText(R.string.workstation_occupied)
                            binding.nfcIcon.setAnimation(R.raw.nfc_success)
                        } catch (e: WorkstationNotAvailableException) {
                            binding.nfcText.setText(R.string.workstation_not_occupied)
                            binding.nfcIcon.setAnimation(R.raw.nfc_fail)
                        }
                    }

                    binding.nfcIcon.playAnimation()

                    Handler(Looper.getMainLooper()).postDelayed({
                        val mainIntent = Intent(this@NfcActivity, MainActivity::class.java)
                        startActivity(mainIntent)
                        finish()
                    }, 2500)
                }
            }
        }
    }

    private fun checkNfcStatus() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        if (nfcAdapter == null) {
            Toast.makeText(
                applicationContext, getString(R.string.unsupported_nfc_device), Toast.LENGTH_SHORT
            ).show()
            finish()
        }

        if (nfcAdapter?.isEnabled != true) {
            AlertDialog.Builder(this).apply {
                setTitle(getString(R.string.nfc_disabled))
                setMessage(getString(R.string.enable_nfc))
                setPositiveButton(getString(R.string.settings)) { _, _ ->
                    val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(settingsIntent)
                }
                setNegativeButton(getString(R.string.cancel)) { _, _ -> finish() }
                setCancelable(false)
            }.show()
        }
    }
}