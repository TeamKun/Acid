package net.kunmc.lab.acid.task;

import net.kunmc.lab.acid.Config;
import net.kunmc.lab.acid.game.GameManager;
import net.kunmc.lab.acid.util.Const;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Task extends BukkitRunnable {
    private JavaPlugin plugin;

    public Task(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    int rainCnt = 0;

    @Override
    public void run() {
        if (GameManager.runningMode == GameManager.GameMode.NEUTRAL)
            return;

        rainCnt++;

        if (Config.booleanConf.get(Const.ACID_TARGET_RAIN)) {
            for (World world : Bukkit.getWorlds()) {
                world.setStorm(false);
            }
        }

        for (Player p : GameManager.getTargetPlayerList()) {

            // 雨が降っている場合の処理
            if(Config.booleanConf.get(Const.ACID_TARGET_RAIN)) {
                if (rainCnt % 3 == 0) {
                    GameManager.fallRain(p);
                    rainCnt = 0;
                }
                if (GameManager.isInRain(p)) {
                    p.playSound(p.getLocation(), Sound.WEATHER_RAIN,0.4f ,1);
                } else {
                    p.playSound(p.getLocation(), Sound.WEATHER_RAIN,0.1f ,1);
                }
            }

            // 水に当たっている間のダメージカウント
            if (GameManager.isInAcid(p)) {
                GameManager.setWettingCnt(p, GameManager.getWettingCnt(p) + 1);
            } else {
                GameManager.setWettingCnt(p, 0);
            }
            // カウントが一定の値を越えたらダメージ
            if (GameManager.getWettingCnt(p) > Config.intConf.get(Const.DAMAGE_TICK)) {
                p.damage(Config.intConf.get(Const.DAMAGE));
                GameManager.setWettingCnt(p, 0);
            }
        }

        // Mobを処理する場合
        if (Config.booleanConf.get(Const.ACID_TARGET_MOB)) {
            for (World world : Bukkit.getWorlds()) {
                for (Entity entity : world.getEntities()) {
                    if (entity instanceof Mob && GameManager.isInAcid(entity)) {
                        ((Mob) entity).damage(Config.intConf.get(Const.DAMAGE));
                    }
                }
            }
        }
        // Itemを処理する場合
        if (Config.booleanConf.get(Const.ACID_TARGET_ITEM)) {
            for (World world : Bukkit.getWorlds()) {
                for (Entity entity : world.getEntities()) {
                    if (entity instanceof Item && GameManager.isInAcid(entity)) {
                        entity.remove();
                    }
                }
            }
        }
    }
}