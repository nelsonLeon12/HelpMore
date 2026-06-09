package com.disasterrelief.exceptions;

/**
 * Se lanza cuando se intenta registrar un email que ya existe en el sistema.
 */
public class DuplicateEmailException extends Exception {

    public DuplicateEmailException(String email) {
        super("Ya existe un usuario registrado con el email '" + email + "'.");
    }
}
