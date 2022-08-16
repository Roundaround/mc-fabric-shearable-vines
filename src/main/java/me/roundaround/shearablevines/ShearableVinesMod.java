package me.roundaround.shearablevines;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ModInitializer;
import net.minecraft.state.property.BooleanProperty;

public final class ShearableVinesMod implements ModInitializer {
  public static final String MOD_ID = "shearablevines";
  public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

  public static final BooleanProperty SHEARED = BooleanProperty.of("sheared");

  @Override
  public void onInitialize() {
  }
}
