package com.disasterrelief.service;

import com.disasterrelief.entities.Admin;
import com.disasterrelief.entities.Database;
import com.disasterrelief.entities.PermanentUser;
import com.disasterrelief.exceptions.*;
import com.disasterrelief.repository.AdminRepository;
import com.disasterrelief.repository.FundRepository;
import com.disasterrelief.service.interfaces.ValidationsServiceIMPL;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Capa de servicio para la lógica de negocio del administrador.
 * Implementa los escenarios del diagrama de secuencia del proyecto.
 *
 * Todos los métodos declaran sus excepciones checked para que el
 * controlador JavaFX pueda capturar cada caso con mensajes específicos.
 */
public class AdminService {

    private final AdminRepository adminRepo = new AdminRepository();
    private final FundRepository  fundRepo  = new FundRepository();
    private final Database        db        = Database.getInstance();

    // Validador compartido
    private final ValidationsServiceIMPL validations = new Validations();

    // ── Crear administrador ──────────────────────────────────────────────────

    /**
     * Crea, valida y persiste un nuevo administrador territorial.
     *
     * @param name          nombre del administrador (no vacío)
     * @param zone          zona territorial asignada (no vacía)
     * @param hotlineNumber número de contacto de la hotline (no vacío)
     * @return el Admin creado y persistido
     * @throws IllegalArgumentException si nombre, zona o ID son inválidos
     * @throws InvalidHotlineException  si el número de hotline o la zona son vacíos
     */
    public Admin createAdmin(String name, String zone, String hotlineNumber)
            throws IllegalArgumentException, InvalidHotlineException {

        Admin admin = new Admin(name, zone, hotlineNumber);
        validations.validateAdmin(admin);   // lanza las excepciones específicas si algo falla
        adminRepo.save(admin);
        System.out.println("Admin '" + name + "' guardado correctamente.");
        return admin;
    }

    // ── Activar alerta de emergencia (Escenario A del diagrama de secuencia) ─

    /**
     * Activa una alerta de emergencia:
     * 1. Usa la hotline para emitir el mensaje.
     * 2. Consulta helpers disponibles en la zona.
     * 3. Notifica a los helpers.
     * 4. Registra la alerta en Database.
     *
     * @param adminId ID del administrador que activa la alerta
     * @param zone    territorio afectado (no vacío)
     * @param message texto del mensaje de alerta (no vacío)
     * @throws AdminNotFoundException si no existe un Admin con ese ID
     * @throws InvalidZoneException   si la zona es nula o vacía
     * @throws IllegalArgumentException si el mensaje es nulo o vacío
     */
    public void activateEmergencyAlert(int adminId, String zone, String message)
            throws AdminNotFoundException, InvalidZoneException, IllegalArgumentException {

        // 1) Validaciones previas
        validations.validateZone(zone);
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("El mensaje de alerta no puede ser nulo ni vacío.");
        }

        // 2) Buscar administrador
        Admin admin = adminRepo.findById(adminId)
                .orElseThrow(() -> new AdminNotFoundException(adminId));

        // 3) Enviar mensaje por hotline
        admin.useHotline(message);

        // 4) Consultar y notificar helpers disponibles en la zona
        List<PermanentUser> helpers = db.findHelpersByZone(zone);
        admin.activateAlert(zone, helpers);

        // 5) Registrar la alerta (último paso del diagrama de secuencia)
        db.logAlert(zone, LocalDateTime.now().toString());

        System.out.println("Alerta procesada correctamente. Helpers notificados: " + helpers.size());
    }

    // ── Gestionar fondos ─────────────────────────────────────────────────────

    /**
     * Muestra el saldo del fondo e imprime el reporte completo para un admin.
     *
     * @param adminId ID del administrador solicitante
     * @throws AdminNotFoundException si no existe un Admin con ese ID
     */
    public void manageFunds(int adminId) throws AdminNotFoundException {

        Admin admin = adminRepo.findById(adminId)
                .orElseThrow(() -> new AdminNotFoundException(adminId));

        admin.manageFunds();
        fundRepo.printHistory();
    }

    // ── Gestionar usuarios ───────────────────────────────────────────────────

    /**
     * Abre el panel de gestión de usuarios para el administrador indicado.
     *
     * @param adminId ID del administrador solicitante
     * @throws AdminNotFoundException si no existe un Admin con ese ID
     */
    public void openUserManagement(int adminId) throws AdminNotFoundException {

        Admin admin = adminRepo.findById(adminId)
                .orElseThrow(() -> new AdminNotFoundException(adminId));

        admin.manageUsers();
    }

    // ── Consultas de fondos y ranking ────────────────────────────────────────

    /**
     * Retorna el saldo total acumulado del fondo de donaciones.
     */
    public double getTotalFundBalance() {
        return fundRepo.getTotalBalance();
    }

    /**
     * Retorna el historial de transacciones del fondo.
     */
    public List<String> getFundHistory() {
        return fundRepo.getHistory();
    }

    /**
     * Retorna el ranking de donación ordenado de mayor a menor.
     * (útil para que el Admin lo muestre en la UI)
     */
    public List<Map.Entry<String, Integer>> getDonationRanking() {
        return db.getHelperRanking().getDonationRanking();
    }

    /**
     * Retorna el ranking de ayuda física ordenado de mayor a menor.
     */
    public List<Map.Entry<String, Integer>> getReliefRanking() {
        return db.getHelperRanking().getReliefRanking();
    }

    // ── Accesor ──────────────────────────────────────────────────────────────

    public AdminRepository getRepository() { return adminRepo; }
}
