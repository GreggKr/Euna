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

    fun getPrefix(id: String): String? {
        val doc = getDoc(id, "prefixes")
        return if (doc == null) null else doc["prefix"] as String
    }

    fun setPrefix(id: String, prefix: String) {
        saveField(id, "prefixes", Document().append("prefix", prefix))
    }

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

    fun getVotingStreak(id: String): Int? {
        val doc = getGlobalDoc("votingstreaks")
        return if (doc == null) 0 else doc[id] as Int?
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

    fun getPet(id: String): String? {
        val doc = getGlobalDoc("pets") ?: return null
        return doc.get(id, Document()).toJson()
    }

    fun setPet(id: String, json: String) {
        val doc = getGlobalDoc("pets") ?: Document()
        saveGlobalField("pets", doc.append(id, Document.parse(json)))
    }

    fun getModRole(id: String): Long? {
        val doc = getDoc(id, "staffroles")
        return if (doc == null) null else doc["mod"] as Long
    }

    fun setModRole(id: String, role: Long) {
        val doc = getDoc(id, "staffroles") ?: Document()
        saveField(id, "staffroles", doc.append("mod", role))
    }

    fun getModLog(id: String): Long? {
        val doc = getDoc(id, "logs") ?: return null
        return doc["modlog"] as Long?
    }

    fun setModLog(id: String, channel: Long) {
        val doc = getDoc(id, "logs") ?: Document()
        saveField(id, "logs", doc.append("modlog", channel))
    }

    fun removeModLog(id: String) {
        val doc = getDoc(id, "logs") ?: return
        doc.remove("modlog")
        saveField(id, "logs", doc)
    }

    fun getActionLog(id: String): Long? {
        val doc = getDoc(id, "logs") ?: return null
        return doc["actionlog"] as Long?
    }

    fun setActionLog(id: String, channel: Long) {
        val doc = getDoc(id, "logs") ?: Document()
        saveField(id, "logs", doc.append("actionlog", channel))
    }

    fun removeActionLog(id: String) {
        val doc = getDoc(id, "logs") ?: return
        doc.remove("actionlog")
        saveField(id, "logs", doc)
    }

    @Suppress("UNCHECKED_CAST")
    fun getVoteChannels(id: String): List<Long>? {
        val doc = getDoc(id, "votechannels") ?: Document()
        return doc["channels"] as ArrayList<Long>?
    }

    @Suppress("UNCHECKED_CAST")
    fun addVoteChannel(id: String, channel: Long) {
        val doc = getDoc(id, "votechannels") ?: Document()
        val current = doc["channels"] as ArrayList<Long>? ?: ArrayList()

        current.add(channel)

        saveField(id, "votechannels", doc.append("channels", current))
    }

    @Suppress("UNCHECKED_CAST")
    fun removeVoteChannel(id: String, channel: Long) {
        val doc = getDoc(id, "votechannels") ?: Document()
        val current = doc["channels"] as ArrayList<Long>? ?: ArrayList()

        current.remove(channel)

        saveField(id, "votechannels", doc.append("channels", current))
    }

    @Suppress("UNCHECKED_CAST")
    fun getMutedUsers(id: String): ArrayList<Long>? {
        val doc = getDoc(id, "muted") ?: return ArrayList()
        return doc["users"] as ArrayList<Long>?
    }

    fun addMutedUser(id: String, user: Long) {
        val users = getMutedUsers(id) ?: ArrayList()
        users.add(user)

        saveField(id, "muted", Document().append("users", users))
    }

    fun removeMutedUser(id: String, user: Long) {
        val users = getMutedUsers(id) ?: ArrayList()
        users.remove(user)

        saveField(id, "muted", Document().append("users", users))
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