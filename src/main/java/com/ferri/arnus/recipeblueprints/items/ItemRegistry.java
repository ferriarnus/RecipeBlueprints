package com.ferri.arnus.recipeblueprints.items;

import com.ferri.arnus.recipeblueprints.RecipeBlueprints;

import net.minecraft.world.item.Item;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemRegistry {
	
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, RecipeBlueprints.MODID);
	
	public static void register() {
		ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
	
	public static final RegistryObject<BlueprintItem> BLUEPRINT = ITEMS.register("blueprint", BlueprintItem::new);

}
