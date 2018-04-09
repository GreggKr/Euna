package me.greggkr.euna.commands.extra

import com.sun.management.OperatingSystemMXBean
import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import me.greggkr.euna.Euna
import me.greggkr.euna.util.Emoji
import me.greggkr.euna.util.HttpUtil
import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.entities.Message
import org.json.JSONArray
import org.json.JSONObject
import oshi.SystemInfo
import java.lang.management.ManagementFactory
import java.util.*

@CommandDescription(name = "stats", triggers = [
    "stats"
], attributes = [
    CommandAttribute(key = "extra")
], description = "Displays useful statistics about Euna")
class StatsCommand : Command {
    override fun execute(message: Message, a: String) {
        val channel = message.channel
        val jda = message.jda
        val author = message.author

        when {
            !a.isEmpty() && a.contains("-d", true) && Euna.data.isOwner(author.id) -> {
                val system = SystemInfo()

                val os = system.operatingSystem
                val osVer = os.version
                val osBean = ManagementFactory.getOperatingSystemMXBean() as OperatingSystemMXBean

                val hal = system.hardware

                val mem = hal.memory

                val processor = hal.processor

                val json = JSONObject()
                        .put("os", JSONObject()
                                .put("name", os.family)
                                .put("version", JSONObject()
                                        .put("ver", osVer.version)
                                        .put("build", osVer.buildNumber)
                                        .put("code_name", osVer.codeName))
                                .put("manufacturer", os.manufacturer))
                        .put("hardware", JSONObject()
                                .put("memory", JSONObject()
                                        .put("total", mem.total.bytesToHumanReadable())
                                        .put("used", (mem.total - mem.available).bytesToHumanReadable())
                                        .put("swap", JSONObject()
                                                .put("total", mem.swapTotal.bytesToHumanReadable())
                                                .put("used", mem.swapUsed.bytesToHumanReadable())
                                        )
                                )
                                .put("cpu", JSONObject()
                                        .put("name", processor.name)
                                        .put("family", processor.family)
                                        .put("model", processor.model)
                                        .put("architecture", if (processor.isCpu64bit) "x64" else "x86")
                                        .put("vendor", processor.vendor)
                                        .put("stepping", processor.stepping)
                                        .put("logical_processor_count", processor.logicalProcessorCount)
                                        .put("physical_processor_count", processor.physicalProcessorCount)
                                        .put("average_load", processor.systemLoadAverage)
                                )
                        )

                if (a.contains("-p", true)) {
                    val procJson = JSONArray()

                    for (p in os.getProcesses(0, null)) {
                        procJson.put(JSONObject()
                                .put("name", p.name)
                                .put("user", p.user)
                                .put("user_id", p.userID)
                                .put("state", p.state)
                                .put("start_time", p.startTime)
                                .put("up_time", p.upTime)
                                .put("kernel_time", p.kernelTime)
                                .put("user_time", p.userTime)
                                .put("command_line", p.commandLine)
                                .put("group", p.group)
                                .put("path", p.path)
                                .put("resident_set_size", p.residentSetSize)
                                .put("virtual_size", p.virtualSize)
                                .put("bytes_read", p.bytesRead.bytesToHumanReadable())
                                .put("bytes_written", p.bytesWritten.bytesToHumanReadable())
                        )
                    }

                    json.put("processes", procJson)
                }
                author.openPrivateChannel().queue({ it.sendMessage(HttpUtil.postGist("euna-dev-stats-${UUID.randomUUID()}.txt", json.toString(4))).queue() })

                message.addReaction(Emoji.WHITE_CHECK_MARK.unicode).queue()
            }

            !a.isEmpty() && a.contains("more", true) -> {
                
            }

            else -> {
                var users = 0

                jda.guilds.forEach { users += it.members.size }
                channel.sendMessage(EmbedBuilder()
                        .setColor(Euna.data.color)
                        .setThumbnail(jda.selfUser.effectiveAvatarUrl)
                        .addField("Guild Count", "${jda.guilds.size}", true)
                        .addField("Members", "$users", true)
                        .build())
                        .queue()

            }
        }
    }

    private fun Long.bytesToHumanReadable(): String {
        if (this < 1024) return "${this}b"

        val z = (63 - java.lang.Long.numberOfLeadingZeros(this)) / 10

        return "${this / (1 shl (z * 10))}${" kmgtpe"[z]}"
    }
}