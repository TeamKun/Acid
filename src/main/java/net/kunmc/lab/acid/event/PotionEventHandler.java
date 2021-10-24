package net.kunmc.lab.acid.event;

import net.kunmc.lab.acid.Config;
import net.kunmc.lab.acid.game.GameManager;
import net.kunmc.lab.acid.util.Const;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public class PotionEventHandler implements Listener {
    @EventHandler
    public void onPotionSplash(ProjectileHitEvent event) {
        if (!GameManager.canEventProcess() || !Config.booleanConf.get(Const.ACID_TARGET_POTION)){
            return;
        }

        Entity targetEntity = event.getHitEntity();
        if (GameManager.isPotionTargetEntity(targetEntity) && event.getEntity() instanceof ThrownPotion){
            ((LivingEntity)targetEntity).damage(Config.intConf.get(Const.SPLASH_DAMAGE));
        }
    }

    @EventHandler
    public void onPotionSplash(EntityPotionEffectEvent event) {
        if (!GameManager.canEventProcess() || !Config.booleanConf.get(Const.ACID_TARGET_POTION_EFFECT)){
            return;
        }

        Entity targetEntity = event.getEntity();
        if (GameManager.isPotionTargetEntity(targetEntity)) {
            ((LivingEntity)targetEntity).damage(Config.intConf.get(Const.SPLASH_DAMAGE));
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
