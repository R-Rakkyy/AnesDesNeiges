package me.rakkyy.anedesneiges.entities;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.horse.Donkey;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Snowball;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;

import static net.minecraft.sounds.SoundEvents.SNOW_GOLEM_SHOOT;


public class CustomDonkey extends Donkey implements RangedAttackMob {
    public CustomDonkey (Location loc, StringBuilder name, boolean coffre, int health) {
        super(EntityType.DONKEY, ((CraftWorld) loc.getWorld()).getHandle());

        this.setPos(loc.getX(), loc.getY(), loc.getZ());

        this.setCustomNameVisible(true); // Custom Name Visible
        this.setCustomName(Component.nullToEmpty(name.toString())); // Custom Name

        this.setChest(coffre); // Coffre ou Non

        (this.getBukkitLivingEntity()).setMaxHealth(health);// Custom Health
        (this.getBukkitLivingEntity()).setHealth(health); // Custom Health


    }


    @Override
    protected void registerGoals() {
        //this.goalSelector.addGoal(1, new PanicGoal(this, 1.2));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal(this, Player.class, true));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0, net.minecraft.world.entity.animal.horse.AbstractHorse.class));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.0));
        this.goalSelector.addGoal(6, new RandomStrollGoal(this, 0.7));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(1, new RangedAttackGoal(this, this.getSpeed(), 1, 15));
        this.addBehaviourGoals();
    }

    @Override
    public void performRangedAttack(LivingEntity entityliving, float f) {
        Snowball entitysnowball = new Snowball(this.level, this);
        double d0 = entityliving.getEyeY() - 1.100000023841858D;
        double d1 = entityliving.getX() - this.getX();
        double d2 = d0 - entitysnowball.getY();
        double d3 = entityliving.getZ() - this.getZ();
        double d4 = Math.sqrt(d1 * d1 + d3 * d3) * 0.20000000298023224D;

        entitysnowball.shoot(d1, d2 + d4, d3, 1.6F, 12.0F);
        this.playSound(SoundEvents.SNOW_GOLEM_SHOOT, 1.0F, 0.4F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level.addFreshEntity(entitysnowball);
    }

    @Override
    protected float getStandingEyeHeight(Pose entitypose, EntityDimensions entitysize) {
        return 1.7F;
    }


    /**
     * @return
     */
    @Override
    public boolean alwaysAccepts() {
        return super.alwaysAccepts();
    }
}
