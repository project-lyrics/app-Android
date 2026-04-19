package com.lyrics.feelin.core.data.datasource.remote.dto.exception

import com.lyrics.feelin.core.data.datasource.remote.dto.ServerErrorDto

class FeelinServerException(val description: ServerErrorDto) : Exception(description.errorCode)
