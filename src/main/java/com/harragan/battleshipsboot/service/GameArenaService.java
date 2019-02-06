package com.harragan.battleshipsboot.service;

import com.harragan.battleshipsboot.model.ships.Ship;
import org.springframework.stereotype.Service;
import com.harragan.battleshipsboot.model.game.BoardPosition;
import com.harragan.battleshipsboot.model.game.GameArena;
import com.harragan.battleshipsboot.model.game.Orientation;
import com.harragan.battleshipsboot.service.exceptions.IllegalBoardPlacementException;

import java.util.List;
import java.util.Optional;

@Service
public class GameArenaService {

    public GameArena createGameArena(int gameArenaSize) {
        GameArena gameArena = new GameArena(gameArenaSize);
        return gameArena;
    }

    public void addShip(Ship ship, GameArena gameArena){
        setOccupiedPositionsOfShip(ship);
        checkShipCanBePlaced(ship, gameArena);
        addShipToGameArena(ship, gameArena);
    }

    private void addShipToGameArena(Ship ship, GameArena gameArena) {
        gameArena.addShip(ship);
        if(gameArena.getShipsOnBoard().size() == 5) {
            gameArena.setAllShipsPlaced(true);
        }
    }

    private void checkShipCanBePlaced(Ship ship, GameArena gameArena) {
        if(shipAlreadyExists(ship, gameArena)) {
            throw new IllegalBoardPlacementException("The " + ship.getClass().getSimpleName().toLowerCase()
                    + " has already been placed on the board");
        }

        if(isShipOffBoard(ship, gameArena)) {
            throw new IllegalBoardPlacementException("Ship is positioned off board."
            + " Please ensure that all positions are valid positions.");

        }
        if(positionsAlreadyOccupied(ship, gameArena)) {
            throw new IllegalBoardPlacementException("This board position would cause the ship"
                    + " to overlap with another ship already placed on the board.");
        }
    }

    private boolean shipAlreadyExists(Ship ship, GameArena gameArena) {
        return (gameArena.isShipOnBoard(ship));
    }

    public Ship getShipAtPosition(BoardPosition boardPosition, GameArena gameArena) {
        for(Ship aShip: gameArena.getShipsOnBoard()) {
            if(aShip.getOccupiedBoardPositions().contains(boardPosition)) {
                return aShip;
            }
        }
        return null;
    }

    public boolean isShipOffBoard(Ship ship, GameArena gameArena) {
        if(ship.getOrient() == Orientation.VERTICAL && ship.getOccupiedPosition(0).getRow()
                + ship.getLength() > gameArena.getGameArenaSize() + 1) {
            return true;
        }
        if(ship.getOrient() == Orientation.HORIZONTAL
                && ship.getOccupiedPosition(0).getCol() + ship.getLength() > gameArena.getGameArenaSize() + 65) {
            return true;
        }
        return false;
    }

    public boolean positionsAlreadyOccupied(Ship ship, GameArena gameArena) {
        for(Ship aShip : gameArena.getShipsOnBoard()){
            boolean isShipOccupyingPosition = aShip.getOccupiedBoardPositions()
                    .stream()
                    .anyMatch(ship.getOccupiedBoardPositions()::contains);

            if(isShipOccupyingPosition)
            {
                return true;
            }
        }
        return false;
    }

    public void registerHit(BoardPosition boardPosition, GameArena gameArena) {
        boardPosition.setHit();
        for(Ship ship : gameArena.getShipsOnBoard()) {
            if(ship.getOccupiedBoardPositions().contains(boardPosition)) {
                setHitPositionOnShip(boardPosition, ship);
                if(ship.isSunk() == true) {
                    gameArena.addSunkenShip(ship);
                }
            }
            gameArena.addShotPosition(boardPosition);
        }
    }

    public BoardPosition getOccupiedPositionsOfShip(BoardPosition boardPosition, Ship ship) {
        Optional<BoardPosition> occupiedPosition = ship.getOccupiedBoardPositions().stream()
                .filter(p -> p.equals(boardPosition)).findFirst();

        if(occupiedPosition.isPresent()) {
            return occupiedPosition.get();
        }
        return null;
    }

    public void setOccupiedPositionsOfShip(Ship ship) {
        if (ship.getOrient() == Orientation.VERTICAL) {
            for (int i = 1; i < ship.getLength(); i++) {
                BoardPosition pos = BoardPositionFactory.createBoardPosition(ship.getOccupiedPosition(0).getCol(),
                        ship.getOccupiedPosition(0).getRow() + i);
                List<BoardPosition> occupiedBoardPositions = ship.getOccupiedBoardPositions();
                occupiedBoardPositions.add(i, pos);
                ship.setOccupiedPosition(occupiedBoardPositions);
            }
        } else {
            for (int i = 1; i < ship.getLength(); i++) {
                    char inputCol = ship.getOccupiedPosition(0).getCol();
                    int input = inputCol + i;
                    BoardPosition pos = BoardPositionFactory.createBoardPosition((char) input, ship.getOccupiedPosition(0).getRow());
                    List<BoardPosition> occupiedBoardPositions = ship.getOccupiedBoardPositions();
                    occupiedBoardPositions.add(i, pos);
                    ship.setOccupiedPosition(occupiedBoardPositions);
            }
        }
    }

    public void setHitPositionOnShip(BoardPosition boardPosition, Ship ship) {
        for(BoardPosition aBoardPosition : ship.getOccupiedBoardPositions()) {
            if(aBoardPosition.equals(boardPosition)) {
                aBoardPosition.setHit();
            }
        }

        boolean allPositionsHit = ship.getOccupiedBoardPositions()
                .stream()
                .allMatch(p -> p.isHit());

        if(allPositionsHit) {
            ship.setSunk(true);
        }
    }
}
