package me.roundaround.shearablevines.mixin;

import me.roundaround.shearablevines.ShearableVinesMod;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.VineBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ShearsItem;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShearsItem.class)
public abstract class ShearsItemMixin {
  @Inject(method = "useOnBlock", at = @At(value = "HEAD"), cancellable = true)
  public void useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> info) {
    World world = context.getWorld();
    BlockPos blockPos = context.getBlockPos();
    BlockState blockState = world.getBlockState(blockPos);
    Block block = blockState.getBlock();

    if (!(block instanceof VineBlock) || blockState.get(ShearableVinesMod.SHEARED)) {
      return;
    }

    PlayerEntity playerEntity = context.getPlayer();
    ItemStack itemStack = context.getStack();
    if (playerEntity instanceof ServerPlayerEntity) {
      Criteria.ITEM_USED_ON_BLOCK.trigger((ServerPlayerEntity) playerEntity, blockPos, itemStack);
    }

    world.playSound(playerEntity, blockPos, SoundEvents.BLOCK_GROWING_PLANT_CROP, SoundCategory.BLOCKS, 1f, 1f);
    world.setBlockState(blockPos, blockState.with(ShearableVinesMod.SHEARED, true));

    if (playerEntity != null) {
      itemStack.damage(1, playerEntity, LivingEntity.getSlotForHand(context.getHand()));
    }

    info.setReturnValue(ActionResult.success(world.isClient));
  }
}
