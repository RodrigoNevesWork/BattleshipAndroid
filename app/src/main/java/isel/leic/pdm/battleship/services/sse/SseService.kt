package isel.leic.pdm.battleship.services.sse

import android.util.Log
import com.google.gson.Gson
import isel.leic.pdm.battleship.domain.Game
import isel.leic.pdm.battleship.http.Uri
import isel.leic.pdm.battleship.http.model.UserInfoOutputModel
import isel.leic.pdm.battleship.preferences.UserInfo
import isel.leic.pdm.battleship.services.sse.models.SseEvent
import isel.leic.pdm.battleship.utils.Requests
import okhttp3.*
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import okhttp3.sse.EventSources

/**
 * The service that handles the sse functionalities.
 *
 * @property httpClient the HTTP client
 * @property jsonEncoder the JSON encoder used to serialize/deserialize objects
 */
class SseService(
    val httpClient: OkHttpClient,
    jsonEncoder: Gson
) : Requests(jsonEncoder = jsonEncoder) {

    private lateinit var eventSource: EventSource

    private val eventSourceListener = object : EventSourceListener() {
        override fun onOpen(eventSource: EventSource, response: Response) {
            super.onOpen(eventSource, response)
            Log.d(TAG, "Connection Opened")
        }

        override fun onClosed(eventSource: EventSource) {
            super.onClosed(eventSource)
            Log.d(TAG, "Connection Closed")
        }

        override fun onEvent(
            eventSource: EventSource,
            id: String?,
            type: String?,
            data: String
        ) {
            Log.d(TAG, "On Event Received! Id: $id, Data: $data")
            id?.let {
                try {
                    // Deserialize the SSE event data based on the event type
                    val event = when (it) {
                        UserInfoOutputModel::class.java.simpleName -> jsonEncoder.fromJson(data, UserInfoOutputModel::class.java)
                        Game::class.java.simpleName -> jsonEncoder.fromJson(data, Game::class.java)
                        else -> SseEvent()
                    }
                    EventBus.postEvent(event)
                } catch (e: Exception) {
                    throw IllegalArgumentException()
                }
            }
        }

        override fun onFailure(eventSource: EventSource, t: Throwable?, response: Response?) {
            super.onFailure(eventSource, t, response)
            Log.d(TAG, "On Failure: ${response?.body}")
        }
    }

    // Start the SSE connection
    fun start(token: String) {
        val request = requestPostBuilder<Nothing>(
            url = getURLtoFetchAPI(Uri.SUBSCRIBE),
            token = token
        )

        eventSource = EventSources
            .createFactory(httpClient)
            .newEventSource(
                request = request,
                listener = eventSourceListener
            )
    }

    fun login(userInfo: UserInfo) {
        val request = requestPostBuilder(
            url = getURLtoFetchAPI(Uri.LOGIN),
            body = userInfo
        )

        eventSource = EventSources
            .createFactory(httpClient)
            .newEventSource(
                request = request,
                listener = eventSourceListener
            )
    }

    // Stop the SSE connection
    fun stop() { eventSource.cancel() }

    companion object {
        private const val TAG = "SSES"
    }
}
