package com.yc.method

import java.lang.Exception
//HttpURLConnection使用
interface HttpCallbackListener {
    fun onFinish(response: String)
    fun onError(e: Exception)
}