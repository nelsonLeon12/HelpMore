package com.disasterrelief.repository;

import com.disasterrelief.entities.Database;
import com.disasterrelief.entities.FundRecord;

import java.util.List;

/**
 * Repositorio de acceso a datos para fondos de donacion.
 */
public class FundRepository {

    private final Database db = Database.getInstance();

    /**
     * Registra una nueva transaccion de donacion.
     * @param amount    monto donado
     * @param donorType "PERMANENT" or "ANONYMOUS"
     */
    public void logDonation(double amount, String donorType) {
        db.getFundRecord().logTransaction(amount, donorType);
    }

    /**
     * Retorna el saldo acumulado actual del fondo.
     */
    public double getTotalBalance() {
        return db.getFundRecord().getTotalAmount();
    }

    /**
     * Retorna la lista completa de entradas del historial de transacciones.
     */
    public List<String> getHistory() {
        return db.getFundRecord().getDonationHistory();
    }

    /**
     * Imprime el historial completo de donaciones en consola.
     */
    public void printHistory() {
        db.getFundRecord().printHistory();
    }

    public FundRecord getRecord() {
        return db.getFundRecord();
    }
}
