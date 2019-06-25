package us.carlosmendez.spigotmc.orerush;

import com.koletar.jj.mineresetlite.MineResetLite;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import us.carlosmendez.spigotmc.orerush.api.OreRushAPI;
import us.carlosmendez.spigotmc.orerush.commands.CmdOreRush;
import us.carlosmendez.spigotmc.orerush.file.CustomFile;
import us.carlosmendez.spigotmc.orerush.file.FileManager;
import us.carlosmendez.spigotmc.orerush.lang.Lang;
import us.carlosmendez.spigotmc.orerush.task.OreRushTask;

import java.util.logging.Level;

public class OreRush extends JavaPlugin {
    private static OreRush instance;
    private FileManager fileManager = new FileManager(this);
    private OreRushAPI oreRushAPI;
    private OreRushTask oreRushTask;
    private MineResetLite mineResetLite;

    @Override
    public void onEnable() {
        instance = this;
        oreRushAPI = new OreRushAPI();
        oreRushTask = new OreRushTask(this);

        if (!setupMineResetLite()) {
            Bukkit.getLogger().log(Level.SEVERE, "Unable to hook into MineResetLite! Disabling the plugin now...");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        registerConfig();
        registerCommands();
        registerListeners();

        oreRushTask.start();
    }

    @Override
    public void onDisable() {
        oreRushTask.stop();
        oreRushAPI.endAllOreRushes();
        oreRushAPI = null;
        instance = null;
    }

    public static OreRush getInstance() {
        return instance;
    }

    public MineResetLite getMineResetLite() {
        return mineResetLite;
    }

    public OreRushAPI getOreRushAPI() {
        return oreRushAPI;
    }

    private void registerCommands() {
        PluginCommand oreRushCommand = getCommand("orerush");

        if (oreRushCommand != null) {
            oreRushCommand.setExecutor(new CmdOreRush());
        }
    }

    private void registerConfig() {
        CustomFile langFile = fileManager.registerFile("lang");

        saveDefaultConfig();
        langFile.saveDefaultConfig();

        oreRushAPI.load();
        Lang.setFileConfiguration(langFile.getConfig());
    }

    private void registerListeners() {

    }

    private boolean setupMineResetLite() {
        Plugin mineResetLitePlugin = Bukkit.getPluginManager().getPlugin("MineResetLite");

        if (!(mineResetLitePlugin instanceof MineResetLite)) {
            return false;
        }

        mineResetLite = (MineResetLite) mineResetLitePlugin;
        return true;
    }
}