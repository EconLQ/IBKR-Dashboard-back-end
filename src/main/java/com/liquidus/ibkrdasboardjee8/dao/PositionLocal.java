package com.liquidus.ibkrdasboardjee8.dao;

import com.liquidus.ibkrdasboardjee8.entity.Position;

import javax.ejb.Local;
import java.io.Serializable;
import java.util.List;

@Local
public interface PositionLocal extends Serializable {

    void savePosition(Position position);

    void addPosition(Position position);

    Position findPositionById(int positionId);

    List<Position> getAllPositions();

    void deletePosition(Position position);

    void updatePosition(Position position);
}
