package com.disasterrelief.entities;

import java.util.List;

/**
 * Administrador territorial del sistema.
 * Gestiona usuarios, activa alertas de emergencia por la linea de atencion
 * y supervisa los fondos de donaciones.
 *
 * Compone (posee) un HotlineManager y un FundManager.
 */
public class Admin {

    private static int idCounter = 1;

    private final int    id;
    private       String name;
    private       String zone;

    // Composicion: Admin posee y controla estos objetos
    private final HotlineManager hotline;
    private final FundManager    fundManager;

    public Admin(String name, String zone, String hotlineNumber) {
        this.id          = idCounter++;
        this.name        = name;
        this.zone        = zone;
        this.hotline     = new HotlineManager(hotlineNumber, zone);
        this.fundManager = new FundManager();
    }

    // ── Metodos UML ─────────────────────────────────────────────────────────

    /**
     * Abre el panel de gestion de usuarios.
     */
    public void manageUsers() {
        System.out.println("Administrador '" + name
                + "' abrió el panel de gestión de usuarios. Zona: " + zone);
    }

    /**
     * Activa una alerta de emergencia para la zona indicada y notifica helpers.
     * @param zone    territorio afectado
     * @param helpers helpers disponibles para notificar
     */
    public void activateAlert(String zone, List<PermanentUser> helpers) {
        System.out.println("Alerta de emergencia activada por '" + name + "' en: " + zone);
        hotline.broadcastHelpSignal();
        hotline.notifyHelpers(zone, helpers);
    }

    /** Activa una alerta sin una lista de helpers precargada. */
    public void activateAlert(String zone) {
        activateAlert(zone, List.of());
    }

    /**
     * Muestra el saldo del fondo y genera un reporte completo.
     */
    public void manageFunds() {
        System.out.println("Administrador '" + name + "' consulta el estado del fondo.");
        fundManager.checkBalance();
        fundManager.generateReport();
    }

    /**
     * Envia un mensaje de emergencia por la linea territorial.
     * @param message texto del mensaje de alerta
     */
    public void useHotline(String message) {
        System.out.println("Hotline activado por '" + name + "': \"" + message + "\"");
        hotline.broadcastHelpSignal();
    }

    // ── Getters / Setters ───────────────────────────────────────────────────

    public int           getId()          { return id; }
    public String        getName()        { return name; }
    public void          setName(String n){ this.name = n; }
    public String        getZone()        { return zone; }
    public void          setZone(String z){ this.zone = z; }
    public HotlineManager getHotline()   { return hotline; }
    public FundManager   getFundManager() { return fundManager; }

    @Override
    public String toString() {
        return "Admin{id=" + id + ", name='" + name + "', zone='" + zone + "'}";
    }
}
