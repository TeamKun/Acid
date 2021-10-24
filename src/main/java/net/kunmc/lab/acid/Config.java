package net.kunmc.lab.acid;

import net.kunmc.lab.acid.util.Const;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

public class Config {
    public static Map<String, Integer> intConf = new HashMap<>();
    public static Map<String, Boolean> booleanConf = new HashMap<>();
    public static void loadConfig(boolean isReload) {

        Acid plugin = Acid.getPlugin();

        plugin.saveDefaultConfig();

        if (isReload) {
            plugin.reloadConfig();
        }

        FileConfiguration config = plugin.getConfig();

        intConf.put(Const.DAMAGE, config.getInt(Const.DAMAGE));
        intConf.put(Const.SPLASH_DAMAGE, config.getInt(Const.SPLASH_DAMAGE));
        intConf.put(Const.DAMAGE_TICK, config.getInt(Const.DAMAGE_TICK));

        booleanConf.put(Const.ACID_TARGET_BLOCK, false);
        booleanConf.put(Const.ACID_TARGET_POTION, false);
        booleanConf.put(Const.ACID_TARGET_POTION_EFFECT, false);
        booleanConf.put(Const.ACID_TARGET_RAIN, false);
        booleanConf.put(Const.ACID_TARGET_MOB, false);

        for (String key: config.getStringList("acidSwitch")) {
            booleanConf.put(key, true);
        }
    }

}
