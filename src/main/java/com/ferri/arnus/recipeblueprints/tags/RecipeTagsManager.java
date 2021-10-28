package com.ferri.arnus.recipeblueprints.tags;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

public class RecipeTagsManager extends SimpleJsonResourceReloadListener{
	
	
	private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
	public Map<String, Set<String>> map = new HashMap<>();

	public RecipeTagsManager() {
		super(GSON, "tags/recipes");
	}
	
	public boolean contains(String s) {
		return map.containsKey(s);
	}
	
	public Set<String> getRecipes(String s) {
		return map.get(s);
	}

	@Override
	protected void apply(Map<ResourceLocation, JsonElement> pObject, ResourceManager pResourceManager,
			ProfilerFiller pProfiler) {
		for(Entry<ResourceLocation, JsonElement> entry : pObject.entrySet()) {
			ResourceLocation resourcelocation = entry.getKey();
			System.out.println(resourcelocation.toString());
			JsonObject asJsonObject = entry.getValue().getAsJsonObject();
			JsonArray asJsonArray = asJsonObject.get("values").getAsJsonArray();
			Set<String> set = new HashSet<>();
			for (JsonElement element: asJsonArray) {
				set.add(element.getAsString());
			}
			map.put(resourcelocation.toString(), set);
		}
	}

}
