package uhfinn.pouchplus.Modules.PouchActions;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import uhfinn.pouchplus.Modules.YML.ConfigCore;
import uhfinn.pouchplus.Modules.YML.PouchFileData;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class PouchUse {

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

    /**
     * Generates a value through a weight based RNG system
     * @param hashMap The LinkedHashMap with all key value pairs for the weighted RNG
     * @param total The total value of each individual value in the LinkedHashMap added together
     * @return The Prize ID (or LHM key) of the weighted prize
     */
    private static String weightCalc(LinkedHashMap<String, Integer> hashMap, int total) {
        int rng = (int) Math.floor(Math.random() * total);
        for(String key : hashMap.keySet()) {
            Integer value = hashMap.get(key);
            if(rng < value) {
                return key;
            }
        }
        return null;
    }

    /**
     * Issues all commands through console associated with the prize
     * @param p The {@link Player} to issue the command for
     * @param pouchPrizePath The yml path to the pouch prize
     * @param file The {@link FileConfiguration} of the pouch
     */
    private static void issueReward(Player p, String pouchPrizePath, FileConfiguration file) {
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        List<String> commands = file.getStringList(pouchPrizePath + ".Commands");

        for(String command : commands) {
            command = command.replaceAll("<playername>", p.getName()).replaceAll("<playerName>", p.getName()).replaceAll("<PlayerName>", p.getName());
            Bukkit.dispatchCommand(console, command);
        }
    }

    public static void usePouch(Player p, String pouchFile) {
        ItemStack itemStack = p.getInventory().getItemInMainHand();
        itemStack.setAmount(itemStack.getAmount()-1);

        FileConfiguration pouchConfig = PouchFileData.getPouchFile(pouchFile);
        LinkedHashMap<String, Integer> pouchPrizes = getPrizes(pouchConfig);

        String prizeKey = weightCalc(pouchPrizes, calcSum(pouchPrizes));
        issueReward(p, "Prizes." + prizeKey + ".", pouchConfig);
    }

}
