package us.carlosmendez.spigotmc.orerush.api;

import com.koletar.jj.mineresetlite.Mine;
import com.koletar.jj.mineresetlite.SerializableBlock;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import us.carlosmendez.spigotmc.orerush.OreRush;
import us.carlosmendez.spigotmc.orerush.lang.Lang;
import us.carlosmendez.spigotmc.orerush.util.UMaterial;
import us.carlosmendez.spigotmc.orerush.util.Util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class OreRushAPI {
    private OreRush plugin = OreRush.getInstance();
    private HashMap<Mine, Long> activeOreRushes = new HashMap<>();
    private HashMap<Mine, Map<SerializableBlock, Double>> mineCompositions = new HashMap<>();
    private Map<SerializableBlock, Double> oreRushComposition = new HashMap<>();

    /**
     * Retrieve a hashmap of active ore rushes and the time at which they're scheduled to end
     *
     * @return HashMap containing mines and times they end
     */
    public HashMap<Mine, Long> getActiveOreRushes() {
        return activeOreRushes;
    }

    /**
     * End all active ore rushes silently
     */
    public void endAllOreRushes() {
        Set<Mine> mines = new HashSet<>(activeOreRushes.keySet());

        for (Mine mine : mines) {
            endOreRush(mine, true);
        }
    }

    /**
     * End an ore rush for a specific mine
     *
     * @param mine The mine to end the ore rush for
     */
    public void endOreRush(Mine mine) {
        if (!activeOreRushes.containsKey(mine) || !mineCompositions.containsKey(mine)) return;
        Map<SerializableBlock, Double> mineComposition = mineCompositions.get(mine);

        activeOreRushes.remove(mine);
        mine.getComposition().clear();
        mine.getComposition().putAll(mineComposition);
        mine.reset();
        Bukkit.broadcastMessage(Lang.ORERUSH_ENDED.toString()
                .replace("%mine%", mine.getName()));
    }

    /**
     * End an ore rush for a specific mine
     *
     * @param mine The mine to end the ore rush for
     * @param silent Whether or not to keep the broadcast message silent
     */
    public void endOreRush(Mine mine, boolean silent) {
        if (!activeOreRushes.containsKey(mine) || !mineCompositions.containsKey(mine)) return;
        Map<SerializableBlock, Double> mineComposition = mineCompositions.get(mine);

        activeOreRushes.remove(mine);
        mine.getComposition().clear();
        mine.getComposition().putAll(mineComposition);
        mine.reset();

        if (!silent) {
            Bukkit.broadcastMessage(Lang.ORERUSH_ENDED.toString()
                    .replace("%mine%", mine.getName()));
        }
    }

    /**
     * Retrieve the loaded composition from the config.yml
     *
     * @return A map of SerializableBlock and Doubles
     */
    public Map<SerializableBlock, Double> getOreRushComposition() {
        return oreRushComposition;
    }

    /**
     * Check whether or not an ore rush is active for a specific mine
     *
     * @param mine The mine to check
     * @return True if there is an ore rush active, false if there is not
     */
    public boolean isOreRushActive(Mine mine) {
        return activeOreRushes.containsKey(mine) && mineCompositions.containsKey(mine);
    }

    /**
     * Loads composition from the config
     */
    public void load() {
        ConfigurationSection configComposition = plugin.getConfig().getConfigurationSection("Composition");
        if (configComposition == null) return;

        for (String compositionKey : configComposition.getKeys(false)) {
            UMaterial material = UMaterial.match(compositionKey);

            if (material == null || material.getMaterial() == null || !material.getMaterial().isBlock()) continue;

            oreRushComposition.put(new SerializableBlock(material.getMaterial().getId()), plugin.getConfig().getDouble("OreRush-Composition." + compositionKey, 1.0));
        }
    }

    /**
     * Start an ore rush for a mine
     *
     * @param mine The mine to start an ore rush for
     * @param duration The length in seconds the ore rush should last
     * @param composition A map of SerializableBlocks and Doubles to use as the mine composition
     */
    public void startOreRush(Mine mine, int duration, Map<SerializableBlock, Double> composition) {
        if (activeOreRushes.containsKey(mine) || duration <= 0) return;
        Map<SerializableBlock, Double> mineComposition = mine.getComposition();
        Map<SerializableBlock, Double> savedComposition = new HashMap<>(mineComposition);

        activeOreRushes.put(mine, System.currentTimeMillis() + (duration * 1000));
        mineCompositions.put(mine, savedComposition);
        mineComposition.clear();
        mineComposition.putAll(composition);
        mine.reset();
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> endOreRush(mine), duration * 20L);
        Bukkit.broadcastMessage(Lang.ORERUSH_STARTED.toString()
                .replace("%mine%", mine.getName())
                .replace("%time%", Util.secondsToReadable(duration)));
    }

    /**
     * Start an ore rush for a mine
     *
     * @param mine The mine to start an ore rush for
     * @param duration The length in seconds the ore rush should last
     * @param composition A map of SerializableBlocks and Doubles to use as the mine composition
     * @param silent Whether or not to keep the broadcast message silent
     */
    public void startOreRush(Mine mine, int duration, Map<SerializableBlock, Double> composition, boolean silent) {
        if (activeOreRushes.containsKey(mine) || duration <= 0) return;
        Map<SerializableBlock, Double> mineComposition = mine.getComposition();
        Map<SerializableBlock, Double> savedComposition = new HashMap<>(mineComposition);

        activeOreRushes.put(mine, System.currentTimeMillis() + (duration * 1000));
        mineCompositions.put(mine, savedComposition);
        mineComposition.clear();
        mineComposition.putAll(composition);
        mine.reset();
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> endOreRush(mine), duration * 20L);

        if (!silent) {
            Bukkit.broadcastMessage(Lang.ORERUSH_STARTED.toString()
                    .replace("%mine%", mine.getName())
                    .replace("%time%", Util.secondsToReadable(duration)));
        }
    }
}