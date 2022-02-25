package uhfinn.pouchplus.Modules.YML;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import uhfinn.pouchplus.Main;

import java.io.File;

public class PouchFileData {

    public static FileConfiguration getPouchFile(String fileName) {
        if(validatePouchExist(fileName)) return YamlConfiguration.loadConfiguration(new File(Main.INSTANCE().getDataFolder() + fileName + ".yml"));
        return null;
    }

    public static boolean validatePouchExist(String fileName) {
        return new File(Main.INSTANCE().getDataFolder() + fileName + ".yml").exists();
    }

}
