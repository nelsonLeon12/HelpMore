package com.disasterrelief.entities;

import com.disasterrelief.enums.RiskLevel;

import java.util.ArrayList;
import java.util.List;

public class Database {

    private static Database instance;

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    private final TerritoryRecord territoryRecord;
    private final CaseCatalog     caseCatalog;
    private final FundRecord      fundRecord;
    private final HelperRanking   helperRanking;

    private final List<User>   users;
    private final List<Admin>  admins;
    private final List<String> alertasActivas;  // ← NUEVO

    private Database() {
        this.territoryRecord = new TerritoryRecord("Sin definir", RiskLevel.LOW);
        this.caseCatalog     = new CaseCatalog("General", true, "General emergency case");
        this.fundRecord      = new FundRecord();
        this.helperRanking   = new HelperRanking();
        this.users           = new ArrayList<>();
        this.admins          = new ArrayList<>();
        this.alertasActivas  = new ArrayList<>();  // ← NUEVO
    }

    // ── User operations ──────────────────────────────────────────────────────

    public void storeUser(User user) {
        users.add(user);
        System.out.println("Usuario almacenado: " + user);
    }

    public void removeUser(int id) {
        boolean removed = users.removeIf(u -> u.getId() == id);
        if (removed) {
            System.out.println("Usuario con ID " + id + " eliminado.");
        } else {
            System.out.println("Usuario con ID " + id + " no encontrado.");
        }
    }

    public User findUserById(int id) {
        return users.stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .orElse(null);
    }

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

    public List<TerritoryRecord> getAllTerritories() {
        return List.of(territoryRecord);
    }

    // ── Ranking operations ───────────────────────────────────────────────────

    public void printRanking(String type) {
        if ("DONATION".equalsIgnoreCase(type)) {
            helperRanking.getDonationRanking()
                    .forEach(e -> System.out.println("  " + e.getKey() + " → " + e.getValue() + " pts"));
        } else if ("RELIEF".equalsIgnoreCase(type)) {
            helperRanking.getReliefRanking()
                    .forEach(e -> System.out.println("  " + e.getKey() + " → " + e.getValue() + " pts"));
        } else {
            System.out.println("Tipo de ranking invalido. Use 'DONATION' o 'RELIEF'.");
        }
    }

    // ── Alert operations ─────────────────────────────────────────────────────

    public void logAlert(String zone, String timestamp) {
        String alerta = "ZONA: " + zone + " | " + timestamp;
        alertasActivas.add(alerta);
        System.out.println("Alerta registrada → " + alerta);
    }

    public void desactivarAlerta(int index) {
        if (index >= 0 && index < alertasActivas.size()) {
            String alerta = alertasActivas.remove(index);
            System.out.println("Alerta desactivada: " + alerta);
        }
    }

    public List<String> getAlertasActivas() {
        return alertasActivas;
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

    // ── Accessors ────────────────────────────────────────────────────────────

    public TerritoryRecord getTerritoryRecord() { return territoryRecord; }
    public CaseCatalog     getCaseCatalog()     { return caseCatalog; }
    public FundRecord      getFundRecord()      { return fundRecord; }
    public HelperRanking   getHelperRanking()   { return helperRanking; }
    public List<User>      getUsers()           { return users; }
    public List<Admin>     getAdmins()          { return admins; }
}