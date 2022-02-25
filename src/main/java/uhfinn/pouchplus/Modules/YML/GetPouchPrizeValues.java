package uhfinn.pouchplus.Modules.YML;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class GetPouchPrizeValues {

    public static String getName(FileConfiguration pouchFile, String id) {
        return pouchFile.getString("Prizes." + id + ".Name");
    }

    public static Material getIconType(FileConfiguration pouchFile, String id) {
        String Icon = pouchFile.getString("Prizes." + id + ".Icon.Item");
        if(Icon == null || Icon.equals("") || Icon.equals(" ") || Icon.equals("AIR")) {
            return Material.BARRIER;
        } else {
            return Material.valueOf(Icon);
        }
    }

    public static int getIconAmount(FileConfiguration pouchFile, String id) {
        int amt = pouchFile.getInt("Prizes." + id + ".Icon.Amount");
        if(amt < 1 || amt > 64) return 1;
        return amt;
    }

    public static int getWeight(FileConfiguration pouchFile, String id) {
        return pouchFile.getInt("Prizes." + id + ".Weight");
    }

    public static List<String> getCommands(FileConfiguration pouchFile, String id) {
        return pouchFile.getStringList("Prizes." + id + ".Commands");
    }

    public static List<String> getGUILore(FileConfiguration pouchFile, String id) {
        if(pouchFile.contains("Prizes." + id + ".GUILore")) {
            return pouchFile.getStringList("Prizes." + id + ".GUILore");
        } else {
            List<String> l = new ArrayList<>();
            l.add("&7Win Chance: &f<chance>%");
            return l;
        }
    }

}
