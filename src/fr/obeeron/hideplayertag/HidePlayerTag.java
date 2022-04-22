package fr.obeeron.hideplayertag;

import fr.obeeron.hideplayertag.commands.CommandHideTag;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.logging.Logger;

public class HidePlayerTag extends JavaPlugin implements Listener {

    public static final Logger LOGGER = Bukkit.getLogger();
    public static final String LOG_PREFIX = "[HidePlayerTag] ";

    // Team for RP players. Tag is visible for other teams
    public static Team rpTagTeam;
    // Team for staff. Tag is only visible to other members of this team.
    public static Team staffTagTeam;

    public static String rpTeamName = "uvs.hide_tag";
    public static String staffTeamName = "uvs.staff_tag";

    private void logInfo(String msg){
        LOGGER.info(LOG_PREFIX+msg);
    }

    @Override
    public void onEnable() {
        setupTeams();

        // Setup commands
        setupCommands();

        // Register this class as an event listener
        PluginManager pm = Bukkit.getServer().getPluginManager();
        pm.registerEvents(this, this);

        // Hide all connected players
        for(Player p : Bukkit.getOnlinePlayers())
            joinRpTagTeam(p);

        logInfo("Enabled");
    }

    private void setupTeams(){
        // Get Main scoreboard, can be accessed in game and persist through reloads
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

        rpTagTeam = scoreboard.getTeam(rpTeamName);
        staffTagTeam = scoreboard.getTeam(staffTeamName);

        // If the team doesn't exist yet create it
        if(rpTagTeam == null)
            rpTagTeam = scoreboard.registerNewTeam(rpTeamName);
        if(staffTagTeam == null)
            staffTagTeam = scoreboard.registerNewTeam(staffTeamName);

        // Visibility options
        rpTagTeam.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.FOR_OWN_TEAM);
        staffTagTeam.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.FOR_OTHER_TEAMS);
    }

    private void setupCommands() {
        this.getCommand(CommandHideTag.commandName).setExecutor(new CommandHideTag(this));
        logInfo("Registered command - hidetag");
    }

    public void joinRpTagTeam(Player p) {
        rpTagTeam.addEntry(p.getName());
    }

    public void joinStaffTagTeam(Player p) {
        staffTagTeam.addEntry(p.getName());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        joinRpTagTeam(e.getPlayer());
    }

    @Override
    public void onDisable() {
        logInfo("Disabled");
    }

}
