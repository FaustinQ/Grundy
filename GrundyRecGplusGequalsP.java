import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * `GrundyRecGplusGequalsP` class for the Grundy Game implementation.
 * This program contains methods to test `jouerGagnant()` and `testEstGagnanteEfficacite()`.
 * Version 4 enhances efficiency using a type table.
 *
 * @author F. Quintane and B. Toumelin
 */
class GrundyRecGplusGequalsP {

    // Type table
    static final int[] type = {0, 0, 0, 1, 0, 2, 1, 0, 2, 1, 0, 2, 1, 3, 2, 1, 3, 2, 4, 3, 0, 4, 3, 0, 4, 3, 0, 4, 1, 2, 3, 1, 2, 4, 1, 2, 4, 1, 2, 4, 1, 5, 4, 1, 5, 4, 1, 5, 4, 1, 0};

    long cpt;
    ArrayList<Integer> plateau = new ArrayList<>();
    ArrayList<ArrayList<Integer>> posPerdantes = new ArrayList<>();
    ArrayList<ArrayList<Integer>> posGagnantes = new ArrayList<>();

    /**
     * Main method of the program.
     */
    void principal() {
        testEstGagnanteEfficacite();
    }

    /**
     * Determines if the current configuration is losing.
     *
     * @param jeu the current game board
     * @return true if the configuration is losing, false otherwise
     */
    boolean estPerdante(ArrayList<Integer> jeu) {
        if (jeu == null) {
            System.err.println("estPerdante(): 'jeu' parameter is null");
            return true;
        }

        // Create a normalized version of the game board
        ArrayList<Integer> copieJeu = new ArrayList<>(jeu);
        Collections.sort(copieJeu);
        copieJeu.removeIf(n -> n == 1 || n == 2);

        // Check for known losing positions
        if (posPerdantes.contains(copieJeu)) {
            return true;
        }

        // If no further actions are possible
        if (!estPossible(jeu)) {
            posPerdantes.add(new ArrayList<>(copieJeu));
            return true;
        }

        // Use the type table to optimize checks if possible
        if (copieJeu.stream().allMatch(tas -> tas < type.length)) {
            int premierType = type[copieJeu.get(0)];
            for (int tas : copieJeu) {
                cpt++; // Count the checks
                if (type[tas] != premierType) {
                    return false; // At least one pile of a different type exists
                }
            }

            // If all piles are of the same type, it's a losing position
            posPerdantes.add(new ArrayList<>(copieJeu));
            return true;
        }

        // If types cannot be used, proceed with standard processing
        ArrayList<Integer> essai = new ArrayList<>();
        int ligne = premier(jeu, essai);
        while (ligne != -1) {
            cpt++; // Count each attempt
            if (!estPerdante(essai)) {
                return false;
            }
            ligne = suivant(jeu, essai, ligne);
        }

        posPerdantes.add(new ArrayList<>(copieJeu));
        return true;
    }

    /**
     * Determines if the current configuration is winning.
     *
     * @param jeu the current game board
     * @return true if the configuration is winning, false otherwise
     */
    boolean estGagnante(ArrayList<Integer> jeu) {
        return !estPerdante(jeu);
    }

    /**
     * Plays the winning move if it exists.
     *
     * @param jeu the current game board
     * @return true if a winning move exists, false otherwise
     */
    boolean jouerGagnant(ArrayList<Integer> jeu) {
        boolean gagnant = false;

        if (jeu == null) {
            System.err.println("jouerGagnant(): 'jeu' parameter is null");
        } else {
            ArrayList<Integer> essai = new ArrayList<>();
            int ligne = premier(jeu, essai);

            while (ligne != -1 && !gagnant) {
                cpt++; // Count the attempts
                if (estPerdante(essai)) {
                    jeu.clear();
                    gagnant = true;
                    jeu.addAll(essai);
                } else {
                    ligne = suivant(jeu, essai, ligne);
                }
            }
        }

        return gagnant;
    }

    /**
     * Checks if it is possible to split any pile on the game board.
     *
     * @param jeu the game board
     * @return true if at least one pile with 3 or more matches exists, false otherwise
     */
    boolean estPossible(ArrayList<Integer> jeu) {
        for (int tas : jeu) {
            if (tas > 2) {
                return true;
            }
        }
        return false;
    }

    /**
     * Finds the first possible split of a pile and generates the resulting game board.
     *
     * @param jeu      the current game board
     * @param jeuEssai the resulting game board after splitting
     * @return the index of the pile split, or -1 if no split is possible
     */
    int premier(ArrayList<Integer> jeu, ArrayList<Integer> jeuEssai) {
        if (jeu == null || jeuEssai == null) {
            System.err.println("premier(): Null parameter");
            return -1;
        }

        jeuEssai.clear();
        jeuEssai.addAll(jeu);

        for (int i = 0; i < jeu.size(); i++) {
            if (jeu.get(i) > 2) {
                enlever(jeuEssai, i, 1);
                return i;
            }
        }

        return -1;
    }

    /**
     * Finds the next possible split of a pile and generates the resulting game board.
     *
     * @param jeu      the current game board
     * @param jeuEssai the resulting game board after splitting
     * @param ligne    the index of the pile last split
     * @return the index of the pile split, or -1 if no further splits are possible
     */
    int suivant(ArrayList<Integer> jeu, ArrayList<Integer> jeuEssai, int ligne) {
        if (jeu == null || jeuEssai == null || ligne >= jeu.size()) {
            System.err.println("suivant(): Invalid parameter");
            return -1;
        }

        int nbAllumettesLigne = jeuEssai.get(ligne);
        int nbAllumettesDernierTas = jeuEssai.get(jeuEssai.size() - 1);

        if ((nbAllumettesLigne - nbAllumettesDernierTas) > 2) {
            jeuEssai.set(ligne, nbAllumettesLigne - 1);
            jeuEssai.set(jeuEssai.size() - 1, nbAllumettesDernierTas + 1);
            return ligne;
        }

        for (int i = ligne + 1; i < jeu.size(); i++) {
            if (jeu.get(i) > 2) {
                jeuEssai.clear();
                jeuEssai.addAll(jeu);
                enlever(jeuEssai, i, 1);
                return i;
            }
        }

        return -1;
    }

    /**
     * Splits a pile into two piles.
     *
     * @param jeu   the game board
     * @param ligne the index of the pile to split
     * @param nb    the number of matches removed from the pile
     */
    void enlever(ArrayList<Integer> jeu, int ligne, int nb) {
        if (jeu == null || ligne >= jeu.size() || nb >= jeu.get(ligne) || nb <= 0 || 2 * nb == jeu.get(ligne)) {
            System.err.println("enlever(): Invalid parameter");
            return;
        }

        jeu.add(nb);
        jeu.set(ligne, jeu.get(ligne) - nb);
    }

    /**
     * Tests the efficiency of the `estGagnante` method.
     */
    void testEstGagnanteEfficacite() {
        int n = 3;
        for (int i = 1; i <= 50000; i++) {
            posPerdantes.clear();
            posGagnantes.clear();
            cpt = 0;

            long t1 = System.nanoTime();
            estGagnante(new ArrayList<>(Arrays.asList(n)));
            long t2 = System.nanoTime();
            long diffT = t2 - t1;

            System.out.println("n = " + n + " cpt = " + cpt + " Time = " + diffT + " ns");
            n++;
        }
    }
}
