/*
 * You can use the following import statements
 * import org.springframework.beans.factory.annotation.Autowired;
 * import org.springframework.http.HttpStatus;
 * import org.springframework.jdbc.core.JdbcTemplate;
 * import org.springframework.stereotype.Service;
 * import org.springframework.web.server.ResponseStatusException;
 * import java.util.ArrayList;
 * 
 */

// Write your code herepcka

package com.example.player.service;

import com.example.player.repository.*;
import com.example.player.model.Player;
import com.example.player.service.PlayerH2Service;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;
import com.example.player.model.PlayerRowMapper;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
//import com.example.player.*;

@Service
public class PlayerH2Service implements PlayerRepository {

    @Autowired
    private JdbcTemplate db;

    @Override
    public ArrayList<Player> getAllPlayer() {
        List<Player> playerList = db.query("Select * from TEAM", new PlayerRowMapper());
        ArrayList<Player> players = new ArrayList<>(playerList);
        return players;
    }

    @Override
    public Player addPlayer(Player player) {
        db.update("insert into TEAM (playerName, jerseyNumber, role) values(?, ?, ?)", player.getPlayerName(),
                player.getJerseyNumber(), player.getRole());
        Player savedPlayer = db.queryForObject("select * from TEAM where playerId = ?, jerseyNumber = ? and role = ?",
                new PlayerRowMapper(), player.getPlayerName(), player.getJerseyNumber(), player.getRole());
        return savedPlayer;
    }

    @Override
    public Player getPlayerById(int playerId) {
        try {
            Player player = db.queryForObject("select * from TEAM where playerId = ?", new PlayerRowMapper(), playerId);

            return player;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }

    @Override
    public Player updatePlayer(int playerId, Player player) {
        if (player.getPlayerName() != null) {
            db.update("update TEAM set playerName = ? where playerId = ?", player.getPlayerName(), playerId);
        }
        if (player.getJerseyNumber() != 0) {
            db.update("update TEAM set jerseyNumber = ? where playerId = ?", player.getJerseyNumber(), playerId);
        }
        if (player.getRole() != null) {
            db.update("update TEAM set role = ? where playerId = ?", player.getRole(), playerId);
        }
        return getPlayerById(playerId);
    }

    @Override
    public void deletePlayer(int playerId) {
        db.update("delete from Team where playerId = ?", playerId);
    } 

}