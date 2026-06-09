package com.disasterrelief.service.interfaces;

import com.disasterrelief.entities.Admin;
import com.disasterrelief.entities.AnonymousUser;
import com.disasterrelief.entities.PermanentUser;
import com.disasterrelief.entities.User;
import com.disasterrelief.exceptions.*;

/**
 * Contrato de validaciones del dominio.
 * Cada método lanza la excepción específica del caso de error.
 */
public interface ValidationsServiceIMPL {

    // ── Admin ────────────────────────────────────────────────────────────────

    /**
     * Valida que un Admin tenga nombre, ID positivo, zona y hotline configurada.
     *
     * @throws IllegalArgumentException si nombre o zona son nulos/vacíos, o ID <= 0
     * @throws InvalidHotlineException  si la hotline no supera su autovalidación
     */
    void validateAdmin(Admin admin) throws IllegalArgumentException, InvalidHotlineException;

    // ── User base ────────────────────────────────────────────────────────────

    /**
     * Valida campos básicos comunes a todo User (nombre no vacío).
     *
     * @throws IllegalArgumentException si el nombre es nulo o vacío
     */
    void validateUser(User user) throws IllegalArgumentException;

    // ── PermanentUser ────────────────────────────────────────────────────────

    /**
     * Valida los datos de registro de un usuario permanente:
     * nombre, email con formato básico y contraseña con longitud mínima.
     *
     * @throws IllegalArgumentException si algún campo no cumple las reglas
     */
    void validatePermanentUser(PermanentUser user) throws IllegalArgumentException;

    /**
     * Valida las credenciales de login: email no vacío y password no vacío.
     *
     * @throws IllegalArgumentException   si email o password están vacíos
     * @throws InvalidCredentialsException si la contraseña no coincide
     */
    void validateCredentials(PermanentUser user, String rawPassword)
            throws IllegalArgumentException, InvalidCredentialsException;

    // ── AnonymousUser ────────────────────────────────────────────────────────

    /**
     * Verifica que el AnonymousUser no sea nulo (validación mínima
     * para operaciones como donación anónima).
     *
     * @throws IllegalArgumentException si anon es null
     */
    void validateAnonymousUser(AnonymousUser anon) throws IllegalArgumentException;

    // ── Donación ────────────────────────────────────────────────────────────

    /**
     * Valida que el monto de donación sea estrictamente mayor a cero.
     *
     * @throws InvalidDonationAmountException si amount <= 0
     */
    void validateDonationAmount(double amount) throws InvalidDonationAmountException;

    // ── Zona ────────────────────────────────────────────────────────────────

    /**
     * Valida que la zona de emergencia no sea nula ni vacía.
     *
     * @throws InvalidZoneException si zone es null o blank
     */
    void validateZone(String zone) throws InvalidZoneException;
}
