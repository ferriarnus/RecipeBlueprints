package com.ferri.arnus.recipeblueprints.loot;

import java.util.List;

import com.google.gson.JsonObject;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

public class BlueprintLoot extends LootModifier{
	boolean locked = false;
	
	protected BlueprintLoot(LootItemCondition[] conditionsIn) {
		super(conditionsIn);
	}
	
	@Override
	protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
		if (locked) {
			return generatedLoot;
		}
		LootTable loottable = context.getLevel().getServer().getLootTables().get(new ResourceLocation("chests/abandoned_mineshaft"));
		locked = true;
		List<ItemStack> randomItems = loottable.getRandomItems(context);
		locked = false;
		loottable.getPool("main");
		generatedLoot.addAll(randomItems);
		return generatedLoot;
	}
	
	static class Serializer extends GlobalLootModifierSerializer<BlueprintLoot> {
		
		@Override
		public BlueprintLoot read(ResourceLocation location, JsonObject object, LootItemCondition[] ailootcondition) {
			return new BlueprintLoot(ailootcondition);
		}

		@Override
		public JsonObject write(BlueprintLoot instance) {
			return makeConditions(instance.conditions);
		}
		
	}

}
