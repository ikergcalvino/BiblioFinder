package com.tfg.bibliofinder.screens.activities

import android.app.Activity
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.content.res.Configuration
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.tech.Ndef
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import com.tfg.bibliofinder.R
import com.tfg.bibliofinder.data.local.database.AppDatabase
import com.tfg.bibliofinder.databinding.ActivityNfcBinding
import com.tfg.bibliofinder.entities.Workstation
import com.tfg.bibliofinder.util.MessageUtil
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject

class NfcActivity : Activity() {

    private lateinit var binding: ActivityNfcBinding

    private var nfcAdapter: NfcAdapter? = null
    private val database: AppDatabase by inject()
    private val sharedPrefs: SharedPreferences by inject()

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

    private fun checkNfcStatus() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        if (nfcAdapter == null) {
            MessageUtil.showToast(applicationContext, getString(R.string.unsupported_nfc_device))
            finish()
        }

        if (!nfcAdapter!!.isEnabled) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.nfc_disabled))
            builder.setMessage(getString(R.string.enable_nfc))
            builder.setPositiveButton(getString(R.string.settings)) { _, _ ->
                val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(intent)
            }
            builder.setNegativeButton(getString(R.string.cancel)) { _, _ -> finish() }
            builder.setCancelable(false)
            builder.show()
        }
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

    @OptIn(DelicateCoroutinesApi::class)
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        val loggedInUserId = sharedPrefs.getLong("userId", 0L)

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
                        val workstationId =
                            String(payloadWithoutHeader, Charsets.UTF_8).toLongOrNull()

                        GlobalScope.launch(Dispatchers.Main) {
                            val workstation = withContext(Dispatchers.IO) {
                                workstationId?.let {
                                    database.workstationDao().getWorkstationById(it)
                                }
                            }

                            if (workstation?.state == Workstation.WorkstationState.AVAILABLE || (workstation?.state == Workstation.WorkstationState.BOOKED && workstation.userId == loggedInUserId)) {
                                workstation.state = Workstation.WorkstationState.OCCUPIED
                                workstation.userId = loggedInUserId

                                database.workstationDao().updateWorkstation(workstation)

                                binding.nfcText.setText(R.string.workstation_occupied)
                                binding.nfcIcon.setAnimation(R.raw.nfc_success)
                            } else {
                                binding.nfcText.setText(R.string.workstation_not_occupied)
                                binding.nfcIcon.setAnimation(R.raw.nfc_fail)
                            }

                            binding.nfcIcon.playAnimation()

                            Handler().postDelayed({
                                val intentMain = Intent(this@NfcActivity, MainActivity::class.java)
                                startActivity(intentMain)
                                finish()
                            }, 2500)
                        }
                    }
                }
            }
        }
    }
}