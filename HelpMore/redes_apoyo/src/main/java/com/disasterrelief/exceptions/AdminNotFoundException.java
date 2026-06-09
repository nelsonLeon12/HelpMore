package com.disasterrelief.exceptions;

/**
 * Se lanza cuando no se encuentra un Admin con el ID o zona indicada.
 */
public class AdminNotFoundException extends Exception {

    public AdminNotFoundException(int id) {
        super("Administrador con ID " + id + " no encontrado.");
    }

    public AdminNotFoundException(String zone) {
        super("No existe un administrador asignado a la zona '" + zone + "'.");
    }
}
