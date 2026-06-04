package com.disasterrelief.entities;

import com.disasterrelief.enums.RiskLevel;

import java.util.ArrayList;
import java.util.List;

/**
 * Almacen central de datos en memoria para el sistema.
 * Compone TerritoryRecord, CaseCatalog, FundRecord y HelperRanking.
 * Actua como fachada para todas las operaciones de acceso a datos.
 *
 * Implementa el patron Singleton para garantizar una unica instancia compartida.
 */
public class Database {

    // ── Singleton ────────────────────────────────────────────────────────────
    private static Database instance;

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    // ── Composition ──────────────────────────────────────────────────────────
    private final TerritoryRecord territoryRecord;
    private final CaseCatalog     caseCatalog;
    private final FundRecord      fundRecord;
    private final HelperRanking   helperRanking;

    // Colecciones en memoria
    private final List<User>  users;
    private final List<Admin> admins;

    private Database() {
        this.territoryRecord = new TerritoryRecord("Sin definir", RiskLevel.LOW);
        this.caseCatalog     = new CaseCatalog("General", true, "General emergency case");
        this.fundRecord      = new FundRecord();
        this.helperRanking   = new HelperRanking();
        this.users           = new ArrayList<>();
        this.admins          = new ArrayList<>();
    }

    // ── User operations ──────────────────────────────────────────────────────

    /**
     * Persiste un usuario en la base de datos.
     * @param user usuario a almacenar
     */
    public void storeUser(User user) {
        users.add(user);
        System.out.println("Usuario almacenado: " + user);
    }

    /**
     * Elimina un usuario por su ID.
     * @param id identificador unico del usuario
     */
    public void removeUser(int id) {
        boolean removed = users.removeIf(u -> u.getId() == id);
        if (removed) {
            System.out.println("Usuario con ID " + id + " eliminado.");
        } else {
            System.out.println("Usuario con ID " + id + " no encontrado.");
        }
    }

    /**
     * Busca un usuario por ID.
     * @param id identificador unico
     * @return el usuario encontrado o null
     */
    public User findUserById(int id) {
        return users.stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .orElse(null);
    }

    /**
     * Retorna todos los usuarios permanentes activos (helpers), sin importar la zona.
     * En un sistema de produccion, PermanentUser tendria un campo de zona preferida.
     * @param zone nombre del territorio (para fines de registro)
     */
    public List<PermanentUser> findHelpersByZone(String zone) {
        List<PermanentUser> helpers = new ArrayList<>();
        for (User u : users) {
            if (u instanceof PermanentUser pu && pu.isActive()) {
                helpers.add(pu);
            }
        }
        System.out.println("Helpers disponibles para la zona '" + zone + "': " + helpers.size());
        return helpers;
    }

    // ── Territory operations ─────────────────────────────────────────────────

    /**
     * Retorna todos los territorios registrados.
     * (Simplificado: retorna el unico registro de territorio; produccion usaria una List.)
     */
    public List<TerritoryRecord> getAllTerritories() {
        return List.of(territoryRecord);
    }

    // ── Ranking operations ───────────────────────────────────────────────────

    /**
     * Imprime el ranking para la categoria indicada.
     * @param type "DONATION" or "RELIEF"
     */
    public void printRanking(String type) {
        if ("DONATION".equalsIgnoreCase(type)) {
            helperRanking.getDonationRanking()
                    .forEach(e -> System.out.println("  " + e.getKey() + " → " + e.getValue() + " pts"));
        } else if ("RELIEF".equalsIgnoreCase(type)) {
            helperRanking.getReliefRanking()
                    .forEach(e -> System.out.println("  " + e.getKey() + " → " + e.getValue() + " pts"));
        } else {
            System.out.println("Tipo de ranking inválido. Use 'DONATION' o 'RELIEF'.");
        }
    }

    // ── Alert logging ────────────────────────────────────────────────────────

    /**
     * Registra una alerta activada con zona y marca de tiempo.
     * @param zone      territorio afectado
     * @param timestamp marca de tiempo en formato ISO-8601
     */
    public void logAlert(String zone, String timestamp) {
        System.out.println("Alerta registrada → Zona: " + zone + " | Timestamp: " + timestamp);
    }

    // ── Admin operations ─────────────────────────────────────────────────────

    public void storeAdmin(Admin admin) {
        admins.add(admin);
        System.out.println("Administrador almacenado: " + admin);
    }

    public Admin findAdminById(int id) {
        return admins.stream()
                .filter(a -> a.getId() == id)
                .findFirst()
                .orElse(null);
    }

    // ── Composed-record accessors ────────────────────────────────────────────

    public TerritoryRecord getTerritoryRecord() { return territoryRecord; }
    public CaseCatalog     getCaseCatalog()     { return caseCatalog; }
    public FundRecord      getFundRecord()      { return fundRecord; }
    public HelperRanking   getHelperRanking()   { return helperRanking; }
    public List<User>      getUsers()           { return users; }
    public List<Admin>     getAdmins()          { return admins; }
}
