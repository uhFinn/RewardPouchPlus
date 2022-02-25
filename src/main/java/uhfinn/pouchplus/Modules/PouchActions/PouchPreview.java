package uhfinn.pouchplus.Modules.PouchActions;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import uhfinn.pouchplus.Main;
import uhfinn.pouchplus.Modules.HEX;
import uhfinn.pouchplus.Modules.YML.ConfigCore;
import uhfinn.pouchplus.Modules.YML.GetPouchPrizeValues;
import uhfinn.pouchplus.Modules.YML.PouchFileData;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class PouchPreview {

    private static Logger logger = Bukkit.getLogger();

    /**
     * Gets a linked hash map of all valid prize keys and their associated weight
     * @param pouchConfig The {@link FileConfiguration} for the pouch
     * @return Returns a LinkedHashMap containing a string key with an integer value
     */
    private static LinkedHashMap<String, Integer> getPrizes(FileConfiguration pouchConfig) {
        LinkedHashMap<String, Integer> linkedHashMap = new LinkedHashMap<>();

        Set<String> keys = pouchConfig.getConfigurationSection("Prizes").getKeys(false);
        for(String key : keys) {
            if(validateKey(pouchConfig, key)) linkedHashMap.put(key, pouchConfig.getInt("Prizes." + key + ".Weight"));
        }

        return linkedHashMap;
    }

    /**
     * Validates that a key contains all necessary parameters, if not it is skipped over and the issue is logged to console
     * @param file The {@link FileConfiguration} for the pouch
     * @param key The Prize ID to be checked
     * @return a boolean value representing the prizes validity
     */
    private static boolean validateKey(FileConfiguration file, String key) {
        String pathDeep = "Prizes." + key + ".";
        if(file.contains(pathDeep + "Name") && file.contains(pathDeep + "Icon.Item") && file.contains(pathDeep + "Icon.Amount") && file.contains(pathDeep + "Weight") && file.contains(pathDeep + "Commands")) {
            return true;
        } else {
            if(!ConfigCore.suppressWarnings()) logger.warning("Pouch+ >>  An invalid format was detected for Prize ID: " + key + ", In the file: " + file.getName() + "\n           This prize was skipped over and was ignored from prize weighting\n           We suggest fixing the issue or removing the prize, You can also suppress these warnings in the config; 'SuppressWarnings'");
            return false;
        }
    }

    /**
     * Calculates the total value from each adding each individual value within the provided LinkedHashMap
     * @param hashMap The LinkedHashMap to be scanned for values
     * @return The total value of all the LinkedHashMaps values added together
     */
    private static int calcSum(LinkedHashMap<String, Integer> hashMap) {
        int total = 0;
        for(String key : hashMap.keySet()) {
            total += hashMap.get(key);
        }

        return total;
    }


    public static void openUI(Player p, String pouchFile) {
        ItemStack itemStack = p.getInventory().getItemInMainHand();
        itemStack.setAmount(itemStack.getAmount()-1);

        FileConfiguration pouchConfig = PouchFileData.getPouchFile(pouchFile);
        LinkedHashMap<String, Integer> pouchPrizes = getPrizes(pouchConfig);
        int total = calcSum(pouchPrizes);
        float multPercentageFactor = 100/total;

        int prizes = pouchPrizes.size();
        if(prizes > 28) prizes = 28;

        Inventory inv = p.getServer().createInventory(null, 54, HEX.hexTranslate(pouchConfig.getString("Name")));
        {
            int[] glassPane = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53};
            ItemStack gP = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
            ItemMeta gPMeta = gP.getItemMeta();
            gPMeta.setDisplayName(" ");
            PersistentDataContainer data = gPMeta.getPersistentDataContainer();
            data.set(new NamespacedKey(Main.INSTANCE(), "noClick"), PersistentDataType.INTEGER, 1);
            gP.setItemMeta(gPMeta);
            for (int i : glassPane) {
                inv.setItem(i, gP);
            }
        }

        int[] available = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43};
        int i = 0;
        for(String key : pouchPrizes.keySet()) {
            if(i <= prizes) {
                i++;
                Integer value = pouchPrizes.get(key);
                ItemStack item = new ItemStack(GetPouchPrizeValues.getIconType(pouchConfig, key), GetPouchPrizeValues.getIconAmount(pouchConfig, key));
                ItemMeta itemM = item.getItemMeta();
                List<String> lore = new ArrayList<>();
                for(String loreLine : GetPouchPrizeValues.getGUILore(pouchConfig, key)) {
                    lore.add(HEX.hexTranslate(loreLine.replaceAll("<chance>", String.format("%.2f", value*multPercentageFactor))));
                }
                itemM.setLore(lore);
                PersistentDataContainer data = itemM.getPersistentDataContainer();
                data.set(new NamespacedKey(Main.INSTANCE(), "noClick"), PersistentDataType.INTEGER, 1);
                item.setItemMeta(itemM);

                inv.setItem(available[i], item);
            }
        }
        p.closeInventory();
        p.openInventory(inv);
    }

}
