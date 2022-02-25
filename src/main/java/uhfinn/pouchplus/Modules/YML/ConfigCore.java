package uhfinn.pouchplus.Modules.YML;

import uhfinn.pouchplus.Modules.HEX;

public class ConfigCore {

    public static String getPrefix(boolean hexTranslate) {
        if(hexTranslate) return HEX.hexTranslate(YmlHandler.getConfig().getString("PluginPrefix"));
        return YmlHandler.getConfig().getString("PluginPrefix");
    }
    public static boolean suppressWarnings() {
        return YmlHandler.getConfig().getBoolean("SuppressWarnings");
    }

}
