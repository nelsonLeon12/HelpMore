package com.disasterrelief.repository;

import com.disasterrelief.entities.Admin;
import com.disasterrelief.entities.Database;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio de acceso a datos para la entidad Admin.
 */
public class AdminRepository {

    private final Database db = Database.getInstance();

    public void save(Admin admin) {
        db.storeAdmin(admin);
    }

    public Optional<Admin> findById(int id) {
        return Optional.ofNullable(db.findAdminById(id));
    }

    public List<Admin> findAll() {
        return db.getAdmins();
    }

    /**
     * Busca un administrador por la zona que gestiona.
     */
    public Optional<Admin> findByZone(String zone) {
        return db.getAdmins().stream()
                .filter(a -> a.getZone().equalsIgnoreCase(zone))
                .findFirst();
    }
}
