package com.mailslurp.examples

import com.mailslurp.apis.EmailControllerApi
import com.mailslurp.apis.InboxControllerApi
import com.mailslurp.infrastructure.ClientException
import org.awaitility.Awaitility.await
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals


@RunWith(JUnit4::class)
class MailSlurpKotlinTest {

    companion object {
        private const val apiKey = "ddfa1f7fda5fab9d51b7242a68b31c9cad5666b590a8980592731fe5f7a8574b"
        private const val TIMEOUT = 60_000L
    }

    @Test
    fun `can send and receive email`() {
        // create inbox
        val inboxController = InboxControllerApi(apiKey)
        val waitForController = EmailControllerApi(apiKey)
        val inbox = inboxController.createInbox(null, null, null, null, null, null, null, null, null, null)

        // do some action: configure notifications for this email, run business flow, etc...
        println("Email address: ${inbox.emailAddress}")

        await()
            .ignoreException(ClientException::class.java)
            .atMost(TIMEOUT, TimeUnit.MILLISECONDS).until {
                val email = waitForController.getLatestEmailInInbox(
                    inboxId = inbox.id!!
                )

                assertEquals(email.subject, "my-test-subject")
                assertEquals(email.to?.single(), inbox.emailAddress)
//            assertEquals(email.from, "dev-test@tradeix.com")

                true
            }
    }
}
