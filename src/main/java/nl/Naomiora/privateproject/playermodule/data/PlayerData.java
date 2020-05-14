package nl.Naomiora.privateproject.playermodule.data;

import lombok.Data;
import nl.Naomiora.privateproject.PrivateProject;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Optional;
import java.util.UUID;

@Data
public class PlayerData {
    private UUID uuid;
    private Player player;
    private Optional<String> offlineUUID;

    public PlayerData(Player player) {
        this.uuid = player.getUniqueId();
        this.player = player;
        this.offlineUUID = Optional.empty();
        this.setOnlineUUID(player);
    }

    private void setOnlineUUID(Player player) {
        new BukkitRunnable() {
            public void run() {
                try {
                    URL oracle = new URL("https://mcuuid.net/?q=" + player.getName());
                    URLConnection yc = oracle.openConnection();
                    yc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
                    yc.connect();

                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            yc.getInputStream()));
                    String inputLine;
                    boolean uuidNextLine = false;
                    while ((inputLine = in.readLine()) != null) {
                        if(uuidNextLine) {
                            for(String string : inputLine.split(" ")) {
                                if(string.startsWith("value")) {
                                    offlineUUID = Optional.of(string.replace("value=\"", "").replace("\"", ""));
                                }
                            }
                        }

                        uuidNextLine = inputLine.toLowerCase().contains("full uuid");
                    }
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(PrivateProject.getInstance());
    }
}
