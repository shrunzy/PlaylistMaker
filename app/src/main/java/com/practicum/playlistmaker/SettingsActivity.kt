package com.practicum.playlistmaker

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.switchmaterial.SwitchMaterial



class SettingsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Находим LinearLayout по id
        val userAgreementLayout = findViewById<LinearLayout>(R.id.user_agreement_layout)
        val emailSupportLayout = findViewById<LinearLayout>(R.id.email_support_layout)
        val shareAppLayout = findViewById<LinearLayout>(R.id.share_app_layout)



        // обработчик нажатия
        userAgreementLayout.setOnClickListener {
            //Toast.makeText(this, "user agreement", Toast.LENGTH_SHORT).show()
            openUserAgreement()
        }

        emailSupportLayout.setOnClickListener {
            openEmailSupport()
        }

        shareAppLayout.setOnClickListener {
            shareAppLayout()

        }


        val switchDarkTheme: SwitchMaterial = findViewById(R.id.switch_notifications)
        switchDarkTheme.isChecked = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
        switchDarkTheme.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Включаем тёмную тему
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                // Выключаем (светлая тема)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        //выход из активити по кнопке назад
        findViewById<ImageView>(R.id.settings_back).setOnClickListener {
            finish()
        }
    }

    private fun openUserAgreement() {
        val url = getString(R.string.agreement_url)

        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
        }
            //startActivity(intent)
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "Не удалось открыть браузер", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Произошла ошибка", Toast.LENGTH_SHORT).show()
        }
    }

    private fun shareAppLayout() {
        val shareAppLayout = Intent(Intent.ACTION_SEND)
        shareAppLayout.type = "text/plain"
        shareAppLayout.putExtra(Intent.EXTRA_SUBJECT, "Курс по Андроид-разработке в Практикуме")
        shareAppLayout.putExtra(Intent.EXTRA_TEXT, "https://practicum.yandex.ru/profile/android-developer-plus/?from=learn_subscriptions-with-prof-recommendations")

        try {
            startActivity(Intent.createChooser(shareAppLayout, "Поделиться приложением"))

        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "Не удалось найти подходящее приложение", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Произошла ошибка", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openEmailSupport() {
        //Toast.makeText(this, "email support", Toast.LENGTH_SHORT).show()
        val emailAddress = getString(R.string.email_support)
        val subject = getString(R.string.email_subject)
        val text = getString(R.string.email_text)

        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            //type = "message/rfc822"
            data = Uri.parse("mailto:$emailAddress")
                //putExtra(Intent.EXTRA_EMAIL, arrayOf(emailAddress))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, text)
        }
            //startActivity(emailIntent)
        try {
            // createChooser
            startActivity(
                Intent.createChooser(emailIntent, "Отправить письмо через...")
            )
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "Почтовое приложение не найдено", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Не удалось открыть почту", Toast.LENGTH_SHORT).show()
        }
    }

}