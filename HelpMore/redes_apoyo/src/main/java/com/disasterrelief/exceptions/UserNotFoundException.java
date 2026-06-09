package com.disasterrelief.exceptions;

/**
 * Se lanza cuando no se encuentra un usuario con el ID o email indicado.
 */
public class UserNotFoundException extends Exception {

    public UserNotFoundException(int id) {
        super("Usuario con ID " + id + " no encontrado o no está activo.");
    }

    public UserNotFoundException(String email) {
        super("Usuario con email '" + email + "' no encontrado.");
    }
}
