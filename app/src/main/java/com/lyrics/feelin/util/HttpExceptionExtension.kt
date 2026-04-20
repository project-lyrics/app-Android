package com.lyrics.feelin.util

import android.util.Log
import com.lyrics.feelin.core.data.datasource.remote.dto.ServerErrorDto
import java.io.IOException
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import retrofit2.HttpException

fun HttpException.toServerErrorDto(): ServerErrorDto {
    val errorBody = try {
        response()?.errorBody()?.string()
    } catch (e: IOException) {
        Log.e("ServerErrorDto", "Failed to read error body. status=${code()}", e)
        return fallbackServerErrorDto()
    }

    if (errorBody.isNullOrBlank()) {
        Log.e("ServerErrorDto", "Error body is empty. status=${code()}")
        return fallbackServerErrorDto()
    }

    return try {
        Json.decodeFromString<ServerErrorDto>(string = errorBody)
    } catch (e: SerializationException) {
        Log.e("ServerErrorDto", "Failed to parse error body. status=${code()}", e)
        fallbackServerErrorDto()
    }
}

private fun fallbackServerErrorDto(): ServerErrorDto =
    ServerErrorDto(
        errorCode = "-1",
        errorMessage = "앱 사용중 오류가 발생했어요.",
        data = null,
    )
