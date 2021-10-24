package net.kunmc.lab.acid.event;

import net.kunmc.lab.acid.Config;
import net.kunmc.lab.acid.game.GameManager;
import net.kunmc.lab.acid.util.Const;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;

public class PotionEventHandler implements Listener {
    @EventHandler
    public void onPotionSplash(PotionSplashEvent event) {
        if (!GameManager.canEventProcess() || !Config.booleanConf.get(Const.ACID_TARGET_POTION)){
            return;
        }

        if (event.getHitEntity() instanceof Player){
            Player p = (Player) event.getHitEntity();
            p.damage(Config.intConf.get(Const.SPLASH_DAMAGE));
        }
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        if (!GameManager.canEventProcess() || !Config.booleanConf.get(Const.ACID_TARGET_POTION)){
            return;
        }

        if (event.getItem().getType() == Material.POTION) {
            event.getPlayer().damage(Config.intConf.get(Const.SPLASH_DAMAGE));
        }
    }
}
