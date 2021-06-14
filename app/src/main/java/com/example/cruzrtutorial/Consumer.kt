package com.example.cruzrtutorial

import android.webkit.MimeTypeMap
import io.github.rybalkinsd.kohttp.dsl.httpGet
import io.github.rybalkinsd.kohttp.dsl.httpPost
import io.github.rybalkinsd.kohttp.dsl.upload
import io.github.rybalkinsd.kohttp.ext.url
import okhttp3.*
import java.io.File
import java.net.URI


//http://192.168.1.51:3000/


//Method to post text as a request and return the response body
fun post(text: String, route: String = "/"): String? {
    val response: Response = httpPost {
        url("http://192.168.1.52:3000$route")

        param {  }
        header {  }

        body {
            json {
                "text" to text
            }
        }
    }

    return response.body()?.string()
}

//Method to get a response body
fun get(route : String): String? {
    val response: Response = httpGet {
        host = "192.168.1.52"
        port = 3000
        path = route

        param {  }
        header {  }
    }

    return response.body()?.string()
}



