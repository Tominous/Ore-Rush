package us.carlosmendez.spigotmc.orerush.util;

import org.bukkit.ChatColor;

import java.util.concurrent.TimeUnit;

public class Util {

    public static String colorize(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public static String secondsToReadable(int seconds) {
        int weeks = (int) TimeUnit.SECONDS.toDays(seconds) / 7;
        int days = (int) TimeUnit.SECONDS.toDays(seconds) % 7;
        long hours = TimeUnit.SECONDS.toHours(seconds) - (TimeUnit.SECONDS.toDays(seconds) * 24);
        long minutes = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds)* 60);
        long remainingSeconds = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) *60);
        StringBuilder timeBuilder = new StringBuilder();

        if (seconds <= 0) {
            return "None";
        }

        if (weeks > 1) {
            timeBuilder.append(weeks).append(" Weeks ");
        } else if (weeks == 1) {
            timeBuilder.append(weeks).append(" Week ");
        }

        if (days > 1) {
            timeBuilder.append(days).append(" Days ");
        } else if (days == 1) {
            timeBuilder.append(days).append(" Day ");
        }

        if (hours > 1) {
            timeBuilder.append(hours).append(" Hours ");
        } else if (hours == 1) {
            timeBuilder.append(hours).append(" Hour ");
        }

        if (minutes > 1) {
            timeBuilder.append(minutes).append(" Minutes ");
        } else if (minutes == 1) {
            timeBuilder.append(minutes).append(" Minute ");
        }

        if (remainingSeconds > 1) {
            timeBuilder.append(remainingSeconds).append(" Seconds ");
        } else if (remainingSeconds == 1) {
            timeBuilder.append(remainingSeconds).append(" Second ");
        }

        return timeBuilder.toString().trim();
    }
}