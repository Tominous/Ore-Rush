package us.carlosmendez.spigotmc.orerush.commands;

import com.koletar.jj.mineresetlite.Mine;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import us.carlosmendez.spigotmc.orerush.OreRush;
import us.carlosmendez.spigotmc.orerush.lang.Lang;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CmdOreRush implements CommandExecutor {
    private OreRush plugin = OreRush.getInstance();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            sendUsage(sender);
            return true;
        }

        if (args[0].equalsIgnoreCase("start")) {
            if (!sender.hasPermission("orerush.start")) {
                sender.sendMessage(Lang.ERR_NOPERMISSION.toString());
                return true;
            }
            Mine mine = null;
            int duration = 300;

            if (args.length > 1) {
                Mine[] minesMatchingQuery = plugin.getMineResetLite().matchMines(args[1]);

                if (minesMatchingQuery.length > 0) {
                    mine = minesMatchingQuery[0];
                } else {
                    sender.sendMessage(Lang.ERR_UNKNOWNMINE.toString()
                        .replace("%mine%", args[1]));
                    return true;
                }
            } else {
                List<Mine> mines = new ArrayList<>(plugin.getMineResetLite().mines);

                Collections.shuffle(mines);
                if (mines.size() > 0) {
                    for (int i = 0; i < mines.size(); i++) {
                        Mine randomMine = mines.get(i);

                        if (!plugin.getOreRushAPI().isOreRushActive(randomMine)) {
                            mine = randomMine;
                            break;
                        }
                    }
                } else {
                    sender.sendMessage(Lang.ERR_NOMINE.toString());
                    return true;
                }
            }

            if (args.length > 2) {
                try {
                    duration = Integer.parseInt(args[2]);
                } catch (IllegalArgumentException e) {
                    sender.sendMessage(Lang.ERR_NOTNUMBER.toString()
                            .replace("%arg%", args[2]));
                    return true;
                }
            }

            if (mine == null) {
                sender.sendMessage(Lang.ERR_NOMINE.toString());
                return true;
            }

            if (plugin.getOreRushAPI().isOreRushActive(mine)) {
                sender.sendMessage(Lang.ERR_ORERUSHACTIVE.toString()
                        .replace("%mine%", mine.getName()));
                return true;
            }

            plugin.getOreRushAPI().startOreRush(mine, duration, plugin.getOreRushAPI().getOreRushComposition());
            return true;
        }

        if (args[0].equalsIgnoreCase("end")) {
            if (!sender.hasPermission("orerush.end")) {
                sender.sendMessage(Lang.ERR_NOPERMISSION.toString());
                return true;
            }

            if (args.length < 2) {
                sender.sendMessage(Lang.CMD_ORERUSH_END_USAGE.toString());
                return true;
            }
            Mine mine;

            try {
                mine = plugin.getMineResetLite().matchMines(args[1])[0];
            } catch (IndexOutOfBoundsException e) {
                sender.sendMessage(Lang.ERR_UNKNOWNMINE.toString()
                        .replace("%mine%", args[1]));
                return true;
            }

            if (!plugin.getOreRushAPI().isOreRushActive(mine)) {
                sender.sendMessage(Lang.ERR_NOORERUSHACTIVE.toString()
                        .replace("%mine%", mine.getName()));
                return true;
            }

            plugin.getOreRushAPI().endOreRush(mine);
            return true;
        }

        if (args[0].equalsIgnoreCase("endall")) {
            if (!sender.hasPermission("orerush.endall")) {
                sender.sendMessage(Lang.ERR_NOPERMISSION.toString());
                return true;
            }

            sender.sendMessage(Lang.CMD_ORERUSH_ENDALL_SUCCESS.toString());
            plugin.getOreRushAPI().endAllOreRushes();
            return true;
        }

        sendUsage(sender);
        return true;
    }

    private void sendUsage(CommandSender sender) {
        for (String line : Lang.getLongMessage("cmd.orerush.usage")) {
            sender.sendMessage(line);
        }
    }
}