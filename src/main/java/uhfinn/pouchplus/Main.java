package uhfinn.pouchplus;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import uhfinn.pouchplus.Listeners.ClickItem;
import uhfinn.pouchplus.Listeners.InventoryClick;
import uhfinn.pouchplus.Modules.YML.ConfigCore;
import uhfinn.pouchplus.Modules.YML.YmlHandler;

import java.util.logging.Logger;

public final class Main extends JavaPlugin {

    private static Main _instance;
    public static Main INSTANCE()
    {
        return _instance;
    }
    Logger logger = Bukkit.getLogger();

    @Override
    public void onEnable() {
        _instance=this;
        // Plugin startup logic


        //File handling
        Main.INSTANCE().saveResource("Pouches/Example.yml", true); //Potential update checking
        YmlHandler.Reload();
        //
        // Initialising Listeners
        addListener(new ClickItem());
        addListener(new InventoryClick());
        //


        logger.info("Pouch+ >> Has started successfully!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void addListener(Listener listener)
    {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }
}
