package net.kunmc.lab.acid.task;

import net.kunmc.lab.acid.Config;
import net.kunmc.lab.acid.util.Const;
import net.kunmc.lab.acid.game.GameManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Task extends BukkitRunnable {
    private JavaPlugin plugin;

    public Task(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        if (GameManager.runningMode == GameManager.GameMode.NEUTRAL)
            return;

        for (Player p: GameManager.getTargetPlayerList()) {
            // 水に当たっている間はカウント
            if (GameManager.isInAcid(p)) {
                GameManager.setWettingCnt(p, GameManager.getWettingCnt(p) + 1);
            } else {
                GameManager.setWettingCnt(p, 0);
            }

            // カウントが一定の値を越えたらダメージ
            if (GameManager.getWettingCnt(p) > Config.intConf.get(Const.DAMAGE_TERM)) {
                p.damage(Config.intConf.get(Const.DAMAGE));
                GameManager.setWettingCnt(p, 0);
            }
        }
    }
}