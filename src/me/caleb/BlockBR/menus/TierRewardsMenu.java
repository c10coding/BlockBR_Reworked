package me.caleb.BlockBR.menus;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import me.caleb.BlockBR.Main;


/*
 * This menu shows the rewards for THIS TIER
 * Each one of these menus has a unique value - It's tier
 */

/*
 * Next time i hop on, i need to make it to where all the materials will be different.
 */
public class TierRewardsMenu extends AbstractMenu implements Listener, InventoryHolder{

	private final String PATH;
	private final String tier, crate;
	private final Material mat;
	private final int money;
	private final List<String> items, commands;
	
	public TierRewardsMenu(Main plugin, Material mat) {
		super(plugin,"Tier Rewards", 9);
		Bukkit.getPluginManager().registerEvents(this,plugin);
		
		this.mat = mat;
		this.tier = findTier();
		this.PATH ="Tiers." + tier + ".Properties.Rewards.";
		this.money = getMoney();
		this.items = getItems();
		this.commands = getCommands();
		this.crate = getCrate();
	}

	private String findTier() {
		List<String> tierList = config.getStringList("TierList");
		for(String tier : tierList) {
			String currentMat = config.getString("Tiers." + tier + ".Properties.Material");
			if(mat.name().equalsIgnoreCase(currentMat)) {
				return tier;
			}
		}
		return null;
	}
	
	private int getMoney() {
		return config.getInt(PATH + "Money");
	}
	
	private List<String> getItems(){
		return config.getStringList(PATH + "Items");
	}
	
	private String getCrate() {
		return config.getString(PATH + "Crate");
	} 
	
	private List<String> getCommands(){
		return config.getStringList(PATH + "Commands");
	}
	
	@Override
	public void initializeItems(Player p) {
		inv.addItem(createGuiItem(Material.ITEM_FRAME, chat("&6Items"), chat("&rClick me to see what items you get"), chat("&rwhen you complete this tier!")));
		inv.addItem(createGuiItem(Material.GOLD_NUGGET, chat("&6Money"),chat("&rYou get &5&l" + money) , chat("&rwhen you complete this tier!")));
		inv.addItem(createGuiItem(Material.CHEST, chat("&6Crate"), chat("&rYou get a &5&l" + crate), chat("&rwhen you complete this tier!")));
		inv.addItem(createGuiItem(Material.COMMAND_BLOCK, chat("&6Commands"), chat("&rClick me to see what commands are ran"), chat("&rwhen you complete this tier!")));
		fillMenu();
	}

	@EventHandler
	protected void onInventoryClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
        ItemStack clickedItem = e.getCurrentItem();
		
        if (e.getInventory().getHolder() != this) return;
        
        if (e.getClick().equals(ClickType.NUMBER_KEY)) e.setCancelled(true);
		
		try {
			if(e.getRawSlot() == 0) {
				
			}
		}catch(NullPointerException n) {
			
		}
		
		e.setCancelled(true);
	}
	
	@Override
	protected void fillMenu() {
		for(int x = 4; x < (inv.getSize()-1);x++) {
			inv.setItem(x, createGuiItem());
		}
		inv.addItem(createGuiItem(Material.RED_WOOL, chat("&6Go back"), chat("&rClick me to go back to the"), chat("&rlast menu!")));
	}
	
}
