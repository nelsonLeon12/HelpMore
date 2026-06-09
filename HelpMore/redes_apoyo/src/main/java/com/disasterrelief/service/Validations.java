package com.disasterrelief.service;

import com.disasterrelief.entities.Admin;
import com.disasterrelief.entities.AnonymousUser;
import com.disasterrelief.entities.PermanentUser;
import com.disasterrelief.entities.User;
import com.disasterrelief.exceptions.*;
import com.disasterrelief.service.interfaces.ValidationsServiceIMPL;

/**
 * Implementación de las reglas de validación del dominio.
 *
 * NOTAS DE CORRECCIÓN (bugs del código original):
 *   - El patrón  (a == null && a.isEmpty())  nunca detecta nulo
 *     porque si 'a' es null el && nunca llega a isEmpty() → lanza NullPointerException.
 *     La forma correcta es (a == null || a.isBlank()).
 *   - Se reemplazó Exception genérica por excepciones específicas del dominio.
 */
public class Validations implements ValidationsServiceIMPL {

    // ── Admin ────────────────────────────────────────────────────────────────

    @Override
    public void validateAdmin(Admin admin) throws IllegalArgumentException, InvalidHotlineException {

        if (admin == null) {
            throw new IllegalArgumentException("El administrador no puede ser nulo.");
        }
        // Nombre
        if (admin.getName() == null || admin.getName().isBlank()) {
            throw new IllegalArgumentException("Error: el nombre del administrador es inválido (nulo o vacío).");
        }
        // ID generado automáticamente siempre es >= 1, pero se valida por seguridad
        if (admin.getId() <= 0) {
            throw new IllegalArgumentException("Error: el ID del administrador es inválido (<= 0).");
        }
        // Zona
        if (admin.getZone() == null || admin.getZone().isBlank()) {
            throw new IllegalArgumentException("Error: la zona del administrador es inválida (nula o vacía).");
        }
        // Hotline: delega en el propio objeto de composición
        if (!admin.getHotline().autoValidation()) {
            throw new InvalidHotlineException();
        }
    }

    // ── User base ────────────────────────────────────────────────────────────

    @Override
    public void validateUser(User user) throws IllegalArgumentException {

        if (user == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo.");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            throw new IllegalArgumentException("El nombre del usuario no puede ser nulo ni vacío.");
        }
    }

    // ── PermanentUser ────────────────────────────────────────────────────────

    @Override
    public void validatePermanentUser(PermanentUser user) throws IllegalArgumentException {

        // Reutiliza validación base
        validateUser(user);

        // Email: formato mínimo (contiene '@' y al menos un punto después)
        String email = user.getEmail();
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("El email no puede ser nulo ni vacío.");
        }
        if (!email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("El email '" + email + "' no tiene un formato válido.");
        }

        // Contraseña: mínimo 6 caracteres
        String password = user.getPassword();
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("La contraseña no puede ser nula ni vacía.");
        }
        if (password.length() < 6) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres.");
        }
    }

    @Override
    public void validateCredentials(PermanentUser user, String rawPassword)
            throws IllegalArgumentException, InvalidCredentialsException {

        if (user == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo.");
        }
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía.");
        }
        if (!user.getPassword().equals(rawPassword)) {
            throw new InvalidCredentialsException();
        }
    }

    // ── AnonymousUser ────────────────────────────────────────────────────────

    @Override
    public void validateAnonymousUser(AnonymousUser anon) throws IllegalArgumentException {
        if (anon == null) {
            throw new IllegalArgumentException("El usuario anónimo no puede ser nulo.");
        }
        // Los AnonymousUser no tienen campos adicionales que validar;
        // la lógica de restricciones se maneja en sus propios métodos.
    }

    // ── Donación ────────────────────────────────────────────────────────────

    @Override
    public void validateDonationAmount(double amount) throws InvalidDonationAmountException {
        if (amount <= 0) {
            throw new InvalidDonationAmountException(amount);
        }
    }

    // ── Zona ────────────────────────────────────────────────────────────────

    @Override
    public void validateZone(String zone) throws InvalidZoneException {
        if (zone == null || zone.isBlank()) {
            throw new InvalidZoneException();
        }
    }
}
