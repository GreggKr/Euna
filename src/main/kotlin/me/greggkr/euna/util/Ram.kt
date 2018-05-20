package me.greggkr.euna.util

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request

private const val BASE_URL = "https://rra.ram.moe"

class Ram {
    enum class ImageType {
        CRY, CUDDE, HUG, KISS, LEWD, LICK, NOM, NYAN, OWO, PAT, POUT, REM, SLAP, SMUG, STARE, TICKLE, TRIGGERED, NSFW_GTN, POTATO, KERMIT;

        override fun toString(): String {
            return name.toLowerCase().replace("_", "-")
        }
    }

    data class Image(
            val path: String,
            val id: String,
            val type: String,
            val nsfw: Boolean
    )

    private val client = OkHttpClient.Builder()
            .build()

    private val gson = GsonBuilder()
            .setPrettyPrinting()
            .create()

    fun getRandomImage(type: ImageType): Image? {
        val url = HttpUrl.Builder()
                .scheme("https")
                .host("rra.ram.moe")
                .addPathSegment("i")
                .addPathSegment("r")
                .setQueryParameter("type", type.toString())
                .setQueryParameter("nsfw", if (type == ImageType.NSFW_GTN) "true" else "false")
                .build()

        val req = Request.Builder()
                .url(url)
                .get()
                .build()

        val res = client.newCall(req).execute()

        val body = res.body() ?: return null

        val str = body.string()

        val obj = gson.fromJson(str, JsonObject::class.java)

        if (obj.has("error")) return null

        return gson.fromJson(obj.toString(), Image::class.java)
    }

    fun getImageFromPath(path: String) = "$BASE_URL$path"
}

