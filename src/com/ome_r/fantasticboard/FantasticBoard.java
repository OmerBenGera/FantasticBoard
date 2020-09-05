package com.ome_r.fantasticboard;

import com.ome_r.fantasticboard.boards.BoardsHandler;
import com.ome_r.fantasticboard.data.DataHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class FantasticBoard extends JavaPlugin{

    private static FantasticBoard main;
    private static FantasticAPI api;
    private DataHandler data;

    private BoardsHandler boardsHandler;

    @Override
    public void onEnable() {
        setupClasses();
        loadListeners();
        updateScoreboards();
    }

    private void loadListeners(){
        getServer().getPluginManager().registerEvents(new Listeners(), this);
    }

    private void setupClasses(){
        main = this;
        api = new FantasticAPI();
        data = new DataHandler();
        boardsHandler = new BoardsHandler(data.getLines());

        if(getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            boardsHandler.setUsingPlaceholders(true);
        }

    }

    private void updateScoreboards(){
        for(Player pl : Bukkit.getOnlinePlayers())
            FantasticBoard.getAPI().startUpdating(pl);
    }

    public BoardsHandler getBoardsHandler() {
        return boardsHandler;
    }

    public static FantasticBoard getInstance(){
        return main;
    }

    public static FantasticAPI getAPI(){
        return api;
    }

}
