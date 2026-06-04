package com.disasterrelief.repository;

import com.disasterrelief.entities.Database;
import com.disasterrelief.entities.PermanentUser;
import com.disasterrelief.entities.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Repositorio de acceso a datos para la entidad User.
 * Encapsula todas las operaciones CRUD contra Database.
 */
public class UserRepository {

    private final Database db = Database.getInstance();

    // ── Guardar ──────────────────────────────────────────────────────────────

    public void save(User user) {
        db.storeUser(user);
    }

    // ── Eliminar ─────────────────────────────────────────────────────────────

    public void deleteById(int id) {
        db.removeUser(id);
    }

    // ── Buscar ───────────────────────────────────────────────────────────────

    public Optional<User> findById(int id) {
        return Optional.ofNullable(db.findUserById(id));
    }

    public List<User> findAll() {
        return db.getUsers();
    }

    /**
     * Retorna solo usuarios permanentes activos.
     */
    public List<PermanentUser> findActivePermanentUsers() {
        return db.getUsers().stream()
                .filter(u -> u instanceof PermanentUser)
                .map(u -> (PermanentUser) u)
                .filter(PermanentUser::isActive)
                .collect(Collectors.toList());
    }

    /**
     * Busca un usuario permanente por direccion de email.
     */
    public Optional<PermanentUser> findByEmail(String email) {
        return db.getUsers().stream()
                .filter(u -> u instanceof PermanentUser)
                .map(u -> (PermanentUser) u)
                .filter(pu -> pu.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    /**
     * Retorna helpers disponibles en una zona indicada.
     */
    public List<PermanentUser> findHelpersByZone(String zone) {
        return db.findHelpersByZone(zone);
    }

    /**
     * Verifica si ya existe un usuario con el email indicado.
     */
    public boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }
}
