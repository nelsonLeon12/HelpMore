package com.disasterrelief.exceptions;

/**
 * Se lanza cuando las credenciales de inicio de sesión son incorrectas.
 */
public class InvalidCredentialsException extends Exception {

    public InvalidCredentialsException() {
        super("Contraseña incorrecta. Acceso denegado.");
    }

    public InvalidCredentialsException(String detail) {
        super("Credenciales inválidas: " + detail);
    }
}
