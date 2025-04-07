package me.roundaround.shearablevines.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.roundaround.shearablevines.ShearableVinesMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.VineBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VineBlock.class)
public abstract class VineBlockMixin {
  @WrapOperation(
      method = "<init>", at = @At(
      value = "INVOKE", target = "Lnet/minecraft/block/VineBlock;setDefaultState(Lnet/minecraft/block/BlockState;)V"
  )
  )
  private void wrapSetDefaultState(VineBlock instance, BlockState blockState, Operation<Void> original) {
    original.call(instance, blockState.with(ShearableVinesMod.SHEARED, false));
  }

  @Inject(method = "appendProperties", at = @At(value = "TAIL"))
  private void appendProperties(StateManager.Builder<Block, BlockState> builder, CallbackInfo info) {
    builder.add(ShearableVinesMod.SHEARED);
  }

  @Inject(method = "randomTick", at = @At(value = "HEAD"), cancellable = true)
  private void randomTick(
      BlockState blockState,
      ServerWorld world,
      BlockPos blockPos,
      Random random,
      CallbackInfo info
  ) {
    if (blockState.get(ShearableVinesMod.SHEARED)) {
      info.cancel();
    }
  }
}
