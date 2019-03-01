package com.harragan.battleshipsboot.model.game;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

import static javax.persistence.CascadeType.PERSIST;

@Entity
public class Game {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @OneToMany(cascade = PERSIST)
  private List<Player> players;

  private int turnIndex;

  private int noOfPlayers;

  private int gameArenaSize;

  public Game() {}

  public Game(int noOfPlayers, int gameArenaSize) {
    this.players = new LinkedList<>();
    this.noOfPlayers = noOfPlayers;
    this.gameArenaSize = gameArenaSize;
  }

  public int getTurnIndex() {
    return turnIndex;
  }

  public void setTurnIndex(int turnIndex) {
    this.turnIndex = turnIndex;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public LinkedList<Player> getPlayers() {
    return new LinkedList<>(players);
  }

  public void setPlayers(LinkedList<Player> players) {
    this.players = players;
  }

  public int getMaxPlayers() {
    return noOfPlayers;
  }

  public int getGameArenaSize() {
    return gameArenaSize;
  }
}
