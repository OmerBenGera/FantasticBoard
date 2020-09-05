package com.ome_r.fantasticboard.boards;

import com.ome_r.fantasticboard.FantasticBoard;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BoardsHandler {

    private List<Map.Entry<List<String>, Long>> lines;
    private List<UUID> uuids;
    private boolean usingPlaceholders;

    public BoardsHandler(List<Map.Entry<List<String>, Long>> lines){
        this.lines = new ArrayList<>(lines);
        this.uuids = new ArrayList<>();
        this.usingPlaceholders = false;
        run();
    }

    public void setUsingPlaceholders(boolean usingPlaceholders){
        this.usingPlaceholders = usingPlaceholders;
    }

    public void addPlayer(Player pl){
        uuids.add(pl.getUniqueId());
        for(int i = 0; i < lines.size(); i++)
            updateLine(pl, i, lines.get(i).getKey().get(0));
    }

    public void removePlayer(Player pl){
        uuids.remove(pl.getUniqueId());
    }

    private void updateLine(Player pl, int index, String line){
        Scoreboard board = pl.getScoreboard();
        String str = usingPlaceholders ? PlaceholderAPI.setPlaceholders(pl, line) : line;

        if(board.getObjective(DisplaySlot.SIDEBAR) == null ||
                !board.getObjective(DisplaySlot.SIDEBAR).getName().equals("fantasticboard")){
            pl.setScoreboard(getDefault());
            board = pl.getScoreboard();
        }

        if(index == 0){
            Objective objective = board.getObjective(DisplaySlot.SIDEBAR);

            if(str.length() > 32)
                str = str.substring(0, 32);

            objective.setDisplayName(str);
        }

        else{
            Team t = board.getTeam( (index - 1) + "");
            String prefix = str, suffix = "";

            if(prefix.length() > 16){
                //Gets the first prefix and suffix
                prefix = str.substring(0, 16);
                suffix = ChatColor.getLastColors(prefix) + str.substring(16, str.length());

                //Makes sure the colors won't be fucked up
                if(str.charAt(15) == '§'){
                    prefix = str.substring(0, 15);
                    suffix = ChatColor.getLastColors(prefix) + str.substring(15, str.length());
                }

                //Makes sure the suffix is less then 16
                if(suffix.length() > 16)
                    suffix = suffix.substring(0, 16);
            }

            //Removes '§' at the end
            if(suffix.endsWith("§"))
                suffix = suffix.substring(0, suffix.length() - 1);

            t.setPrefix(prefix);
            t.setSuffix(suffix);
        }

    }

    private Scoreboard getDefault(){
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();

        Objective objective = board.registerNewObjective("fantasticboard", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        int size = lines.size() - 1;
        for(int i = 0; i < size; i++){
            Team t = board.registerNewTeam(i + "");
            t.addEntry(ChatColor.values()[i] + "");
            objective.getScore(ChatColor.values()[i] + "").setScore(size - i);
        }

        return board;
    }

    private void run(){
        for(int i = 0; i < lines.size(); i++){
            Map.Entry<List<String>, Long> entry = lines.get(i);
            final int index = i;
            new BukkitRunnable(){
                int counter = 0;

                @Override
                public void run() {
                    for(UUID uuid : uuids) {
                        Player pl = Bukkit.getPlayer(uuid);
                        if(pl != null)
                            updateLine(pl, index, entry.getKey().get(counter));
                    }

                    counter++;

                    if(counter >= entry.getKey().size())
                        counter = 0;
                }
            }.runTaskTimer(FantasticBoard.getInstance(), 0, entry.getValue());
        }
    }

}
