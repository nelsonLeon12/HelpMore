package com.disasterrelief.entities;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Gestiona dos rankings independientes para helpers:
 *   - donationRanking : ordenado por puntos de donacion
 *   - reliefRanking   : ordenado por puntos de ayuda fisica
 *
 * Las donaciones anonimas NO afectan el ranking nominal.
 */
public class HelperRanking {

    // Map: userName → donation points
    private final Map<String, Integer> donationRanking;

    // Map: userName → physical relief points
    private final Map<String, Integer> reliefRanking;

    public HelperRanking() {
        this.donationRanking = new HashMap<>();
        this.reliefRanking   = new HashMap<>();
    }

    // ── Metodos UML ─────────────────────────────────────────────────────────

    /**
     * Sincroniza los puntos actuales de un usuario permanente en ambos rankings.
     * @param user helper a actualizar
     */
    public void updateRanking(PermanentUser user) {
        donationRanking.put(user.getName(), user.getDonationPoints());
        reliefRanking.put(user.getName(),   user.getReliefPoints());
        System.out.println("Ranking actualizado para: " + user.getName());
    }

    /**
     * Retorna el ranking de donaciones ordenado de mayor a menor por puntos.
     */
    public List<Map.Entry<String, Integer>> getDonationRanking() {
        return donationRanking.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toList());
    }

    /**
     * Retorna el ranking de ayuda ordenado de mayor a menor por puntos.
     */
    public List<Map.Entry<String, Integer>> getReliefRanking() {
        return reliefRanking.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toList());
    }

    /**
     * Imprime ambos rankings en consola.
     */
    public void printRankings() {
        System.out.println("\n╔════════════════════════════════╗");
        System.out.println("║       RANKING DE HELPERS       ║");
        System.out.println("╠════════════════════════════════╣");

        printCategory("🏆 Por Donación", getDonationRanking());
        printCategory("🤝 Por Ayuda Física", getReliefRanking());

        System.out.println("╚════════════════════════════════╝\n");
    }

    private void printCategory(String label, List<Map.Entry<String, Integer>> entries) {
        System.out.println("  " + label + ":");
        if (entries.isEmpty()) {
            System.out.println("     (sin datos)");
        } else {
            for (int i = 0; i < entries.size(); i++) {
                Map.Entry<String, Integer> entry = entries.get(i);
                System.out.printf("     %d. %-20s %d pts%n",
                        i + 1, entry.getKey(), entry.getValue());
            }
        }
    }

    // ── Getters ─────────────────────────────────────────────────────────────

    public Map<String, Integer> getDonationRankingMap() { return donationRanking; }
    public Map<String, Integer> getReliefRankingMap()   { return reliefRanking; }
}
