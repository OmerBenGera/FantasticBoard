package com.ome_r.fantasticboard.data;

import com.ome_r.fantasticboard.FantasticBoard;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataHandler {

    private List<Map.Entry<List<String>, Long>> lines;
    private File file;

    public DataHandler(){
        this.lines = new ArrayList<>();
        this.file = new File(FantasticBoard.getInstance().getDataFolder(), "scoreboard.yml");
        loadData();
    }

    public List<Map.Entry<List<String>, Long>> getLines(){
        return new ArrayList<>(lines);
    }

    private void loadData(){
        if(!file.exists())
            FantasticBoard.getInstance().saveResource("scoreboard.yml", false);

        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        if(!cfg.contains("lines")){
            file.delete();
            FantasticBoard.getInstance().saveResource("scoreboard.yml", false);
            cfg = YamlConfiguration.loadConfiguration(file);
        }

        for(String str : cfg.getConfigurationSection("lines").getKeys(false)){
            long interval = cfg.getLong("lines." + str  + ".interval");
            List<String> lines = new ArrayList<>();

            for(String line : cfg.getStringList("lines." + str + ".text"))
                lines.add(ChatColor.translateAlternateColorCodes('&', line));

            this.lines.add(new Map.Entry<List<String>, Long>() {
                @Override
                public List<String> getKey() {
                    return lines;
                }

                @Override
                public Long getValue() {
                    return interval;
                }

                @Override
                public Long setValue(Long value) {
                    return null;
                }
            });
        }

    }

}
