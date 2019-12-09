package com.github.sigureruri.customsidebar

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scoreboard.DisplaySlot

/**
 * ScoreboardSidebarを利用し、PlayerにSidebarを表示する。
 * 仮想エンティティの表示名を利用するため、仮想エンティティのScoreを変更するような使用法は想定していない。
 * PlayerのScoreboardをMainScoreboardから変更するため、他のスコアボードを使用するシステムと競合する恐れがある。
 *
 * @author SigureRuri
 *
 * @param plugin plugin
 * @param objectiveName ScoreboardObjectiveの名称。
 *     表示名には影響しない。
 */
abstract class Sidebar(
        val plugin: JavaPlugin,
        val objectiveName: String
) {

    /**
     * SidebarのTitleを取得する。
     * 一種類のみにする場合はsecondは適当でよい。
     *
     * @return Titleの一覧。
     *     first -> 表示するTitle
     *     second -> 次を表示するまでのTick
     */
    abstract fun getTitle(
            player: Player
    ): List<Pair<String, Int>>

    /**
     * SidebarのContentsを取得する。
     * Sidebarには15列までのみ表示が可能です。
     * 同一の文字列は指定できません。
     *
     * @return first -> secondにて指定した仮想エンティティに割り当てるScoreの値
     *     second -> 仮想エンティティの名称
     */
    abstract fun getContents(
            player: Player
    ): List<Pair<String, Int>>

    /**
     * SidebarのContentsを更新する
     *
     * @param player Sidebarを更新するプレイヤー
     */
    fun update(
            player: Player
    ) {
        val scoreboard = player.scoreboard
        val objective = scoreboard.getObjective(DisplaySlot.SIDEBAR) ?: return
        if (objective.name != objectiveName || objective.criteria != CRITERIA_DUMMY) return

        var update = false
        getContents(player).forEach {
            if (!objective.getScore(it.first).isScoreSet) update = true
        }
        if (!update) return

        scoreboard.entries.forEach {
            scoreboard.resetScores(it)
        }
        getContents(player).forEach { (content, score) ->
            objective.getScore(content).score = score
        }
    }

    /**
     * Sidebarを表示する
     *
     * @param player Sidebarを表示するプレイヤー
     * @param updateIntervalTicks Sidebarを更新する間隔。[-1L]にすると更新されない。
     */
    fun show(
            player: Player,
            updateIntervalTicks: Long = 1L
    ) {
        val title = if (getTitle(player).isNotEmpty()) getTitle(player).first().first else TITLE_EMPTY
        val newScoreboard = SCOREBOARD_MANAGER.newScoreboard
        val objective = newScoreboard.registerNewObjective(objectiveName, CRITERIA_DUMMY, title).apply {
            displaySlot = DisplaySlot.SIDEBAR
        }
        getContents(player).forEach { (content, score) ->
            objective.getScore(content).score = score
        }
        player.scoreboard = newScoreboard

        if (updateIntervalTicks >= 0) {
            object : BukkitRunnable() {
                override fun run() {
                    update(player)
                }
            }.runTaskTimer(plugin, updateIntervalTicks, updateIntervalTicks)
        }

        if (getTitle(player).size > 1) {
            val titleList = getTitle(player)
            object : BukkitRunnable() {
                var stage = 0
                var count = 0
                override fun run() {
                    if (titleList[stage].second <= count) {
                        count = 0
                        stage = if (titleList.lastIndex <= stage) 0 else stage + 1
                        objective.displayName = titleList[stage].first
                    } else {
                        count++
                    }
                }
            }.runTaskTimer(plugin, 0L, 1L)
        }
    }

    /**
     * Sidebarを非表示にする
     * 対象のPlayerのScoreboardをMainScoreboardに変更する
     *
     * @param player Sidebarを非表示にするプレイヤー
     */
    fun hide(
            player: Player
    ) {
        player.scoreboard = SCOREBOARD_MANAGER.mainScoreboard
    }

    companion object {
        private val SCOREBOARD_MANAGER = Bukkit.getScoreboardManager()!!
        private val CRITERIA_DUMMY = "dummy"
        private val TITLE_EMPTY = "${ChatColor.GRAY}${ChatColor.BOLD}Title is empty"
    }
}