package com.disasterrelief.service;

import com.disasterrelief.entities.AnonymousUser;
import com.disasterrelief.entities.PermanentUser;
import com.disasterrelief.exceptions.*;
import com.disasterrelief.repository.FundRepository;
import com.disasterrelief.repository.RankingRepository;
import com.disasterrelief.repository.TerritoryRepository;
import com.disasterrelief.repository.UserRepository;
import com.disasterrelief.service.interfaces.ValidationsServiceIMPL;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Capa de servicio para la lógica de negocio relacionada con usuarios.
 * Orquesta repositorios, aplica validaciones y lanza excepciones del dominio.
 *
 * Todos los métodos son checked (declaran throws) para que el controlador
 * JavaFX pueda capturar cada caso con mensajes específicos en la UI.
 */
public class UserService {

    private final UserRepository      userRepo      = new UserRepository();
    private final FundRepository      fundRepo      = new FundRepository();
    private final RankingRepository   rankingRepo   = new RankingRepository();
    private final TerritoryRepository territoryRepo = new TerritoryRepository();

    // Validador compartido
    private final ValidationsServiceIMPL validations = new Validations();

    // ── Registro ─────────────────────────────────────────────────────────────

    /**
     * Registra un nuevo usuario permanente en el sistema.
     *
     * @param name     nombre completo (no vacío)
     * @param email    dirección de correo (formato mínimo con @ y .)
     * @param password contraseña (mínimo 6 caracteres)
     * @return el PermanentUser creado y persistido
     * @throws IllegalArgumentException si algún campo no pasa la validación
     * @throws DuplicateEmailException  si el email ya está registrado
     */
    public PermanentUser registerUser(String name, String email, String password)
            throws IllegalArgumentException, DuplicateEmailException {

        // 1) Verificar duplicado de email ANTES de crear el objeto
        if (userRepo.existsByEmail(email)) {
            throw new DuplicateEmailException(email);
        }

        // 2) Crear y validar el objeto
        PermanentUser newUser = new PermanentUser(name, email, password);
        validations.validatePermanentUser(newUser);   // lanza IllegalArgumentException si algo falla

        // 3) Activar y persistir
        newUser.register();
        userRepo.save(newUser);
        return newUser;
    }

    // ── Login ────────────────────────────────────────────────────────────────

    /**
     * Autentica un usuario permanente por email y contraseña.
     *
     * @param email    email del usuario
     * @param password contraseña en texto plano
     * @return el PermanentUser autenticado
     * @throws IllegalArgumentException   si los campos están vacíos
     * @throws UserNotFoundException      si no existe usuario con ese email
     * @throws InvalidCredentialsException si la contraseña no coincide
     */
    public PermanentUser login(String email, String password)
            throws IllegalArgumentException, UserNotFoundException, InvalidCredentialsException {

        // Validación de campos vacíos
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("El email no puede estar vacío.");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía.");
        }

        // Buscar usuario
        Optional<PermanentUser> opt = userRepo.findByEmail(email);
        if (opt.isEmpty()) {
            throw new UserNotFoundException(email);
        }

        PermanentUser user = opt.get();

        // Validar contraseña
        validations.validateCredentials(user, password);  // lanza InvalidCredentialsException si no coincide

        user.login();
        return user;
    }

    // ── Eliminar cuenta ──────────────────────────────────────────────────────

    /**
     * Elimina la cuenta de un usuario permanente activo por ID.
     *
     * @param userId ID del usuario a eliminar
     * @throws UserNotFoundException si no existe usuario activo con ese ID
     */
    public void deleteAccount(int userId) throws UserNotFoundException {

        PermanentUser target = userRepo.findActivePermanentUsers().stream()
                .filter(u -> u.getId() == userId)
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(userId));

        target.deleteAccount();
        userRepo.deleteById(userId);
    }

    // ── Donación de usuario registrado ───────────────────────────────────────

    /**
     * Procesa una donación de un usuario permanente activo.
     * Actualiza sus puntos, registra la transacción en el fondo y sincroniza el ranking.
     *
     * @param userId ID del usuario donante
     * @param amount monto a donar (> 0)
     * @throws UserNotFoundException          si no existe usuario activo con ese ID
     * @throws InvalidDonationAmountException si el monto es <= 0
     */
    public void donate(int userId, double amount)
            throws UserNotFoundException, InvalidDonationAmountException {

        // 1) Validar monto primero (antes de buscar usuario)
        validations.validateDonationAmount(amount);

        // 2) Buscar usuario activo
        PermanentUser user = userRepo.findActivePermanentUsers().stream()
                .filter(u -> u.getId() == userId)
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(userId));

        // 3) Procesar donación
        user.donate(amount);
        fundRepo.logDonation(amount, "PERMANENT");
        rankingRepo.updateRanking(user);
    }

    // ── Ir a zona de emergencia ──────────────────────────────────────────────

    /**
     * Registra que un helper se desplazó a una zona de emergencia.
     * Actualiza sus puntos de ayuda, registra la acción territorial y sincroniza el ranking.
     *
     * @param userId ID del helper
     * @param zone   nombre del territorio afectado (no vacío)
     * @throws UserNotFoundException si no existe usuario activo con ese ID
     * @throws InvalidZoneException  si la zona es nula o vacía
     */
    public void goToZone(int userId, String zone)
            throws UserNotFoundException, InvalidZoneException {

        // 1) Validar zona primero
        validations.validateZone(zone);

        // 2) Buscar usuario activo
        PermanentUser user = userRepo.findActivePermanentUsers().stream()
                .filter(u -> u.getId() == userId)
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(userId));

        // 3) Registrar desplazamiento
        user.goToZone(zone);
        territoryRepo.logReliefAction();
        rankingRepo.updateRanking(user);
    }

    // ── Donación anónima ────────────────────────────────────────────────────

    /**
     * Procesa una donación anónima. NO afecta el ranking nominal.
     *
     * @param amount monto de la donación (> 0)
     * @throws InvalidDonationAmountException si el monto es <= 0
     */
    public void donateAnonymously(double amount) throws InvalidDonationAmountException {

        // 1) Validar monto
        validations.validateDonationAmount(amount);

        // 2) Crear usuario anónimo, validar y procesar
        AnonymousUser anon = new AnonymousUser();
        validations.validateAnonymousUser(anon);   // validación mínima: no null

        anon.donate(amount);
        fundRepo.logDonation(amount, "ANONYMOUS");
    }

    // ── Consultas ────────────────────────────────────────────────────────────

    /**
     * Retorna la lista de todos los usuarios permanentes activos.
     */
    public List<PermanentUser> getActiveUsers() {
        return userRepo.findActivePermanentUsers();
    }

    /**
     * Retorna el ranking de donación ordenado de mayor a menor.
     */
    public List<Map.Entry<String, Integer>> getDonationRanking() {
        return rankingRepo.getDonationRanking();
    }

    /**
     * Retorna el ranking de ayuda física ordenado de mayor a menor.
     */
    public List<Map.Entry<String, Integer>> getReliefRanking() {
        return rankingRepo.getReliefRanking();
    }

    /**
     * Retorna el saldo total acumulado del fondo.
     */
    public double getTotalFundBalance() {
        return fundRepo.getTotalBalance();
    }

    // ── Accesor ──────────────────────────────────────────────────────────────

    public UserRepository getRepository() { return userRepo; }
}
