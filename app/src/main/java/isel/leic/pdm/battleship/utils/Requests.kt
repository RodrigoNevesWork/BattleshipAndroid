package isel.leic.pdm.battleship.utils

import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

open class Requests(
    private val jsonEncoder: Gson
) {

    private val URL_API = "https://c389-161-230-135-128.ngrok.io"
    fun getURLtoFetchAPI(uri: String) = "$URL_API$uri"

    private fun <T> T.requestBodyBuilder(): RequestBody {
        val json = jsonEncoder.toJson(this)
        return json.toRequestBody("application/json".toMediaTypeOrNull())
    }

    fun  <T> requestPostBuilder(url: String, body: T? = null, token: String? = null): Request {
        val request = Request.Builder()
            .url(url)
            .addHeaders(token)
            .post(body.requestBodyBuilder())

        return request.build()
    }

    fun  <T> requestPutBuilder(url: String, body: T, token: String? = null): Request {
        val request = Request.Builder()
            .url(url)
            .addHeaders(token)
            .put(body.requestBodyBuilder())
        return request.build()
    }

    fun requestGetBuilder(url: String, token: String? = null): Request {
        val request = Request.Builder()
            .url(url)
            .addHeaders(token)
        return request.build()
    }

    private fun Request.Builder.addHeaders(token: String?) : Request.Builder{
         this
            .header("Connection", "keep-alive")
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept-Encoding", "gzip, deflate, br")
        if(token != null) this.addHeader("Authorization", "Bearer $token")
        return this
    }
}
