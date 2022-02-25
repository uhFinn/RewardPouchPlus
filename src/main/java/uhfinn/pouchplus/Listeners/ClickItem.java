package uhfinn.pouchplus.Listeners;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import uhfinn.pouchplus.Main;
import uhfinn.pouchplus.Modules.HEX;
import uhfinn.pouchplus.Modules.PouchActions.PouchPreview;
import uhfinn.pouchplus.Modules.PouchActions.PouchUse;
import uhfinn.pouchplus.Modules.YML.ConfigCore;
import uhfinn.pouchplus.Modules.YML.PouchFileData;

public class ClickItem implements Listener {

    @EventHandler
    public void onPlayerClickItem(PlayerInteractEvent event) {
        Player p = event.getPlayer();

        if(p.getInventory().getItemInMainHand().getType() != Material.AIR) {
            PersistentDataContainer dataContainer = p.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer();
            if(dataContainer.has(new NamespacedKey(Main.INSTANCE(), "pouch"), PersistentDataType.STRING)) {
                String pouchFile = dataContainer.get(new NamespacedKey(Main.INSTANCE(), "pouch"), PersistentDataType.STRING);
                if(!PouchFileData.validatePouchExist(pouchFile)) {
                    p.sendMessage(HEX.hexTranslate(ConfigCore.getPrefix(true) + " &c&oOops... &cThat pouch data seems to have been deleted &8[ERR:NoPouchData]"));
                }
                if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
                    // Preview Pouch
                    PouchPreview.openUI(p, pouchFile);
                } else if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR) {
                    // Use Pouch
                    PouchUse.usePouch(p, pouchFile);
                }
            }
        }
    }

}
