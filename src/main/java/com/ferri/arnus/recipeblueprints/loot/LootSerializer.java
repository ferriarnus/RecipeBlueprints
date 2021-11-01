package com.ferri.arnus.recipeblueprints.loot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.Deserializers;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraftforge.common.ForgeHooks;

public class LootSerializer extends SimpleJsonResourceReloadListener{
	private static final Gson GSON = Deserializers.createLootTableSerializer().create();
	public static HashMap<ResourceLocation, List<LootTable>> REPLACEPOOL= EmptyMap();
	public static HashMap<ResourceLocation, List<LootTable>> REMOVEPOOL = EmptyMap();
	public static HashMap<ResourceLocation, List<LootTable>> ADDPOOL = EmptyMap();
	public static HashMap<ResourceLocation, List<LootTable>> REPLACEENTRY = EmptyMap();
	public static HashMap<ResourceLocation, List<LootTable>> REMOVEENTRY = EmptyMap();
	public static HashMap<ResourceLocation, List<LootTable>> ADDENTRY = EmptyMap();
	
	public LootSerializer() {
		super(GSON, "loot_changes");
		}

	@Override
	protected void apply(Map<ResourceLocation, JsonElement> pObject, ResourceManager pResourceManager,
			ProfilerFiller pProfiler) {
		JsonElement jsonelement = pObject.remove(BuiltInLootTables.EMPTY);
		if (jsonelement != null) {
			
		}
		pObject.forEach((location, element) -> {
			try {
				LootTable loottable = ForgeHooks.loadLootTable(GSON, location, element, true, null);
				ResourceLocation loc = new ResourceLocation(element.getAsJsonObject().get("table").getAsString());
				switch (element.getAsJsonObject().get("action").getAsString()) {
				case "replacepool": {
					addLootPool(REPLACEPOOL, loc, loottable);
					System.out.println(location);
					break;
				}
				case "removepool": {
					addLootPool(REMOVEPOOL, loc, loottable);
					break;
				}
				case "addpool": {
					addLootPool(ADDPOOL, loc, loottable);
					break;
				}
				case "replaceentry": {
					addLootPool(REPLACEENTRY, loc, loottable);
					break;
				}
				case "removentry": {
					addLootPool(REMOVEENTRY, loc, loottable);
					break;
				}
				case "addntry": {
					addLootPool(ADDENTRY, loc, loottable);
					break;
				}
				default:
					break;
					
				}
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		});
	}
	
	public static void addLootPool(HashMap<ResourceLocation, List<LootTable>> map, ResourceLocation loc, LootTable table) {
		if (map.containsKey(loc)) {
			map.get(loc).add(table);
		} else {
			ArrayList<LootTable> list = new ArrayList<>();
			list.add(table);
			map.put(loc, list);
		}
	}
	
	private static HashMap<ResourceLocation, List<LootTable>> EmptyMap() {
		HashMap<ResourceLocation, List<LootTable>> map = new HashMap<ResourceLocation, List<LootTable>>();
		ArrayList<LootTable> list = new ArrayList<>();
		list.add(LootTable.EMPTY);
		map.put(BuiltInLootTables.EMPTY, list);
		return map;
	}
	
	public static void addPools(LootTable table) {
		if (!ADDPOOL.containsKey(table.getLootTableId())) {
			return;
		}
		ADDPOOL.get(table.getLootTableId()).forEach(t -> {
			table.pools.addAll(t.pools);
		});
	}
	
	public static void replacePools(LootTable table) {
		if (!REPLACEPOOL.containsKey(table.getLootTableId())) {
			return;
		}
		REPLACEPOOL.get(table.getLootTableId()).forEach(t -> {
			t.pools.forEach(p -> {
				table.pools.forEach((m) -> {
					if (m.getName().equals(p.getName())) {
						table.pools.set(table.pools.indexOf(m), p);
					}
				});
				
			});
		});
	}
	
	public static void removePools(LootTable table) {
		if (!REMOVEPOOL.containsKey(table.getLootTableId())) {
			return;
		}
		REMOVEPOOL.get(table.getLootTableId()).forEach(t -> {
			t.pools.forEach(p -> {
				Iterator<LootPool> iterator = table.pools.iterator();
				while (iterator.hasNext()) {
					if (iterator.next().getName().equals(p.getName())) {
						iterator.remove();
					}
				}
			});
		});
	}
	
	public static void addEntry(LootTable table) {
		if (!ADDENTRY.containsKey(table.getLootTableId())) {
			return;
		}
		ADDENTRY.get(table.getLootTableId()).forEach(t -> {
			t.pools.forEach(p -> {
				table.pools.forEach(p2 -> {
					if (p2.getName().equals(p.getName())) {
						p2.entries.addAll(p.entries);
					}
				});
			});
		});
	}
	
	public static void replaceEntry(LootTable table) {
		if (!REPLACEENTRY.containsKey(table.getLootTableId())) {
			return;
		}
		REPLACEENTRY.get(table.getLootTableId()).forEach(t -> {
			t.pools.forEach(p -> {
				table.pools.forEach(p2 -> {
					if (p2.getName().equals(p.getName())) {
						
					}
				});
			});
		});
	}

}
