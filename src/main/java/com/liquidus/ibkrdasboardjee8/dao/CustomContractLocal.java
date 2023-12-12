package com.liquidus.ibkrdasboardjee8.dao;

import com.liquidus.ibkrdasboardjee8.entity.CustomContract;

import javax.ejb.Local;

@Local
public interface CustomContractLocal {
    void saveContract(CustomContract contract);

    boolean isContractExists(CustomContract contract);

    CustomContract findContractByPositionId(int positionId);
}
