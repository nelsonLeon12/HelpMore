package com.disasterrelief.entities;

import com.disasterrelief.enums.UserType;

/**
 * Usuario invitado sin cuenta. Solo puede hacer donaciones anonimas.
 * No acumula puntos ni aparece en el ranking nominal.
 */
public class AnonymousUser extends User {

    public AnonymousUser() {
        super("Anónimo", UserType.ANONYMOUS);
    }

    // ── Metodos UML ─────────────────────────────────────────────────────────

    @Override
    public void register() {
        System.out.println("Los usuarios anónimos no pueden registrarse desde este flujo.");
    }

    @Override
    public void login() {
        System.out.println("Los usuarios anónimos no requieren inicio de sesión.");
    }

    @Override
    public void deleteAccount() {
        System.out.println("Los usuarios anónimos no tienen cuenta que eliminar.");
    }

    /**
     * Procesa una donacion anonima. No afecta el ranking nominal.
     * @param amount monto a donar
     */
    @Override
    public void donate(double amount) {
        if (amount <= 0) {
            System.out.println("El monto debe ser mayor a cero.");
            return;
        }
        System.out.println("Donación anónima de $" + amount + " recibida. No afecta el ranking nominal.");
    }

    @Override
    public void goToZone(String zone) {
        System.out.println("Acceso denegado: debe registrarse para acudir a zonas de emergencia.");
    }

    @Override
    public String toString() {
        return "AnonymousUser{id=" + getId() + "}";
    }
}
