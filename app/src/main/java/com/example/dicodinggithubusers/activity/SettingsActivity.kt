package com.example.dicodinggithubusers.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.example.dicodinggithubusers.R
import com.example.dicodinggithubusers.databinding.SettingsActivityBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: SettingsActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SettingsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.settings, SettingsFragment())
            }
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    class SettingsFragment : PreferenceFragmentCompat(),
        SharedPreferences.OnSharedPreferenceChangeListener {

        private lateinit var languageSetting: String
        private lateinit var notificationSetting: String

        private lateinit var notificationPreference: SwitchPreferenceCompat
        private lateinit var languagePreference: Preference

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.preferences, rootKey)
            init()

            val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            languagePreference.intent = intent
        }

        private fun init() {
            languageSetting = resources.getString(R.string.key_language_setting)
            notificationSetting = resources.getString(R.string.key_notification_setting)

            notificationPreference =
                findPreference<SwitchPreferenceCompat>(notificationSetting) as SwitchPreferenceCompat
            languagePreference = findPreference<Preference>(languageSetting) as Preference

        }

        override fun onResume() {
            super.onResume()
            preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        }

        override fun onPause() {
            super.onPause()
            preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        }

        override fun onSharedPreferenceChanged(
            sharedPreferences: SharedPreferences?,
            key: String?
        ) {
            if (key == notificationSetting)
                notificationPreference.isChecked =
                    sharedPreferences?.getBoolean(notificationSetting, false) ?: false
        }
    }
}