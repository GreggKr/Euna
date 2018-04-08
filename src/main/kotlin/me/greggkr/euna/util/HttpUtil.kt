package me.greggkr.euna.util

import me.greggkr.euna.Euna
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.File
import java.util.*

class HttpUtil {
    companion object {
        private val client = OkHttpClient.Builder()
                .build()

        fun uploadText(data: String): String? {
            val file = File("tmp/${UUID.randomUUID()}")

            file.writeText(data, charset("UTF-8"))

            val req = Request.Builder()
                    .url("http://shx.greggkr.me/upload.php?key=${Euna.config[Config.greggkr.key]}")
                    .post(RequestBody.create(null, file))
                    .build()

            val res = client.newCall(req).execute()

            val body = res.body() ?: return null

            println("wooooah: ${body.string()}")

            return body.string()
        }
    }
}