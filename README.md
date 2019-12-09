# CustomSidebar
仮想エンティティを使用したScoreboardSidebarをプレイヤーに表示が可能です。  
時間経過によるTitleの変更や、 Sidebarの更新が可能です。

## 環境
- Java 8
- Kotlin 1.3.60
- Spigot 1.14.4

## Sample
Java:
```java
public class SampleSidebarJava extends Sidebar {

    public SampleSidebarJava(JavaPlugin plugin) {
        super(plugin, "samplesidebar");
    }

    @NotNull
    @Override
    public List<Pair<String, Integer>> getTitle(@NotNull Player player) {
        return Arrays.asList(
                new Pair<>("§b§lSample", 5),
                new Pair<>("§3§lSample", 5),
                new Pair<>("§b§lSample", 5),
                new Pair<>("§3§lSample", 5),
                new Pair<>("§b§lS§3§lample", 1),
                new Pair<>("§3§lS§b§la§3§lmple", 1),
                new Pair<>("§3§lSa§b§lm§3§lple", 1),
                new Pair<>("§3§lSam§b§lp§3§lle", 1),
                new Pair<>("§3§lSamp§b§ll§3§le", 1)
        );
    }

    @NotNull
    @Override
    public List<Pair<String, Integer>> getContents(@NotNull Player player) {
        return Arrays.asList(
                new Pair<>(" ", 15),
                new Pair<>("§c§lHealth§f: §c§l" + Math.round(player.getHealth()) + "§7/§c§l" + Math.round(player.getHealthScale()), 14),
                new Pair<>("  ", 13),
                new Pair<>("§6§lOnline Players§f: §e§l" + Bukkit.getOnlinePlayers().size(), 12),
                new Pair<>("   ", 11)
        );
    }
}
```
Kotlin:
```kotlin
class SampleSidebarKotlin(plugin: JavaPlugin) : Sidebar(plugin, "samplesidebar") {
    override fun getTitle(player: Player): List<Pair<String, Int>> {
        return listOf(
                Pair("§b§lSample", 5),
                Pair("§3§lSample", 5),
                Pair("§b§lSample", 5),
                Pair("§3§lSample", 5),
                Pair("§b§lS§3§lample", 1),
                Pair("§3§lS§b§la§3§lmple", 1),
                Pair("§3§lSa§b§lm§3§lple", 1),
                Pair("§3§lSam§b§lp§3§lle", 1),
                Pair("§3§lSamp§b§ll§3§le", 1)
        )
    }

    override fun getContents(player: Player): List<Pair<String, Int>> {
        return listOf(
                Pair(" ", 15),
                Pair("§c§lHealth§f: §c§l" + player.health.roundToInt() + "§7/§c§l" + player.healthScale.roundToInt(), 14),
                Pair("  ", 13),
                Pair("§6§lOnline Players§f: §e§l" + Bukkit.getOnlinePlayers().size, 12),
                Pair("   ", 11)
        )
    }
}
```

