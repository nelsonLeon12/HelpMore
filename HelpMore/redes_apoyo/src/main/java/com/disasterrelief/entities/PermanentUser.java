package com.disasterrelief.entities;

import com.disasterrelief.enums.UserType;

/**
 * Usuario registrado con credenciales y un sistema doble de puntos.
 * Gana puntos por donacion y puntos de ayuda por asistencia fisica.
 */
public class PermanentUser extends User {

    private String  email;
    private String  password;
    private int     donationPoints;
    private int     reliefPoints;
    private boolean active;

    public PermanentUser(String name, String email, String password) {
        super(name, UserType.PERMANENT);
        this.email          = email;
        this.password       = password;
        this.donationPoints = 0;
        this.reliefPoints   = 0;
        this.active         = false;
    }

    // ── Metodos UML ─────────────────────────────────────────────────────────

    @Override
    public void register() {
        this.active = true;
        System.out.println("Usuario '" + getName() + "' registrado exitosamente. Email: " + email);
    }

    @Override
    public void login() {
        if (!active) {
            System.out.println("Error: la cuenta no está activa.");
            return;
        }
        System.out.println("Sesión iniciada para: " + getName());
    }

    @Override
    public void deleteAccount() {
        this.active = false;
        System.out.println("Cuenta de '" + getName() + "' eliminada correctamente.");
    }

    @Override
    public void donate(double amount) {
        if (amount <= 0) {
            System.out.println("El monto debe ser mayor a cero.");
            return;
        }
        int earned = (int) (amount / 10_000); // 1 punto por cada $10,000
        this.donationPoints += earned;
        System.out.println("Donación de $" + amount + " registrada. "
                + "Puntos ganados: " + earned + " | Total puntos donación: " + donationPoints);
    }

    @Override
    public void goToZone(String zone) {
        this.reliefPoints += 10; // 10 puntos por visita
        System.out.println("'" + getName() + "' acudió a la zona: " + zone
                + " | Puntos de ayuda acumulados: " + reliefPoints);
    }

    /**
     * Muestra la posicion actual del usuario en ambos rankings.
     */
    public void viewRanking() {
        System.out.println("Ranking de '" + getName() + "':");
        System.out.println("  Puntos donación : " + donationPoints);
        System.out.println("  Puntos ayuda    : " + reliefPoints);
        System.out.println("  Total           : " + getTotalPoints());
    }

    // ── Getters / Setters ───────────────────────────────────────────────────

    public String  getEmail()           { return email; }
    public void    setEmail(String e)   { this.email = e; }
    public String  getPassword()        { return password; }
    public void    setPassword(String p){ this.password = p; }
    public int     getDonationPoints()  { return donationPoints; }
    public void    setDonationPoints(int p) { this.donationPoints = p; }
    public int     getReliefPoints()    { return reliefPoints; }
    public void    setReliefPoints(int p)   { this.reliefPoints = p; }
    public boolean isActive()           { return active; }
    public int     getTotalPoints()     { return donationPoints + reliefPoints; }

    @Override
    public String toString() {
        return "PermanentUser{id=" + getId() + ", name='" + getName()
                + "', email='" + email + "', donationPoints=" + donationPoints
                + ", reliefPoints=" + reliefPoints + ", active=" + active + "}";
    }
}
