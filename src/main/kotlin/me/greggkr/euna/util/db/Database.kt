package me.greggkr.euna.util.db

import com.mongodb.MongoClient
import com.mongodb.MongoClientOptions
import com.mongodb.MongoCredential
import com.mongodb.ServerAddress
import com.mongodb.client.model.Filters
import com.mongodb.client.model.UpdateOptions
import org.bson.Document

class Database(user: String,
               password: String,
               authDBName: String,
               dbName: String,
               host: String,
               port: Int) {

    private val creds = MongoCredential.createCredential(user, authDBName, password.toCharArray())
    private val client = MongoClient(ServerAddress(host, port), creds, MongoClientOptions.builder().build())
    private val database = client.getDatabase(dbName)
    private val updateOptions = UpdateOptions().upsert(true)

    fun getMoney(id: String): Double {
        val doc = getDoc(id, "money")
        return if (doc == null) -1.0 else doc.getDouble("money")
    }

    fun setMoney(id: String, amount: Double) {
        saveField(id, "money", Document().append("money", amount))
    }

    /**
     * @return Map of Double, Double, first being the multiplier, second being the percent
     */
    @Suppress("UNCHECKED_CAST")
    fun getCasinoChances(id: String): Map<Double, Double>? {
        val doc = getDoc(id, "casino")
        return if (doc == null) null else doc["chances"] as Map<Double, Double>
    }

    @Suppress("UNCHECKED_CAST")
    fun getFishChances(id: String): Map<Pair<Double, String>, Double>? {
        val doc = getDoc(id, "fish")
        return if (doc == null) null else doc["chances"] as Map<Pair<Double, String>, Double>
    }

    fun getVotingStreak(id: String): Int {
        val doc = getGlobalDoc("votingstreaks")
        return if (doc == null) 0 else doc[id] as Int
    }

    fun setVotingStreak(id: String, amount: Int) {
        val doc = getGlobalDoc("votingstreaks") ?: Document()
        saveGlobalField("votingstreaks", doc.append(id, amount))
    }

    @Suppress("UNCHECKED_CAST")
    fun getFish(id: String): MutableMap<String, Int>? {
        val doc = getGlobalDoc("fish")
        return if (doc == null) null else doc[id] as MutableMap<String, Int>?
    }

    fun setFish(id: String, fish: Map<String, Int>) {
        val doc = getGlobalDoc("fish") ?: Document()
        saveGlobalField("fish", doc.append(id, fish))
    }

    private fun getDoc(id: String, collection: String): Document? {
        return database.getCollection(collection).find(Filters.eq("_id", id)).firstOrNull()
    }

    private fun saveField(id: String, collection: String, data: Document) {
        database.getCollection(collection).replaceOne(Filters.eq("_id", id), data.append("_id", id), updateOptions)
    }

    private fun removeField(id: String, collection: String) {
        database.getCollection(collection).deleteOne(Filters.eq("_id", id))
    }

    private fun getGlobalDoc(collection: String): Document? {
        return getDoc("global", collection)
    }

    private fun saveGlobalField(collection: String, data: Document) {
        saveField("global", collection, data)
    }

    private fun removeGlobalField(collection: String) {
        removeField("global", collection)
    }
}