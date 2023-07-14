package me.jishuna.betterpistons.nms.v1_20_R1;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.bukkit.craftbukkit.v1_20_R1.util.CraftMagicNumbers;

import me.jishuna.betterpistons.Settings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.state.BlockState;

public class InternalPistonHandler {
    private static final WeakHashMap<Level, Map<BlockPos, CompoundTag>> movements = new WeakHashMap<>();

    public static boolean canPush(BlockState state, Level world, BlockPos pos, Direction direction, boolean canBreak, Direction pistonDir) {
        if (Settings.BLACKLISTED_MATERIALS.contains(CraftMagicNumbers.getMaterial(state.getBlock()))) {
            return false;
        }

        if (pos.getY() < world.getMinBuildHeight() || pos.getY() > world.getMaxBuildHeight() - 1 || !world.getWorldBorder().isWithinBounds(pos)) {
            return false;
        }
        if (state.isAir()) {
            return true;
        }
        if (direction == Direction.DOWN && pos.getY() == world.getMinBuildHeight()) {
            return false;
        }
        if (direction == Direction.UP && pos.getY() == world.getMaxBuildHeight() - 1) {
            return false;
        }

        if (state.is(Blocks.PISTON) || state.is(Blocks.STICKY_PISTON)) {
            return !state.getValue(PistonBaseBlock.EXTENDED);
        }

        if (state.is(Blocks.PISTON_HEAD) || state.is(Blocks.MOVING_PISTON)) {
            return false;
        }

        switch (state.getPistonPushReaction()) {
        case BLOCK:
            return true;
        case DESTROY:
            return canBreak;
        case PUSH_ONLY:
            return direction == pistonDir;
        case IGNORE, NORMAL:
        default:
            return true;
        }
    }

    public static int getPistonPushLimit() {
        return Settings.PUSH_LIMIT;
    }

    public static boolean finishMovingBlock(Level world, BlockPos pos, BlockState state, int flags) {
        if (!world.setBlock(pos, state, flags)) {
            return false;
        }

        if (state.hasBlockEntity()) {
            BlockEntity entity = world.getBlockEntity(pos);
            if (entity == null) {
                return true;
            }

            Map<BlockPos, CompoundTag> worldMovements = movements.get(world);
            if (worldMovements == null) {
                return true;
            }

            CompoundTag tag = worldMovements.remove(pos);
            if (tag == null) {
                return true;
            }

            entity.load(tag);
            entity.setChanged();
        }

        return true;
    }

    public static void startMoving(Level world, BlockPos origin, Direction dir, boolean extend, List<BlockPos> positions) {
        if (!extend) {
            dir = dir.getOpposite();
        }

        for (BlockPos pos : positions) {
            BlockState state = world.getBlockState(pos);
            if (state.hasBlockEntity()) {
                BlockEntity entity = world.getBlockEntity(pos);
                movements.computeIfAbsent(world, l -> new HashMap<>()).put(pos.relative(dir), entity.saveWithFullMetadata());
                world.removeBlockEntity(pos);
            }
        }
    }
}
