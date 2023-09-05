package isel.leic.pdm.battleship.activities.about

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import isel.leic.pdm.battleship.R

class AboutActivity: ComponentActivity() {
    companion object {
        fun navigate(context: Context) {
            with(context) {
                val intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AboutScreen(
                onEmailSend = { openSendEmail() },
                onBackRequested = { finish() },
                authors = authors
            )
        }
    }

    private fun openSendEmail() {
        try {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(authors.map { it.email }))
                putExtra(Intent.EXTRA_SUBJECT, emailSubject)
            }

            startActivity(intent)
        }
        catch (e: ActivityNotFoundException) {
            //Log.e(TAG, "Failed to send email", e)
            Toast
                .makeText(
                    this,
                    R.string.activity_info_no_suitable_app,
                    Toast.LENGTH_LONG
                )
                .show()
        }
    }
}

data class Author(
    val number: Int,
    val name: String,
    val email: String
)

private val authors = listOf(
    Author(46536, "Rodrigo Neves", "A46536@alunos.isel.pt")
    )

private const val emailSubject = "About the Battleship App"
