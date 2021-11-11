package net.kunmc.lab.acid.game;

import net.kunmc.lab.acid.Config;
import net.kunmc.lab.acid.util.Const;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class GameManager {
    public static Random rand = new Random();
    public static GameMode runningMode = GameMode.NEUTRAL;
    public static Map<UUID, Integer> playerWettingCnt = new HashMap();

    public static void controller(GameMode runningMode) {
        // モードを設定
        GameManager.runningMode = runningMode;

        switch (runningMode) {
            case NEUTRAL:
                break;
            case RUNNING:
                break;
        }
    }

    public enum GameMode {
        NEUTRAL,
        RUNNING
    }

    public static boolean canEventProcess() {
        if (GameManager.runningMode == GameManager.GameMode.NEUTRAL)
            return false;
        return true;
    }

    public static List<Player> getTargetPlayerList() {
        return Bukkit.getOnlinePlayers().stream()
                .filter(e -> e.getGameMode() == org.bukkit.GameMode.SURVIVAL)
                .collect(Collectors.toList());
    }

    public static int getWettingCnt(Player p) {
        UUID id = p.getUniqueId();
        if (playerWettingCnt.get(id) == null) {
            setWettingCnt(p, 0);
        }
        return playerWettingCnt.get(id);
    }

    public static void setWettingCnt(Player p, int cnt) {
        playerWettingCnt.put(p.getUniqueId(), cnt);
    }

    public static boolean isInAcid(Entity entity) {
        // 水中
        if (Config.booleanConf.get(Const.ACID_TARGET_BLOCK) && entity.isInWaterOrBubbleColumn()) {
            return true;
        }

        // 雨
        boolean isInAcid = false;
        if (Config.booleanConf.get(Const.ACID_TARGET_RAIN)) {
            isInAcid = true;
            Location loc = entity.getLocation().add(0,1,0);
            for(int i = 0; i < Config.intConf.get(Const.RAIN_POINT); i++) {
                if (loc.getBlock().getType() != Material.AIR) {
                    isInAcid = false;
                    break;
                }
                loc.add(0,1,0);
            }
        }

        return isInAcid;
    }

    public static boolean isPotionTargetEntity(Entity entity) {
        boolean isTargetEntity = false;
        if (entity instanceof Player) {
            isTargetEntity = true;
        }

        if (entity instanceof Mob && Config.booleanConf.get(Const.ACID_TARGET_MOB)) {
            isTargetEntity = true;
        }

        return isTargetEntity;
    }

    public static void fallRain(Player p) {
        int rainPoint = Config.intConf.get(Const.RAIN_POINT);
        double x = p.getLocation().getX();
        double y = p.getLocation().getY() + rainPoint;
        double z = p.getLocation().getZ();
        p.getWorld().spawnParticle(Particle.FALLING_WATER, x, y, z, Config.intConf.get(Const.RAIN_NUM), 10 ,0, 10);
    }
}
