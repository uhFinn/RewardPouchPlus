package uhfinn.pouchplus.Listeners;

import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.persistence.PersistentDataType;
import uhfinn.pouchplus.Main;

public class InventoryClick implements Listener {

    @EventHandler
    public void onGuiClick(InventoryClickEvent event) {
        //really ugly but ram efficient
        if(event.getCurrentItem() == null) return;
        if(event.getCurrentItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(Main.INSTANCE(), "noClick"), PersistentDataType.INTEGER)) event.setCancelled(true);
    }

}
