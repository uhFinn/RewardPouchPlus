package uhfinn.pouchplus.Modules;

import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HEX {

    public static String hexTranslate(String str) {
        Pattern reg = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher match = reg.matcher(str);

        while (match.find()) {
            String color = str.substring(match.start(), match.end());
            str = str.replace(color, ChatColor.of(color) + "");
            match = reg.matcher(str);
        }
        return ChatColor.translateAlternateColorCodes('&', str);
    }

}
