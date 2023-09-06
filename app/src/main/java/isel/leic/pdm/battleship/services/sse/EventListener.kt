package isel.leic.pdm.battleship.services.sse

import isel.leic.pdm.battleship.services.sse.models.SseEvent


interface SseEventListener {
    fun onEventReceived(eventData: SseEvent)
}
