package com.disasterrelief.entities;

import com.disasterrelief.enums.RiskLevel;

/**
 * Representa un territorio de intervencion registrado.
 * Controla el nivel de riesgo y la cantidad de ayudas registradas en esa zona.
 */
public class TerritoryRecord {

    private static int idCounter = 1;

    private final int  id;
    private       String    zone;
    private       RiskLevel riskLevel;
    private       int       registeredReliefCount;

    public TerritoryRecord(String zone, RiskLevel riskLevel) {
        this.id                   = idCounter++;
        this.zone                 = zone;
        this.riskLevel            = riskLevel;
        this.registeredReliefCount = 0;
    }

    /**
     * Incrementa el contador de acciones de ayuda para este territorio.
     */
    public void logReliefAction() {
        this.registeredReliefCount++;
        System.out.println("Ayuda registrada en '" + zone
                + "'. Total ayudas: " + registeredReliefCount);
    }

    // ── Getters / Setters ───────────────────────────────────────────────────

    public int       getId()                       { return id; }
    public String    getZone()                     { return zone; }
    public void      setZone(String zone)          { this.zone = zone; }
    public RiskLevel getRiskLevel()                { return riskLevel; }
    public void      setRiskLevel(RiskLevel level) { this.riskLevel = level; }
    public int       getRegisteredReliefCount()    { return registeredReliefCount; }

    @Override
    public String toString() {
        return "TerritoryRecord{id=" + id + ", zone='" + zone
                + "', riskLevel=" + riskLevel
                + ", reliefCount=" + registeredReliefCount + "}";
    }
}
