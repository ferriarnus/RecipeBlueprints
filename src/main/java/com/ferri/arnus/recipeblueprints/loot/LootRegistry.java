package com.ferri.arnus.recipeblueprints.loot;

import com.ferri.arnus.recipeblueprints.RecipeBlueprints;

import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class LootRegistry {
	
	public static final DeferredRegister<GlobalLootModifierSerializer<?>> GLM = DeferredRegister.create(ForgeRegistries.LOOT_MODIFIER_SERIALIZERS, RecipeBlueprints.MODID);
	
	public static void register() {
		GLM.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
	
	public static final RegistryObject<BlueprintLoot.Serializer> BLUEPRINTS = GLM.register("blueprints", BlueprintLoot.Serializer::new);

}
