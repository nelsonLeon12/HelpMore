package com.disasterrelief.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Almacena el historial completo de donaciones y el monto total acumulado.
 * Pertenece a la capa Database (composicion dentro de Database).
 */
public class FundRecord {

    private double       totalAmount;
    private List<String> donationHistory;

    public FundRecord() {
        this.totalAmount     = 0.0;
        this.donationHistory = new ArrayList<>();
    }

    /**
     * Registra una transaccion de donacion.
     * @param amount    monto donado
     * @param donorType "PERMANENT" or "ANONYMOUS"
     */
    public void logTransaction(double amount, String donorType) {
        this.totalAmount += amount;
        String entry = LocalDateTime.now()
                + " | Monto: $" + amount
                + " | Donante: " + donorType
                + " | Acumulado: $" + totalAmount;
        donationHistory.add(entry);
        System.out.println("Transacción registrada → " + entry);
    }

    /**
     * Imprime el historial completo de donaciones en consola.
     */
    public void printHistory() {
        System.out.println("\n─── Historial de Donaciones ───");
        if (donationHistory.isEmpty()) {
            System.out.println("  (sin donaciones registradas)");
        } else {
            donationHistory.forEach(d -> System.out.println("  " + d));
        }
        System.out.printf("  TOTAL: $%.2f%n%n", totalAmount);
    }

    // ── Getters ─────────────────────────────────────────────────────────────

    public double       getTotalAmount()     { return totalAmount; }
    public List<String> getDonationHistory() { return donationHistory; }

    @Override
    public String toString() {
        return "FundRecord{totalAmount=" + totalAmount
                + ", transactions=" + donationHistory.size() + "}";
    }
}
