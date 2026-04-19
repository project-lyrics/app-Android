package com.lyrics.feelin.core.data.interceptor

import com.lyrics.feelin.core.data.datasource.local.DeviceIdDataStore
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

const val DEVICE_ID_MARKER_HEADER = "X-Requires-Device-Id"

@Singleton
class DeviceIdInterceptor @Inject constructor(
    private val deviceIdDataStore: DeviceIdDataStore
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        if (originalRequest.header(DEVICE_ID_MARKER_HEADER) == null) {
            return chain.proceed(originalRequest)
        }

        val deviceId = runBlocking {
            deviceIdDataStore.getOrCreate()
        }

        val requestWithDeviceId = originalRequest.newBuilder()
            .removeHeader(DEVICE_ID_MARKER_HEADER)
            .header("Device-Id", deviceId)
            .build()

        return chain.proceed(requestWithDeviceId)
    }
}
