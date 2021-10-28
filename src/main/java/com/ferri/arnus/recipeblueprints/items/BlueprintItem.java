package com.ferri.arnus.recipeblueprints.items;

import java.util.Collections;
import java.util.Optional;
import java.util.Random;

import com.ferri.arnus.recipeblueprints.RecipeBlueprints;

import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;

public class BlueprintItem extends Item{

	public BlueprintItem() {
		super(new Properties().stacksTo(1).tab(CreativeModeTab.TAB_MISC));
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
		boolean randomise = true;
		if (pLevel.isClientSide) {
			return super.use(pLevel, pPlayer, pUsedHand);
		}
		ItemStack stack = pPlayer.getItemInHand(pUsedHand);
		if (!stack.getOrCreateTag().contains("blueprint")) {
			//random recipe
			for (Recipe<?> r : pLevel.getRecipeManager().getRecipes()) {
				if (!((ServerPlayer)pPlayer).getRecipeBook().contains(r)) {
					break;
				}
				return super.use(pLevel, pPlayer, pUsedHand);
			}
			
			Recipe<?> recipe = (Recipe<?>) pLevel.getRecipeManager().getRecipes().toArray()[pLevel.random.nextInt(pLevel.getRecipeManager().getRecipes().size())];
			while (((ServerPlayer)pPlayer).getRecipeBook().contains(recipe)) {
				recipe = (Recipe<?>) pLevel.getRecipeManager().getRecipes().toArray()[pLevel.random.nextInt(pLevel.getRecipeManager().getRecipes().size())];
			}
			((ServerPlayer)pPlayer).awardRecipes(Collections.singleton(recipe));
			stack.shrink(1);
			return super.use(pLevel, pPlayer, pUsedHand);
		}
		CompoundTag tag = stack.getOrCreateTag().getCompound("blueprint");
		randomise = tag.getBoolean("random");
		//single tag
		if (tag.contains("tags")) {
			String location = tag.getString("tags");
			if (RecipeBlueprints.RECIPE_TAGS_MANAGER.contains(location)) {
				// all elements
				if (!randomise) {
					RecipeBlueprints.RECIPE_TAGS_MANAGER.map.get(location).forEach(s -> {
						if (pLevel.getRecipeManager().byKey(new ResourceLocation(s)).isPresent()) {
							((ServerPlayer)pPlayer).awardRecipes(Collections.singleton(pLevel.getRecipeManager().byKey(new ResourceLocation(s)).get()));
						}
					});
					return super.use(pLevel, pPlayer, pUsedHand);
				}
				//random element
				String random = (String) RecipeBlueprints.RECIPE_TAGS_MANAGER.map.get(location).toArray()[new Random().nextInt(RecipeBlueprints.RECIPE_TAGS_MANAGER.map.get(location).size())];
				Optional<? extends Recipe<?>> recipe = pLevel.getRecipeManager().byKey(new ResourceLocation(random));
				if (recipe.isPresent()) {
					((ServerPlayer)pPlayer).awardRecipes(Collections.singleton(recipe.get()));
				}
				stack.shrink(1);
				return super.use(pLevel, pPlayer, pUsedHand);
			}
		}
		//single recipe
		if (tag.contains("recipe")) {
			String location = tag.getString("recipe");
			Optional<? extends Recipe<?>> recipe = pLevel.getRecipeManager().byKey(new ResourceLocation(location));
			if (recipe.isPresent()) {
				((ServerPlayer)pPlayer).awardRecipes(Collections.singleton(recipe.get()));
			}
			stack.shrink(1);
			return super.use(pLevel, pPlayer, pUsedHand);
			
		}
		//random recipe
		for (Recipe<?> r : pLevel.getRecipeManager().getRecipes()) {
			if (!((ServerPlayer)pPlayer).getRecipeBook().contains(r)) {
				break;
			}
			return super.use(pLevel, pPlayer, pUsedHand);
		}
		
		Recipe<?> recipe = (Recipe<?>) pLevel.getRecipeManager().getRecipes().toArray()[pLevel.random.nextInt(pLevel.getRecipeManager().getRecipes().size())];
		while (((ServerPlayer)pPlayer).getRecipeBook().contains(recipe)) {
			recipe = (Recipe<?>) pLevel.getRecipeManager().getRecipes().toArray()[pLevel.random.nextInt(pLevel.getRecipeManager().getRecipes().size())];
		}
		((ServerPlayer)pPlayer).awardRecipes(Collections.singleton(recipe));
		stack.shrink(1);
		return super.use(pLevel, pPlayer, pUsedHand);
	}
	
	@Override
	public Component getName(ItemStack pStack) {
		MutableComponent text = new TextComponent("");
		TranslatableComponent name = new TranslatableComponent(this.getDescriptionId(pStack));
		if (!pStack.getOrCreateTag().contains("blueprint")) {
			text = new TextComponent(" (random)");
			return name.append(text);
		}
		CompoundTag tag = pStack.getOrCreateTag().getCompound("blueprint");
		if (tag.contains("recipe")) {
			String location = tag.getString("recipe");
			if (Minecraft.getInstance().level == null) {return name;}
			Optional<? extends Recipe<?>> recipe = Minecraft.getInstance().level.getRecipeManager().byKey(new ResourceLocation(location));
			if (recipe.isPresent()) {
				text = new TextComponent(" (").append( new TranslatableComponent(recipe.get().getResultItem().getDescriptionId())).append(new TextComponent(")"));
			}
		}
		else if (tag.contains("tags")) {
			String location = tag.getString("tags");
			text = new TextComponent(" (" + location.split(":")[1] + ")");
		}
		else {
			text = new TextComponent(" (random)");
		}
		return name.append(text);
	}
	
	@Override
	public void fillItemCategory(CreativeModeTab pCategory, NonNullList<ItemStack> pItems) {
		if (!allowdedIn(pCategory)) {
			return;
		}
		CompoundTag tag = new CompoundTag();
		tag.putString("tags", "recipeblueprints:file");
		ItemStack stack = new ItemStack(this);
		stack.getOrCreateTag().put("blueprint", tag);
		pItems.add(stack);
		
		CompoundTag tag2 = new CompoundTag();
		tag2.putString("recipe", "minecraft:acacia_wood");
		ItemStack stack2 = new ItemStack(this);
		stack2.getOrCreateTag().put("blueprint", tag2);
		pItems.add(stack2);
	}
}
