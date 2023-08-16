package me.hungaz.vianarchyutils;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ViAnarchyUtils extends JavaPlugin implements Listener {

    private boolean killEnabled;
    private boolean discordLinkEnabled;
    private Map<UUID, Boolean> confirmations;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        loadConfig();
        confirmations = new HashMap<>();
    }

    private void loadConfig() {
        saveDefaultConfig();
        reloadConfig();
        FileConfiguration config = getConfig();
        killEnabled = config.getBoolean("kill-enabled", true);
        discordLinkEnabled = config.getBoolean("discord-link-enabled", true);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if ((command.getName().equalsIgnoreCase("kill") || command.getName().equalsIgnoreCase("suicide")) && killEnabled) {
            if (confirmations.containsKey(player.getUniqueId())) {
                confirmations.remove(player.getUniqueId());
                player.setHealth(0.0);
                player.sendMessage("§4Bạn đã tự kết liễu bản thân.");
            } else {
                player.sendMessage("§cBạn có chắc chắn rằng bạn muốn tự kết liễu bản thân? Nhập lại để xác nhận.");
                confirmations.put(player.getUniqueId(), true);
            }
            return true;
        } else if (command.getName().equalsIgnoreCase("joindiscord") && discordLinkEnabled) {
            TextComponent discordLink = new TextComponent("§aLink tham gia server Discord là");
            TextComponent linkText = new TextComponent(" §ehttps://www.vianarchy.net/discord");
            linkText.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.vianarchy.net/discord"));
            discordLink.addExtra(linkText);
            player.spigot().sendMessage(discordLink);
            return true;
        }

        return false;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (player.getLastDamageCause() != null) {
            player.getLastDamageCause().getCause();
        }
    }
}
