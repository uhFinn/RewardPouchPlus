package uhfinn.pouchplus.Modules.YML;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import uhfinn.pouchplus.Main;

import java.io.File;
import java.util.logging.Logger;

public class YmlHandler {

    private static final Logger logger = Bukkit.getLogger();

    private static FileConfiguration Config = YamlConfiguration.loadConfiguration(new File(Main.INSTANCE().getDataFolder() + "/Config.yml"));
    private static FileConfiguration Messages = YamlConfiguration.loadConfiguration(new File(Main.INSTANCE().getDataFolder() + "/Messages.yml"));

    public static FileConfiguration getConfig() {
        return Config;
    }
    public static FileConfiguration getMessages() {
        return Messages;
    }

    public static int Reload() {
        int generated = 0;
        File Configur = new File(Main.INSTANCE().getDataFolder() + "/Config.yml");
        if(!Configur.exists()) {
            Main.INSTANCE().saveResource("Config.yml", false);
            generated += 1;
            logger.info("Pouch+ >>  Generated Config.yml File");
        }
        File Messageser = new File(Main.INSTANCE().getDataFolder() + "/Messages.yml");
        if(!Messageser.exists()) {
            Main.INSTANCE().saveResource("Messages.yml", false);
            generated += 1;
            logger.info("Pouch+ >>  Generated Messages.yml File");
        }
        Config = YamlConfiguration.loadConfiguration(Configur);
        Messages = YamlConfiguration.loadConfiguration(Messageser);

        return generated;
    }
}
