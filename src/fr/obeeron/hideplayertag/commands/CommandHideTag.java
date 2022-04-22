package fr.obeeron.hideplayertag.commands;

import fr.obeeron.hideplayertag.HidePlayerTag;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHideTag implements CommandExecutor {

    public static String commandName = "hidetag";
    public static String rpVisibilityFeedback = "Player tags are now hidden.";
    public static String staffVisibilityFeedback = "Player tags are no longer hidden.";

    private HidePlayerTag hptPlugin;

    public CommandHideTag(HidePlayerTag hptPlugin){
        this.hptPlugin = hptPlugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // If the command comes from the console, exit silently
        if(!(sender instanceof Player))
            return true;

        // Check command validity
        if(args.length > 1)
            return false;

        Player player = (Player) sender;
        boolean hasTagsEnabled = HidePlayerTag.staffTagTeam.getEntries().contains(sender.getName());

        // ./hidetag
        if(args.length == 0){
            String tagStatus = ChatColor.BOLD+((hasTagsEnabled)? ChatColor.GREEN+"[Visible]" : ChatColor.RED+"[Hidden]");
            player.sendMessage("Current player tags visibility : "+tagStatus);
            return true;
        }

        // ./hidetag true|false
        else if (args[0].equalsIgnoreCase("true")) {
            if(hasTagsEnabled)
                setTagsVisibility(player, false);
        }
        else if (args[0].equalsIgnoreCase("false")) {
            if (!hasTagsEnabled)
                setTagsVisibility(player, true);
        }
        else
            return false;

        return true;
    }

    private void setTagsVisibility(Player player, boolean state){
        // See all tags
        if(state)
        {
            hptPlugin.joinStaffTagTeam(player);
            player.sendMessage(staffVisibilityFeedback);
        }
        // Hide all tags
        else
        {
            hptPlugin.joinRpTagTeam(player);
            player.sendMessage(rpVisibilityFeedback);
        }
    }

}
