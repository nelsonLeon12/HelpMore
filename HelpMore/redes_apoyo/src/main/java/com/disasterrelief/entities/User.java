package com.disasterrelief.entities;

import com.disasterrelief.enums.UserType;

/**
 * Clase base abstracta que representa cualquier usuario del sistema.
 * Tipos concretos: PermanentUser y AnonymousUser.
 */
public abstract class User {

    private static int idCounter = 1;

    private final int id;
    private String name;
    private final UserType type;

    protected User(String name, UserType type) {
        this.id   = idCounter++;
        this.name = name;
        this.type = type;
    }

    // ── Metodos abstractos (diagrama UML) ───────────────────────────────────

    public abstract void register();

    public abstract void login();

    public abstract void deleteAccount();

    /**
     * Realiza una donacion al sistema.
     * @param amount monto en pesos colombianos
     */
    public abstract void donate(double amount);

    /**
     * Indica disponibilidad para desplazarse a una zona de emergencia.
     * @param zone nombre del territorio afectado
     */
    public abstract void goToZone(String zone);

    // ── Getters / Setters ───────────────────────────────────────────────────

    public int getId()           { return id; }
    public String getName()      { return name; }
    public void setName(String name) { this.name = name; }
    public UserType getType()    { return type; }

    @Override
    public String toString() {
        return "User{id=" + id + ", name='" + name + "', type=" + type + "}";
    }
}
