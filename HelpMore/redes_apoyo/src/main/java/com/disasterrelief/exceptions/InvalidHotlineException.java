package com.disasterrelief.exceptions;

/**
 * Se lanza cuando la hotline de un Admin no supera la autovalidación
 * (número de contacto o zona asignada vacíos/nulos).
 */
public class InvalidHotlineException extends Exception {

    public InvalidHotlineException() {
        super("La hotline del administrador no es válida. Verifique número de contacto y zona asignada.");
    }
}
