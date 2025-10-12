package com.lyrics.feelin.util

import android.util.Log
import com.lyrics.feelin.core.data.datasource.remote.dto.ServerErrorDto
import kotlinx.serialization.json.Json
import retrofit2.HttpException

@Suppress("TooGenericExceptionCaught")
fun HttpException.toServerErrorDto(): ServerErrorDto {
    try {
        return Json.decodeFromString<ServerErrorDto>(string = response()!!.errorBody()!!.string())
    } catch (e: NullPointerException) {
        Log.e("ServerErrorDto", "ServerErrorDto parse error", e)
        return Json.decodeFromString<ServerErrorDto>(
            string = """
            {
                "errorCode": -1,
                "errorMessage": "ServerErrorDto parse error",
                "data": null
            }
            """.trimIndent()
        )
    }
}
