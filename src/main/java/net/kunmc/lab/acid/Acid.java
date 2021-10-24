package net.kunmc.lab.acid;

import net.kunmc.lab.acid.command.CommandController;
import net.kunmc.lab.acid.event.PotionEventHandler;
import net.kunmc.lab.acid.task.Task;
import net.kunmc.lab.acid.util.Const;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public final class Acid extends JavaPlugin {
    private static Acid plugin;
    private BukkitTask task;

    public static Acid getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
        getServer().getPluginManager().registerEvents(new PotionEventHandler(), plugin);
        task = new Task(plugin).runTaskTimer(this, 0, 1);
        Config.loadConfig(false);
        getCommand(Const.MAIN).setExecutor(new CommandController());
    }

    @Override
    public void onDisable() {
        getLogger().info("Acid Plugin is disabled");
    }
}
