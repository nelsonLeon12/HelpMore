package com.disasterrelief.entities;

import java.util.List;

/**
 * Componente de linea territorial propiedad de Admin (composicion).
 * Emite senales de emergencia y notifica helpers disponibles por zona.
 */
public class HotlineManager {

    private String contactNumber;
    private String assignedZone;

    public HotlineManager() {
    }

    public HotlineManager(String contactNumber, String assignedZone) {
        this.contactNumber = contactNumber;
        this.assignedZone  = assignedZone;
    }

    // ── Metodos UML ─────────────────────────────────────────────────────────

    /**
     * Emite una senal general de ayuda para la zona asignada.
     */
    public void broadcastHelpSignal() {
        System.out.println("¡ALERTA ACTIVADA! Zona: " + assignedZone
                + " | Contacto: " + contactNumber);
    }

    /**
     * Notifica a todos los helpers disponibles en la zona indicada.
     * @param zone    territorio afectado
     * @param helpers lista de usuarios permanentes (helpers) a notificar
     */
    public void notifyHelpers(String zone, List<PermanentUser> helpers) {
        if (helpers == null || helpers.isEmpty()) {
            System.out.println("No hay helpers disponibles en la zona: " + zone);
            return;
        }
        System.out.println("Notificando " + helpers.size() + " helper(s) en la zona: " + zone);
        for (PermanentUser helper : helpers) {
            System.out.println("  → Alerta enviada a: " + helper.getName()
                    + " (" + helper.getEmail() + ")");
        }
    }

    // ── Getters / Setters ───────────────────────────────────────────────────

    public String getContactNumber()               { return contactNumber; }
    public void   setContactNumber(String number)  { this.contactNumber = number; }
    public String getAssignedZone()                { return assignedZone; }
    public void   setAssignedZone(String zone)     { this.assignedZone = zone; }

    public boolean autoValidation(){
        return contactNumber!=null&&!contactNumber.isEmpty()&&assignedZone!=null&&!assignedZone.isEmpty();
    }


    @Override
    public String toString() {
        return "HotlineManager{contactNumber='" + contactNumber
                + "', assignedZone='" + assignedZone + "'}";
    }
}
