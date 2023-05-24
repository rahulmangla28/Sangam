package com.example.sangam

import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.widget.Toast
import com.example.sangam.databinding.ActivityFeedbackBinding
import java.net.Authenticator
import java.net.PasswordAuthentication
import java.util.Properties
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class FeedbackActivity : AppCompatActivity() {
    lateinit var binding : ActivityFeedbackBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.coolPinkNav)
        binding = ActivityFeedbackBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Feedback"

        binding.sendFA.setOnClickListener {
            val feedbackMsg = binding.feedbackMsgFA.text.toString() + "\n" + binding.emailFA.text.toString()
            val subject = binding.topicFA.text.toString()
            val userName = "geniuskoder03@gmaiil.com"
            val password = "alpha"
            val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if(feedbackMsg.isNotEmpty() && subject.isNotEmpty() && (connectivityManager.activeNetworkInfo?.isConnectedOrConnecting == true)) {
                Thread {
                    try {
                        val properties = Properties()
                        properties["mail.smtp.auth"] = "true"
                        properties["mail.smtp.starttls.enable"] = "true"
                        properties["mail.smtp.host"] = "smtp.gmail.com"
                        properties["mail.smtp.port"] = "587"
                        val session = javax.mail.Session.getInstance( properties, object  :Authenticator() {
                            override fun getPasswordAuthentication(): PasswordAuthentication {
                                return PasswordAuthentication(userName , password)
                            }
                        })
                        val mail = MimeMessage(session)
                        mail.subject = subject
                        mail.setText(feedbackMsg)
                        mail.setFrom(InternetAddress(userName))
                        mail.setRecipients(Message.RecipientType.TO , InternetAddress.parse(userName))
                        Transport.send(mail)
                    }catch (e : Exception) {
                        Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show()
                    }
                }.start()
                Toast.makeText(this,"Thanks for your feedback !",Toast.LENGTH_SHORT).show()
                finish()
            }else {
                Toast.makeText(this,"Something went Wrong! !",Toast.LENGTH_SHORT).show()
            }
        }
    }
}