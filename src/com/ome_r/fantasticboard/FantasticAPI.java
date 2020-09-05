package com.ome_r.fantasticboard;

import org.bukkit.entity.Player;

public class FantasticAPI {

    public void startUpdating(Player pl){
        FantasticBoard.getInstance().getBoardsHandler().addPlayer(pl);
    }

    public void stopUpdating(Player pl){
        FantasticBoard.getInstance().getBoardsHandler().removePlayer(pl);
    }

}
