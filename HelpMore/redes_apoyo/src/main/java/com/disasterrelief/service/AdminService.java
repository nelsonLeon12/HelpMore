package com.disasterrelief.service;

import com.disasterrelief.entities.Admin;
import com.disasterrelief.entities.Database;
import com.disasterrelief.entities.PermanentUser;
import com.disasterrelief.repository.AdminRepository;
import com.disasterrelief.repository.FundRepository;
import com.disasterrelief.service.interfaces.ValidationsServiceIMPL;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Capa de servicio para la logica de negocio del administrador.
 * Implementa los escenarios del diagrama de secuencia del documento del proyecto.
 */
public class AdminService  {

    private final AdminRepository adminRepo = new AdminRepository();
    private final FundRepository  fundRepo  = new FundRepository();
    private final Database        db        = Database.getInstance();

    //Metodos de  validacion
    ValidationsServiceIMPL impl = new Validations();


    /**
     * Crea, verifica y persiste un nuevo administrador territorial.
     */
    public Admin createAdmin(String name, String zone, String hotlineNumber)throws Exception {
        try {
            Admin admin = new Admin(name, zone, hotlineNumber);
            impl.validateAdmin(admin); //llama al metodo para verificar
            adminRepo.save(admin);
            System.out.println("ADMIN GUARDADO CORRECTAMENTE");
            return admin;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }

    }

    /**
     * Diagrama de secuencia - Escenario A:
     * Activa una alerta de emergencia, consulta helpers, los notifica y registra la alerta.
     *
     * @param adminId ID del administrador que activa la alerta
     * @param zone    territorio afectado
     * @param message texto del mensaje de alerta
     */
    public void activateEmergencyAlert(int adminId, String zone, String message) {
        Optional<Admin> optAdmin = adminRepo.findById(adminId);
        if (optAdmin.isEmpty()) {
            System.out.println("Administrador con ID " + adminId + " no encontrado.");
            return;
        }
        Admin admin = optAdmin.get();

        // Paso 1: Enviar mensaje por la linea de atencion
        admin.useHotline(message);

        // Paso 2: Consultar helpers disponibles en la zona
        List<PermanentUser> helpers = db.findHelpersByZone(zone);

        // Paso 3: Activar la alerta y notificar helpers
        admin.activateAlert(zone, helpers);

        // Paso 4: Registrar la alerta en Database (ultimo paso del Escenario A)
        db.logAlert(zone, LocalDateTime.now().toString());

        System.out.println("Alerta procesada. Helpers notificados: " + helpers.size());
    }

    /**
     * Muestra el saldo del fondo e imprime el reporte completo.
     * @param adminId ID del administrador solicitante
     */
    public void manageFunds(int adminId) {
        Optional<Admin> optAdmin = adminRepo.findById(adminId);
        if (optAdmin.isEmpty()) {
            System.out.println("Administrador no encontrado.");
            return;
        }
        optAdmin.get().manageFunds();
        fundRepo.printHistory();
    }

    /**
     * Abre el panel de gestion de usuarios para el administrador indicado.
     * @param adminId ID del administrador solicitante
     */
    public void openUserManagement(int adminId) {
        adminRepo.findById(adminId).ifPresentOrElse(
                Admin::manageUsers,
                () -> System.out.println("Administrador no encontrado.")
        );
    }

    // ── Accesores ────────────────────────────────────────────────────────────

    public AdminRepository getRepository() { return adminRepo; }
}
