package me.caleb.BlockBR.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.caleb.BlockBR.Main;
import me.caleb.BlockBR.utils.Chat;

public class Rewards {
		
		private Main plugin;
		private FileConfiguration config = null;
		
		public Rewards(Main plugin) {
			this.plugin = plugin;
			config = plugin.getConfig();
		}
		
		//Checks to see if the path in the config even exists before it does anything else
		public void isTier(String tierName,Player p) {
			
			if(config.getString("Tiers." + tierName.toLowerCase()) == null) {
				Chat.sendPlayerMessage(p, "&bThis is not a tier! Do &6&l/bbr tierlist &bto see all the different tiers!");
				return;
			}
			
		}
		
		//For CrateName
		//bbr rewardadd (tier) (rewardType) (Crate)
		public void rewardAddCrate(Player p,String tier, String rewardType, String crateName) {	
			
			final String PATH = "Tiers." + tier + ".Properties.Rewards.Crate";

			isTier(tier,p);
			
			/*
			 * CrateReloaded is not required for this plugin to fully work,
			 * but it does limit your reward types.
			 * (Can't use the crate types)
			 */
			if(checkCrateReloaded() == false) {
				Chat.sendPlayerMessage(p, "&bYou must have &5&lCrateReloaded &binstalled to use this reward type!");
				return;
			}else {
				Chat.sendPlayerMessage(p, "&bThe crate for tier &5&l" + tier.toUpperCase() + "&b has been updated to &5&l" + crateName.toUpperCase());
				config.set("Tiers." + tier + ".Properties.Rewards.Crate", crateName);
				plugin.saveConfig();
			}
			
		}
		//For Money
		//bbr rewardadd (tier) (rewardType) (Amount of money)
		public void rewardAddMoney(Player p,String tier, String rewardType, int amount) {	
	
			final String PATH = "Tiers." + tier + ".Properties.Rewards.Money";
			isTier(tier,p);
			
			//Tests to see if the value is an actual number and if the number is negative
			try {
				if(amount < 0) {
					Chat.sendPlayerMessage(p, "It must be a positive number!");
					return;
				}
			}catch(NumberFormatException e) {
				Chat.sendPlayerMessage(p, "It must be a number!");
				return;
			}
			
			Chat.sendPlayerMessage(p, "&bThe money for tier &5&l" + tier.toUpperCase() + "&b has been updated to &5&l" + amount);
			config.set("Tiers." + tier + ".Properties.Rewards.Money", amount);
			
			plugin.saveConfig();
			
		}
		//For Items
		//It will add the item in your hand
		//bbr rewardadd (tier) (rewardType)
		public void rewardAddItem(Player p,String tier) {	
			
			final String PATH = "Tiers." + tier + ".Properties.Rewards.Item";
			String itemLine = "";
			ItemStack item = p.getItemInHand();
			isTier(tier,p);
			
			int amount = item.getAmount();
			String itemName = item.getType().name();
			
			if(item.getType().equals(Material.AIR)) {
				Chat.sendPlayerMessage(p, "&bThe item in your hand can not be air!");
				return;
			}
			
			Map<Enchantment, Integer> enchants = item.getEnchantments();
			ArrayList<Enchantment> itemEnchants = new ArrayList<Enchantment>();
			ArrayList<Integer> itemLevels = new ArrayList<Integer>();
			
			//Gets all the enchants from the map
			for(Entry<Enchantment, Integer> en : enchants.entrySet()){
				itemEnchants.add(en.getKey());
			}
			
			//Gets all the levels from the map
			for(Entry<Enchantment, Integer> en : enchants.entrySet()){
				itemLevels.add(en.getValue());
			}
			
			//Started to created the item line you see in the config
			itemLine += "name: " + itemName + " amount: " + amount + " Enchants: ";
			
			for(int x = 0; x < itemEnchants.size();x++) {
				
				Enchantment currentEnchant = itemEnchants.get(x);
				int currentLevel = itemLevels.get(x);
				
				itemLine += currentEnchant.getName() + "-" + currentLevel + "; "; 
			}
			
			List<String> itemList = (List<String>) config.getList("Tiers." + tier + ".Properties.Rewards.Items");
			
			//If the item has no enchants, just print out the name and the amount. Otherwise, print out the enchants
			//Each enchant is separated by a semi-colon
			//The dash (-) separates the enchant and level
			if(enchants.isEmpty()) {
				itemLine = "name: " + itemName + " amount: " + amount;
				itemList.add(itemLine);
				config.set("Tiers." + tier + ".Properties.Rewards.Items", itemList);
			}else {
				itemList.add(itemLine);
				config.set("Tiers." + tier + ".Properties.Rewards.Items", itemList);
			}
			
			Chat.sendPlayerMessage(p, "&bYour item has been added as a reward!");
			
			plugin.saveConfig();
		}
		
		public void rewardRemoveItem() {
			
		}
		
		//For Commands
		//bbr rewardadd (tier) (rewardType)
		public void rewardAddCommand(Player p,String tier, String rewardType) {	
			
		}
		
		

		public boolean checkCrateReloaded() {
			return plugin.getServer().getPluginManager().isPluginEnabled("CrateReloaded");
		}
}