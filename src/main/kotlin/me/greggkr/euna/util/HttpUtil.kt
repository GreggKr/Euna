package me.greggkr.euna.util

import me.greggkr.euna.Euna
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject

class HttpUtil {
    companion object {
        private val client = OkHttpClient.Builder()
                .build()

        fun postGist(name: String, content: String): String? {
            val req = Request.Builder()
                    .url("https://api.github.com/gists")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", "token ${Euna.config[Config.github.oauthToken]}")
                    .post(RequestBody.create(null, JSONObject()
                            .put("public", false)
                            .put("files", JSONObject()
                                    .put(name, JSONObject()
                                            .put("content", content)))
                            .toString()))
                    .build()

            val res = client.newCall(req).execute()

            val body = res.body() ?: return null

            val obj = JSONObject(body.string())

            return if (obj.has("html_url")) obj["html_url"] as String else null
        }
    }
}