package me.rakkyy.anedesneiges.entities;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.horse.Donkey;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Snowball;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.jetbrains.annotations.NotNull;


public class CustomDonkey extends Donkey implements RangedAttackMob {
    final float maxShootRange = 6;

    public CustomDonkey (Location loc, String name, boolean coffre, int health) {
        super(EntityType.DONKEY, ((CraftWorld) loc.getWorld()).getHandle());

        this.setPos(loc.getX(), loc.getY(), loc.getZ());

        this.setCustomNameVisible(true); // Name Visible

        this.setCustomName(Component.nullToEmpty(name)); // Custom Name

        this.setChest(coffre); // Coffre ou Non

        (this.getBukkitLivingEntity()).setMaxHealth(health);// Custom Health
        (this.getBukkitLivingEntity()).setHealth(health); // Custom Health

        this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(10); // Range

    }

    // Register Goal
    //
    // Float
    // Target: Les Joueurs Proches
    //         + Les Entités qui le frappent
    //
    // Goal: RangedAttackGoalDonkey ( RangedAttackGoal légérement modif pour pas que la range Bug)
    //       RandomStrollGoal ( Balade Aléatoire, pas nécéssaire)
    //       RandomLookAroundGoal ( Regards Aléatoire, pas nécéssaire)
    @Override
    protected void registerGoals() {
        //this.goalSelector.addGoal(1, new PanicGoal(this, 1.2));

        this.goalSelector.addGoal(0, new FloatGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, Player.class));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal(this, Player.class, true, true));
        this.goalSelector.addGoal(1, new RangedAttackGoalDonkey(this, 1.05D, 13, 18, maxShootRange));

        //this.goalSelector.addGoal(2, new BreedGoal(this, 1.0, net.minecraft.world.entity.animal.horse.AbstractHorse.class));
        //this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.0));

        this.goalSelector.addGoal(4, new RandomStrollGoal(this, 0.7, 1));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));

        //this.addBehaviourGoals();

    }


    // Override de RangedAttackMob->performRangedAttack
    // Les Modifications proviennent de SnowGolem
    // Lance une boule de neige sur la cible depuis l'Ane
    // ça évite d'utiliser une boucle, on passe par un pathfinder minecraft
    @Override
    public void performRangedAttack(LivingEntity entityliving, float f) {
        Snowball entitysnowball = new Snowball(this.level, this);
        double d0 = entityliving.getEyeY() - 1.500000023841858D;
        double d1 = entityliving.getX() - this.getX();
        double d2 = d0 - entitysnowball.getY();
        double d3 = entityliving.getZ() - this.getZ();
        double d4 = Math.sqrt(d1 * d1 + d3 * d3) * 0.20000000298023224D;
        entitysnowball.shoot(d1, d2 + d4, d3, 1.6F, 12.0F);
        this.playSound(SoundEvents.SNOW_GOLEM_SHOOT, 1.0F, 0.4F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level.addFreshEntity(entitysnowball);
    }

    // Override de RangedAttackMob->getStandingEyeHeight
    // Sert pour le point de spawn de la boule de neige
    // Défini arbitrairement

    @Override
    protected float getStandingEyeHeight(@NotNull Pose entitypose, @NotNull EntityDimensions entitysize) {
        return 1.05F;
    }

}
