package com.disasterrelief.exceptions;

/**
 * Se lanza cuando la zona de emergencia indicada es nula, vacía o inválida.
 */
public class InvalidZoneException extends Exception {

    public InvalidZoneException() {
        super("La zona de emergencia no puede ser nula ni vacía.");
    }

    public InvalidZoneException(String zone) {
        super("La zona '" + zone + "' no es válida o no está registrada en el sistema.");
    }
}
