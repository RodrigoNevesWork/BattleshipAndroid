package isel.leic.pdm.battleship.utils

import com.google.gson.*
import okhttp3.Response
import java.lang.reflect.Type

fun <T> Response.extractProperties(type: Type): T {
    try{
        return Gson().fromJson(body?.string(), type)
    } catch (err: JsonSyntaxException){
        throw err
    }
}

private fun Response.extractProblemJson(): ProblemJson {
    try{
        return Gson().fromJson(body?.string(), ProblemJson::class.java)
    } catch (err: JsonSyntaxException) {
        throw err
    }
}

fun Response.check(): Response {
    if (body?.contentType() == ProblemJson.MEDIA_TYPE)
        throw extractProblemJson()
    else return this
}
