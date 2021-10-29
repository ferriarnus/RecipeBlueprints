package com.ferri.arnus.recipeblueprints.loot;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.Deserializers;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraftforge.common.ForgeHooks;

public class LootSerializer extends SimpleJsonResourceReloadListener{
	private static final Gson GSON = Deserializers.createLootTableSerializer().create();
	public static ImmutableMap<ResourceLocation, LootTable> MAP;
	
	public LootSerializer() {
		super(GSON, "loot_tables");
		}

	@Override
	protected void apply(Map<ResourceLocation, JsonElement> pObject, ResourceManager pResourceManager,
			ProfilerFiller pProfiler) {
		Builder<ResourceLocation, LootTable> builder = ImmutableMap.builder();
		JsonElement jsonelement = pObject.remove(BuiltInLootTables.EMPTY);
		if (jsonelement != null) {
			
		}
		pObject.forEach((location, element) -> {
			try (net.minecraft.server.packs.resources.Resource res = pResourceManager.getResource(getPreparedPath(location));){
				LootTable loottable = ForgeHooks.loadLootTable(GSON, location, element, true, null);
				builder.put(location, loottable);
			} catch (Exception exception) {
				exception.printStackTrace();
			}
			
		});
		builder.put(BuiltInLootTables.EMPTY, LootTable.EMPTY);
		ImmutableMap<ResourceLocation, LootTable> immutablemap = builder.build();
		MAP = immutablemap;
	}

}
