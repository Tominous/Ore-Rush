package us.carlosmendez.spigotmc.orerush.task;

import com.koletar.jj.mineresetlite.Mine;
import org.bukkit.Bukkit;
import us.carlosmendez.spigotmc.orerush.OreRush;

import java.util.List;
import java.util.Random;

public class OreRushTask {
    private OreRush plugin;
    private int taskId;

    public OreRushTask(OreRush plugin) {
        this.plugin = plugin;
    }

    public void start() {
        int delay = plugin.getConfig().getInt("Scheduled-Rush-Settings.Interval", 3600);
        String mineSpecified = plugin.getConfig().getString("Scheduled-Rush-Settings.Mine", "random");

        if (delay <= 0) return;

        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            Mine mine = null;

            if (mineSpecified != null && !mineSpecified.equalsIgnoreCase("random")) {
                Mine[] mines = plugin.getMineResetLite().matchMines(mineSpecified);

                if (mines.length > 0) {
                    mine = mines[0];
                }
            }

            if (mine == null) {
                List<Mine> mines = plugin.getMineResetLite().mines;

                mine = mines.get(new Random().nextInt(mines.size()));
            }

            plugin.getOreRushAPI().startOreRush(mine, plugin.getConfig().getInt("Scheduled-Rush-Settings.Length", 300), plugin.getOreRushAPI().getOreRushComposition());
        }, delay * 20L, delay * 20L);
    }

    public void stop() {
        Bukkit.getScheduler().cancelTask(taskId);
    }
}