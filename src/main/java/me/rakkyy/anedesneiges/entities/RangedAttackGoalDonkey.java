package me.rakkyy.anedesneiges.entities;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.RangedAttackMob;

import javax.annotation.Nullable;
import java.util.EnumSet;

// 1 HotFixes: (RangedAttackGoalDonkey.java:92)
public class RangedAttackGoalDonkey extends Goal {
    private final Mob mob;
    private final RangedAttackMob rangedAttackMob; // A.K.A IRangedEntity
    @Nullable
    private LivingEntity target;
    private int attackTime;
    private final double speedModifier;
    private int seeTime;
    private final int attackIntervalMin;
    private final int attackIntervalMax;
    private final float attackRadius;
    private final float attackRadiusSqr;

    public RangedAttackGoalDonkey(RangedAttackMob irangedentity, double d0, int i, float f) {
        this(irangedentity, d0, i, i, f);
    }

    public RangedAttackGoalDonkey(RangedAttackMob irangedentity, double d0, int i, int j, float f) {
        this.attackTime = -1;
        if (!(irangedentity instanceof LivingEntity)) {
            throw new IllegalArgumentException("ArrowAttackGoal requires Mob implements RangedAttackMob");
        } else {
            this.rangedAttackMob = irangedentity;
            this.mob = (Mob) irangedentity;
            this.speedModifier = d0;
            this.attackIntervalMin = i;
            this.attackIntervalMax = j;
            this.attackRadius = f;
            this.attackRadiusSqr = f * f;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }
    }

    @Override
    public boolean canUse() {
        LivingEntity entityliving = this.mob.getTarget();

        if (entityliving != null && entityliving.isAlive()) {
            this.target = entityliving;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse() || !this.mob.getNavigation().isDone();
    }

    @Override
    public void stop() {
        this.target = null;
        this.seeTime = 0;
        this.attackTime = -1;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        double d0 = this.mob.distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ());
        boolean flag = this.mob.getSensing().hasLineOfSight(this.target);

        if (flag) {
            ++this.seeTime;
        } else {
            this.seeTime = 0;
        }
        if (d0 <= (double) this.attackRadiusSqr && this.seeTime >= 5) {
            this.mob.getNavigation().stop();
        } else {
            // HotFix
            // Range Navigation Fixed (Les Non-Monstres ignore les FollowRange quand il s'agit de Navigation)
            double range = this.mob.getAttribute(Attributes.FOLLOW_RANGE).getValue();
            if(d0 <= 1.25 * range*range)
                {this.mob.getNavigation().moveTo((Entity) this.target, this.speedModifier);}
            else
                {this.mob.setTarget(null);}
        }

        this.mob.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
        if (--this.attackTime == 0) {
            if (!flag) {
                return;
            }

            float f = (float) Math.sqrt(d0) / this.attackRadius;
            float f1 = Mth.clamp(f, 0.1F, 1.0F);

            this.rangedAttackMob.performRangedAttack(this.target, f1);
            this.attackTime = Mth.floor(f * (float) (this.attackIntervalMax - this.attackIntervalMin) + (float) this.attackIntervalMin);
        } else if (this.attackTime < 0) {
            this.attackTime = Mth.floor(Mth.lerp(Math.sqrt(d0) / (double) this.attackRadius, (double) this.attackIntervalMin, (double) this.attackIntervalMax));
        }

    }
}
