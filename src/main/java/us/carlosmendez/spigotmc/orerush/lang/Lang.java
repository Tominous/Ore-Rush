package us.carlosmendez.spigotmc.orerush.lang;

import org.bukkit.configuration.file.FileConfiguration;
import us.carlosmendez.spigotmc.orerush.util.Util;

import java.util.ArrayList;
import java.util.List;

public enum Lang {
    CMD_ORERUSH_END_USAGE("Proper usage is &b/orerush end <mine>"),
    CMD_ORERUSH_ENDALL_SUCCESS("You have ended all ore rushes!"),

    ERR_NOMINE("Unable to find a mine to start an ore rush for"),
    ERR_NOORERUSHACTIVE("There is not an ore rush active for the &b%mine% &7mine"),
    ERR_NOPERMISSION("You don't have permission to do this!"),
    ERR_NOTNUMBER("&b%arg% &7is not a number!"),
    ERR_ORERUSHACTIVE("There is already an ore rush active for the &b%mine% &7mine"),
    ERR_UNKNOWNMINE("No mine called '&b%mine%&7' was found"),

    ORERUSH_STARTED("An ore rush has started in &b%mine% Mine &7and will last for &b%time%"),
    ORERUSH_ENDED("An ore rush has ended in &b%mine% Mine"),

    PREFIX("&3&lOre Rush &8\u00BB &7");

    private String path;
    private String def;
    private static FileConfiguration fileConfiguration;

    Lang(String def) {
        this.path = name().toLowerCase().replace("-", ".");
        this.def = def;
    }

    public static List<String> getLongMessage(String path) {
        List<String> longMessage = new ArrayList<>();

        for (String line : fileConfiguration.getStringList(path)) {
            longMessage.add(Util.colorize(line));
        }

        return longMessage;
    }

    public static void setFileConfiguration(FileConfiguration fileConfiguration) {
        Lang.fileConfiguration = fileConfiguration;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        if (this != PREFIX) {
            stringBuilder.append(PREFIX.toString());
        }

        stringBuilder.append(fileConfiguration.getString(path, def));
        return Util.colorize(stringBuilder.toString());
    }
}