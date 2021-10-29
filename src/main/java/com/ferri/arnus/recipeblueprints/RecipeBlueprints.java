package com.ferri.arnus.recipeblueprints;

import com.ferri.arnus.recipeblueprints.items.ItemRegistry;
import com.ferri.arnus.recipeblueprints.loot.LootRegistry;
import com.ferri.arnus.recipeblueprints.loot.LootSerializer;
import com.ferri.arnus.recipeblueprints.tags.RecipeTagsManager;

import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(RecipeBlueprints.MODID)
@EventBusSubscriber
public class RecipeBlueprints {
    public static final String MODID = "recipeblueprints";
    public static final RecipeTagsManager RECIPE_TAGS_MANAGER = new RecipeTagsManager();
    public static final LootSerializer LOOT = new LootSerializer();
    
    public RecipeBlueprints() {
    	
    	ItemRegistry.register();
    	LootRegistry.register();

    }

    @SubscribeEvent
    static void reload(AddReloadListenerEvent event) {
    	event.addListener(RECIPE_TAGS_MANAGER);
    	event.addListener(LOOT);
    }
    
}
