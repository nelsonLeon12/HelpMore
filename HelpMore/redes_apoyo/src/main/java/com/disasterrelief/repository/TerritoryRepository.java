package com.disasterrelief.repository;

import com.disasterrelief.enums.RiskLevel;
import com.disasterrelief.entities.Database;
import com.disasterrelief.entities.TerritoryRecord;

import java.util.List;

/**
 * Repositorio de acceso a datos para registros de territorio.
 */
public class TerritoryRepository {

    private final Database db = Database.getInstance();

    public List<TerritoryRecord> findAll() {
        return db.getAllTerritories();
    }

    /**
     * Actualiza el nivel de riesgo del registro de territorio principal.
     */
    public void updateRiskLevel(RiskLevel level) {
        db.getTerritoryRecord().setRiskLevel(level);
        System.out.println("Nivel de riesgo actualizado a: " + level);
    }

    /**
     * Registra una accion de ayuda en el registro de territorio principal.
     */
    public void logReliefAction() {
        db.getTerritoryRecord().logReliefAction();
    }

    public TerritoryRecord getPrimary() {
        return db.getTerritoryRecord();
    }
}
