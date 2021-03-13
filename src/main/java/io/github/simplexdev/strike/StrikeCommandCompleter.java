package io.github.simplexdev.strike;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class StrikeCommandCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        List<String> strings = new ArrayList<>();

        strings.add("get");
        strings.add("reload");
        strings.add("help");
        strings.add("edit");
        strings.add("set-spawn");
        strings.add("pad-create");
        strings.add("remove-npc");
        strings.add("spawn-npc");

        return strings;
    }
}
