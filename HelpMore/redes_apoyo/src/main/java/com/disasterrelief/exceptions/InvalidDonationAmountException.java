package com.disasterrelief.exceptions;

/**
 * Se lanza cuando el monto de una donación es inválido (cero o negativo).
 */
public class InvalidDonationAmountException extends Exception {

    public InvalidDonationAmountException(double amount) {
        super("El monto de donación '$" + amount + "' es inválido. Debe ser mayor a cero.");
    }
}
