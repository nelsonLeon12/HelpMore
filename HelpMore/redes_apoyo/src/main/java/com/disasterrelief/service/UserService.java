package com.disasterrelief.service;

import com.disasterrelief.entities.AnonymousUser;
import com.disasterrelief.entities.PermanentUser;
import com.disasterrelief.repository.FundRepository;
import com.disasterrelief.repository.RankingRepository;
import com.disasterrelief.repository.TerritoryRepository;
import com.disasterrelief.repository.UserRepository;

import java.util.Optional;

/**
 * Capa de servicio para la logica de negocio relacionada con usuarios.
 * Orquesta repositorios y aplica reglas del dominio.
 */
public class UserService {

    private final UserRepository      userRepo      = new UserRepository();
    private final FundRepository      fundRepo      = new FundRepository();
    private final RankingRepository   rankingRepo   = new RankingRepository();
    private final TerritoryRepository territoryRepo = new TerritoryRepository();

    // ── Permanent user operations ─────────────────────────────────────────────

    /**
     * Registra un nuevo usuario permanente.
     * Valida que el email no este en uso.
     *
     * @return el usuario creado, o null si el email ya existe
     */
    public PermanentUser registerUser(String name, String email, String password) {
        if (userRepo.existsByEmail(email)) {
            System.out.println("Error: ya existe un usuario con el email " + email);
            return null;
        }
        PermanentUser newUser = new PermanentUser(name, email, password);
        newUser.register();
        userRepo.save(newUser);
        return newUser;
    }

    /**
     * Autentica un usuario permanente por email y contrasena.
     *
     * @return true si las credenciales son validas, false en caso contrario
     */
    public boolean login(String email, String password) {
        Optional<PermanentUser> opt = userRepo.findByEmail(email);
        if (opt.isEmpty()) {
            System.out.println("Usuario no encontrado: " + email);
            return false;
        }
        PermanentUser user = opt.get();
        if (!user.getPassword().equals(password)) {
            System.out.println("Contraseña incorrecta para: " + email);
            return false;
        }
        user.login();
        return true;
    }

    /**
     * Elimina la cuenta de un usuario permanente por ID.
     */
    public void deleteAccount(int userId) {
        userRepo.findActivePermanentUsers().stream()
                .filter(u -> u.getId() == userId)
                .findFirst()
                .ifPresentOrElse(u -> {
                    u.deleteAccount();
                    userRepo.deleteById(userId);
                }, () -> System.out.println("Usuario con ID " + userId + " no encontrado."));
    }

    /**
     * Procesa una donacion de un usuario permanente:
     * actualiza sus puntos, registra la transaccion del fondo y sincroniza el ranking.
     *
     * @param userId usuario que realiza la donacion
     * @param amount monto de la donacion
     */
    public void donate(int userId, double amount) {
        userRepo.findActivePermanentUsers().stream()
                .filter(u -> u.getId() == userId)
                .findFirst()
                .ifPresentOrElse(u -> {
                    u.donate(amount);
                    fundRepo.logDonation(amount, "PERMANENT");
                    rankingRepo.updateRanking(u);
                }, () -> System.out.println("Usuario con ID " + userId + " no encontrado."));
    }

    /**
     * Registra que un helper se desplazo a una zona de emergencia.
     * Actualiza sus puntos de ayuda, registra la accion territorial y sincroniza el ranking.
     *
     * @param userId usuario que va a la zona
     * @param zone   territorio afectado
     */
    public void goToZone(int userId, String zone) {
        userRepo.findActivePermanentUsers().stream()
                .filter(u -> u.getId() == userId)
                .findFirst()
                .ifPresentOrElse(u -> {
                    u.goToZone(zone);
                    territoryRepo.logReliefAction();
                    rankingRepo.updateRanking(u);
                }, () -> System.out.println("Usuario con ID " + userId + " no encontrado."));
    }

    // ── Anonymous user operations ─────────────────────────────────────────────

    /**
     * Procesa una donacion anonima.
     * NO afecta el ranking nominal.
     *
     * @param amount monto de la donacion
     */
    public void donateAnonymously(double amount) {
        AnonymousUser anon = new AnonymousUser();
        anon.donate(amount);
        fundRepo.logDonation(amount, "ANONYMOUS");
        System.out.println("Donación anónima procesada. No afecta el ranking.");
    }

    // ── Accesores ────────────────────────────────────────────────────────────

    public UserRepository getRepository() { return userRepo; }
}
