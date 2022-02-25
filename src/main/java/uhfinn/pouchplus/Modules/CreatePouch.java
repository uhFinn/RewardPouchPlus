package uhfinn.pouchplus.Modules;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import uhfinn.pouchplus.Main;

import java.io.File;

public class CreatePouch {

    public ItemStack create(String fileName) {
        FileConfiguration file = YamlConfiguration.loadConfiguration(new File(Main.INSTANCE().getDataFolder() + "fileName")); //todo
        ItemStack pouch = new ItemStack(Material.valueOf(file.getString("Pouch.Item")));
        ItemMeta pouchMeta = pouch.getItemMeta();

        pouchMeta.setDisplayName(HEX.hexTranslate(file.getString("Name")));
        if(file.getBoolean("Pouch.Enchanted")) {
            pouchMeta.addEnchant(Enchantment.ARROW_INFINITE, 100, true);
            pouchMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        pouchMeta.setLore(file.getStringList("Pouch.Lore"));

        PersistentDataContainer pouchData = pouchMeta.getPersistentDataContainer();
        pouchData.set(new NamespacedKey(Main.INSTANCE(), "pouch"), PersistentDataType.STRING, fileName);

        pouch.setItemMeta(pouchMeta);

        return pouch;
    }

}
