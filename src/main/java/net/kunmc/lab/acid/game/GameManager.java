package net.kunmc.lab.acid.game;

import net.kunmc.lab.acid.Config;
import net.kunmc.lab.acid.util.Const;
import org.bukkit.Bukkit;
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

    public static int getWettingCnt(Player p){
        UUID id = p.getUniqueId();
        if (playerWettingCnt.get(id) == null) {
            setWettingCnt(p, 0);
        }
        return playerWettingCnt.get(id);
    }

    public static void setWettingCnt(Player p, int cnt){
        playerWettingCnt.put(p.getUniqueId(), cnt);
    }

    public static boolean isInAcid(Player p) {
        boolean isInAcid = false;
        if (Config.booleanConf.get(Const.ACID_TARGET_BLOCK) && p.isInWaterOrBubbleColumn()) {
            isInAcid = true;
        }
        if (Config.booleanConf.get(Const.ACID_TARGET_RAIN) && p.isInRain()) {
            isInAcid = true;
        }

        return isInAcid;
    }
}
