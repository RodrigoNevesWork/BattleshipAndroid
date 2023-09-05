package isel.leic.pdm.battleship.utils

import okhttp3.*
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * Extension function used to send [this] request using [okHttpClient].
 *
 * @receiver the request to be sent
 * @param okHttpClient  the client from where the request is sent
 * @return the response
 * @throws  [IOException] if a communication error occurs.
 * @throws  [Throwable] if any error is thrown by the response handler.
 */
suspend fun Request.send(okHttpClient: OkHttpClient): Response =
    suspendCoroutine { continuation ->
        okHttpClient.newCall(request = this).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                continuation.resumeWithException(e)
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    continuation.resume(response)
                }
                catch (t: Throwable) {
                    continuation.resumeWithException(t)
                }
            }
        })
    }
