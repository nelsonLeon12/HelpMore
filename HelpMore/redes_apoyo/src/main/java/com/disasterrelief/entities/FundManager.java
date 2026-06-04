package com.disasterrelief.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Componente de gestion de fondos propiedad de Admin (composicion).
 * Registra todas las donaciones entrantes y genera reportes de saldo.
 */
public class FundManager {

    private double       totalBalance;
    private List<String> transactionHistory;

    public FundManager() {
        this.totalBalance       = 0.0;
        this.transactionHistory = new ArrayList<>();
    }

    // ── Metodos UML ─────────────────────────────────────────────────────────

    /**
     * Registra una donacion entrante.
     * @param amount      monto donado
     * @param donorType   "PERMANENT" or "ANONYMOUS"
     */
    public void receiveDonation(double amount, String donorType) {
        if (amount <= 0) {
            System.out.println("El monto debe ser mayor a cero.");
            return;
        }
        this.totalBalance += amount;
        String entry = LocalDateTime.now()
                + " | +" + amount
                + " | Tipo: " + donorType
                + " | Saldo: " + totalBalance;
        transactionHistory.add(entry);
        System.out.println("Donación recibida: $" + amount
                + " (" + donorType + "). Nuevo saldo: $" + totalBalance);
    }

    /** Sobrecarga de conveniencia para donaciones anonimas. */
    public void receiveDonation(double amount) {
        receiveDonation(amount, "ANONYMOUS");
    }

    /**
     * Retorna el saldo actual del fondo.
     * @return total acumulado
     */
    public double checkBalance() {
        System.out.println("Saldo actual del fondo: $" + totalBalance);
        return totalBalance;
    }

    /**
     * Imprime el historial completo de transacciones en consola.
     */
    public void generateReport() {
        System.out.println("\n========== REPORTE DE FONDOS ==========");
        System.out.printf("Saldo total acumulado: $%.2f%n", totalBalance);
        System.out.println("Transacciones registradas: " + transactionHistory.size());
        System.out.println("---------------------------------------");
        if (transactionHistory.isEmpty()) {
            System.out.println("  (sin transacciones)");
        } else {
            transactionHistory.forEach(t -> System.out.println("  " + t));
        }
        System.out.println("========================================\n");
    }

    // ── Getters ─────────────────────────────────────────────────────────────

    public double       getTotalBalance()        { return totalBalance; }
    public List<String> getTransactionHistory()  { return transactionHistory; }
}
