package com.disasterrelief.repository;

import com.disasterrelief.entities.Database;
import com.disasterrelief.entities.HelperRanking;
import com.disasterrelief.entities.PermanentUser;

import java.util.List;
import java.util.Map;

/**
 * Repositorio de acceso a datos para el ranking de helpers.
 */
public class RankingRepository {

    private final Database db = Database.getInstance();

    /**
     * Sincroniza los puntos actuales de un usuario permanente en el ranking.
     */
    public void updateRanking(PermanentUser user) {
        db.getHelperRanking().updateRanking(user);
    }

    /**
     * Retorna el ranking de donacion ordenado de mayor a menor por puntos.
     */
    public List<Map.Entry<String, Integer>> getDonationRanking() {
        return db.getHelperRanking().getDonationRanking();
    }

    /**
     * Retorna el ranking de ayuda ordenado de mayor a menor por puntos.
     */
    public List<Map.Entry<String, Integer>> getReliefRanking() {
        return db.getHelperRanking().getReliefRanking();
    }

    /**
     * Imprime ambos rankings en consola.
     */
    public void printRankings() {
        db.getHelperRanking().printRankings();
    }

    public HelperRanking getRanking() {
        return db.getHelperRanking();
    }
}
