import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * GrundyRecPerdEtGagn class for the Grundy Game implementation.
 * This class manages the game logic to determine winning and losing positions for the game.
 * It includes methods to calculate positions using recursive algorithms and store known positions
 * in order to improve efficiency.
 *
 * @author F.Quintane and B.Toumelin
 */
class GrundyRecPerdEtGagn {

    // Global variables accessible across all methods
    long cpt; // Counter for performance measurement
    ArrayList<Integer> plateau = new ArrayList<Integer>();
    ArrayList<ArrayList<Integer>> posPerdantes = new ArrayList<ArrayList<Integer>>(); // Losing positions
    ArrayList<ArrayList<Integer>> posGagnantes = new ArrayList<ArrayList<Integer>>(); // Winning positions

    /**
     * Main program execution method.
     */
    void principal() {
        /**
         * Uncomment the following block to enable user interaction for setting up the game board.
         *
         * int nbrcase = SimpleInput.getInt("Avec combien d'allumettes voulez-vous jouer ? ");
         * while(nbrcase < 3){
         *     nbrcase = SimpleInput.getInt("Veuillez entrer un nombre d'allumettes supérieur à 2");
         * }
         *
         * plateau.add(nbrcase);
         * System.out.println("~~ Le Plateau " + plateau + " ~~");
         *
         * while(estPossible(plateau)){
         *     // Game logic for user and computer moves.
         * }
         */
        testEstGagnanteEfficacite();
    }

    /**
     * Plays the winning move if it exists.
     *
     * @param jeu The current game board.
     * @return true if there is a winning move, false otherwise.
     */
    boolean jouerGagnant(ArrayList<Integer> jeu) {
        boolean gagnant = false;

        if (jeu == null) {
            System.err.println("jouerGagnant(): The parameter jeu is null");
        } else {
            ArrayList<Integer> essai = new ArrayList<>();
            int ligne = premier(jeu, essai);

            while (ligne != -1 && !gagnant) {
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
     * Recursive method to determine if a configuration is losing.
     *
     * @param jeu The current game board.
     * @return true if the configuration is losing, false otherwise.
     */
    boolean estPerdante(ArrayList<Integer> jeu) {
        if (jeu == null) {
            System.err.println("estPerdante(): The parameter jeu is null");
            return true;
        }

        ArrayList<Integer> copieJeu = new ArrayList<>(jeu);
        Collections.sort(copieJeu);
        copieJeu.removeIf(n -> n == 1 || n == 2);

        if (posPerdantes.contains(copieJeu)) {
            return true;
        }

        if (!estPossible(jeu)) {
            return true;
        }

        ArrayList<Integer> essai = new ArrayList<>();
        int ligne = premier(jeu, essai);

        while (ligne != -1) {
            if (!estPerdante(essai)) {
                return false;
            }
            ligne = suivant(jeu, essai, ligne);
            cpt++;
        }

        posPerdantes.add(copieJeu);
        return true;
    }

    /**
     * Determines if a configuration is winning.
     *
     * @param jeu The current game board.
     * @return true if the configuration is winning, false otherwise.
     */
    boolean estGagnante(ArrayList<Integer> jeu) {
        if (jeu == null) {
            System.err.println("estGagnante(): The parameter jeu is null");
            return false;
        }

        if (estConnueGagnantes(jeu)) {
            return true;
        }

        return !estPerdante(jeu);
    }

    /**
     * Tests the efficiency of the estGagnante method.
     */
    void testEstGagnanteEfficacite() {
        int n = 3;
        long t1, t2, diffT;

        for (int i = 1; i <= 500; i++) {
            posPerdantes.clear();
            posGagnantes.clear();
            cpt = 0;

            t1 = System.nanoTime();
            estGagnante(new ArrayList<>(Arrays.asList(n)));
            t2 = System.nanoTime();
            diffT = (t2 - t1);

            System.out.println("n = " + n + " cpt = " + cpt + " Time = " + diffT + " ns");
            n++;
        }
    }

    /**
     * Checks if a position is already known as losing.
     *
     * @param jeu The current game board.
     * @return true if the position is known as losing, false otherwise.
     */
    boolean estConnuePerdante(ArrayList<Integer> jeu) {
        ArrayList<Integer> copieJeuPerdante = new ArrayList<>(jeu);
        Collections.sort(copieJeuPerdante);
        copieJeuPerdante.removeIf(n -> n == 1 || n == 2);
        return posPerdantes.contains(copieJeuPerdante);
    }

    /**
     * Checks if a position is already known as winning.
     *
     * @param jeu The current game board.
     * @return true if the position is known as winning, false otherwise.
     */
    boolean estConnueGagnantes(ArrayList<Integer> jeu) {
        ArrayList<Integer> copieJeuGagnante = new ArrayList<>(jeu);
        Collections.sort(copieJeuGagnante);
        copieJeuGagnante.removeIf(n -> n == 1 || n == 2);
        return posGagnantes.contains(copieJeuGagnante);
    }

    /**
     * test the method jouerGagnant()
     */
    void testJouerGagnant() {
        System.out.println();
        System.out.println("*** testJouerGagnant() ***");

        System.out.println("Test des cas normaux");
        ArrayList<Integer> jeu1 = new ArrayList<Integer>();
        jeu1.add(6);
        ArrayList<Integer> resJeu1 = new ArrayList<Integer>();
        resJeu1.add(4);
        resJeu1.add(2);
		
        testCasJouerGagnant(jeu1, resJeu1, true);
        
    }

    /**
     * test a case of the method jouerGagnant()
	 *
	 * @param jeu gameboard
	 * @param resJeu gameboard after having played win
	 * @param res waited result of jouerGagnant()
     */
    void testCasJouerGagnant(ArrayList<Integer> jeu, ArrayList<Integer> resJeu, boolean res) {
        // Arrange
        System.out.print("jouerGagnant (" + jeu.toString() + ") : ");

        // Act
        boolean resExec = jouerGagnant(jeu);

        // Assert
        System.out.print(jeu.toString() + " " + resExec + " : ");
		boolean egaliteJeux = jeu.equals(resJeu);
        if (  egaliteJeux && (res == resExec) ) {
            System.out.println("OK\n");
        } else {
            System.err.println("ERREUR\n");
        }
    }	

    
    /**
     * Divides a pile of matches into two piles (1 pile = 1 line in the game).
     * The new pile is necessarily placed at the end of the list.
     * The pile being divided decreases by the number of matches removed.
     *
     * @param jeu   list of match piles
     * @param ligne the pile to be split
     * @param nb    number of matches REMOVED from the pile during the split
     */
    void enlever(ArrayList<Integer> jeu, int ligne, int nb) {
        // Error handling
        if (jeu == null) {
            System.err.println("enlever(): the 'jeu' parameter is null");
        } else if (ligne >= jeu.size()) {
            System.err.println("enlever(): the 'ligne' index is too large");
        } else if (nb >= jeu.get(ligne)) {
            System.err.println("enlever(): the number of matches to remove is too large");
        } else if (nb <= 0) {
            System.err.println("enlever(): the number of matches to remove is too small");
        } else if (2 * nb == jeu.get(ligne)) {
            System.err.println("enlever(): the number of matches to remove is exactly half the pile");
        } else {
            // New pile added to the game (necessarily at the end of the list)
            // This new pile contains the number of matches removed (nb) from the original pile
            jeu.add(nb);
            // The remaining pile loses "nb" matches
            jeu.set(ligne, (jeu.get(ligne) - nb));
        }
    }

    /**
     * Checks if it is possible to split one of the piles.
     *
     * @param jeu list of match piles
     * @return true if there is at least one pile with 3 or more matches, false otherwise
     */
    boolean estPossible(ArrayList<Integer> jeu) {
        boolean ret = false;
        if (jeu == null) {
            System.err.println("estPossible(): the 'jeu' parameter is null");
        } else {
            int i = 0;
            while (i < jeu.size() && !ret) {
                if (jeu.get(i) > 2) {
                    ret = true;
                }
                i++;
            }
        }
        return ret;
    }

    /**
     * Creates the very first test configuration from the game board.
     *
     * @param jeu      current game board
     * @param jeuEssai new configuration of the game board
     * @return the index of the pile that was split, or (-1) if no pile with 3 or more matches exists
     */
    int premier(ArrayList<Integer> jeu, ArrayList<Integer> jeuEssai) {
        int numTas = -1; // Default: no pile to split
        int i;

        if (jeu == null) {
            System.err.println("premier(): the 'jeu' parameter is null");
        } else if (!estPossible(jeu)) {
            System.err.println("premier(): no pile is divisible");
        } else if (jeuEssai == null) {
            System.err.println("premier(): the 'jeuEssai' parameter is null");
        } else {
            // Clear the test configuration list before copying
            jeuEssai.clear(); // Reset size to 0
            i = 0;

            // Copy the current game state into the test configuration
            while (i < jeu.size()) {
                jeuEssai.add(jeu.get(i));
                i++;
            }

            i = 0;
            boolean found = false;
            // Find a pile with at least 3 matches
            while (i < jeu.size() && !found) {
                if (jeuEssai.get(i) >= 3) {
                    found = true;
                    numTas = i;
                }
                i++;
            }

            // Split the pile (index numTas) into a new pile with 1 match
            // The pile at index numTas decreases by 1 match
            // The test configuration reflects this split
            if (numTas != -1) enlever(jeuEssai, numTas, 1);
        }

        return numTas;
    }

    /**
     * Brief tests for the "premier" method.
     */
    void testPremier() {
        System.out.println();
        System.out.println("*** testPremier() ***");

        ArrayList<Integer> jeu1 = new ArrayList<>();
        jeu1.add(10);
        jeu1.add(11);
        int ligne1 = 0;
        ArrayList<Integer> res1 = new ArrayList<>();
        res1.add(9);
        res1.add(11);
        res1.add(1);
        testCasPremier(jeu1, ligne1, res1);
    }

    /**
     * Tests a specific case for the "premier" method.
     *
     * @param jeu the game board
     * @param ligne the index of the pile that was split
     * @param res the expected game board after the first split
     */
    void testCasPremier(ArrayList<Integer> jeu, int ligne, ArrayList<Integer> res) {
        // Arrange
        System.out.print("premier (" + jeu.toString() + ") : ");
        ArrayList<Integer> jeuEssai = new ArrayList<>();
        // Act
        int noLigne = premier(jeu, jeuEssai);
        // Assert
        System.out.println("\nnoLigne = " + noLigne + " jeuEssai = " + jeuEssai.toString());
        boolean equalBoards = jeuEssai.equals(res);
        if (equalBoards && noLigne == ligne) {
            System.out.println("OK\n");
        } else {
            System.err.println("ERROR\n");
        }
    }

    /**
     * Generates the next test configuration (i.e., a possible split).
     *
     * @param jeu current game board
     * @param jeuEssai test configuration after splitting a pile
     * @param ligne the index of the last pile that was split
     * @return the index of the pile split for the new configuration, or -1 if no more splits are possible
     */
    int suivant(ArrayList<Integer> jeu, ArrayList<Integer> jeuEssai, int ligne) {
        int numTas = -1; // Default: no more splits possible

        if (jeu == null) {
            System.err.println("suivant(): the 'jeu' parameter is null");
        } else if (jeuEssai == null) {
            System.err.println("suivant(): the 'jeuEssai' parameter is null");
        } else if (ligne >= jeu.size()) {
            System.err.println("suivant(): the 'ligne' index is too large");
        } else {
            int currentPile = jeuEssai.get(ligne);
            int lastPile = jeuEssai.get(jeuEssai.size() - 1);

            // If the difference between the current pile and the last pile is > 2
            if ((currentPile - lastPile) > 2) {
                jeuEssai.set(ligne, (currentPile - 1));
                jeuEssai.set(jeuEssai.size() - 1, (lastPile + 1));
                numTas = ligne;
            } else {
                // Move to the next pile and attempt a split
                jeuEssai.clear();
                for (int i = 0; i < jeu.size(); i++) {
                    jeuEssai.add(jeu.get(i));
                }

                boolean found = false;
                for (int i = ligne + 1; i < jeuEssai.size() && !found; i++) {
                    if (jeu.get(i) > 2) {
                        found = true;
                        enlever(jeuEssai, i, 1);
                        numTas = i;
                    }
                }
            }
        }

        return numTas;
    }

    /**
     * Brief tests for the "suivant" method.
     */
    void testSuivant() {
        System.out.println();
        System.out.println("*** testSuivant() ***");

        int ligne1 = 0;
        int resLigne1 = 0;
        ArrayList<Integer> jeu1 = new ArrayList<>();
        jeu1.add(10);
        ArrayList<Integer> jeuEssai1 = new ArrayList<>();
        jeuEssai1.add(9);
        jeuEssai1.add(1);
        ArrayList<Integer> res1 = new ArrayList<>();
        res1.add(8);
        res1.add(2);
        testCasSuivant(jeu1, jeuEssai1, ligne1, res1, resLigne1);

        int ligne2 = 0;
        int resLigne2 = -1;
        ArrayList<Integer> jeu2 = new ArrayList<>();
        jeu2.add(10);
        ArrayList<Integer> jeuEssai2 = new ArrayList<>();
        jeuEssai2.add(6);
        jeuEssai2.add(4);
        ArrayList<Integer> res2 = new ArrayList<>();
        res2.add(10);
        testCasSuivant(jeu2, jeuEssai2, ligne2, res2, resLigne2);

        int ligne3 = 1;
        int resLigne3 = 1;
        ArrayList<Integer> jeu3 = new ArrayList<>();
        jeu3.add(4);
        jeu3.add(6);
        jeu3.add(3);
        ArrayList<Integer> jeuEssai3 = new ArrayList<>();
        jeuEssai3.add(4);
        jeuEssai3.add(5);
        jeuEssai3.add(3);
        jeuEssai3.add(1);
        ArrayList<Integer> res3 = new ArrayList<>();
        res3.add(4);
        res3.add(4);
        res3.add(3);
        res3.add(2);
        testCasSuivant(jeu3, jeuEssai3, ligne3, res3, resLigne3);
    }

    /**
     * Tests a specific case for the "suivant" method.
     *
     * @param jeu the game board
     * @param jeuEssai the game board after a split
     * @param ligne the index of the last pile split
     * @param resJeu the expected game board after the split
     * @param resLigne the expected index of the split pile
     */
    void testCasSuivant(ArrayList<Integer> jeu, ArrayList<Integer> jeuEssai, int ligne, ArrayList<Integer> resJeu, int resLigne) {
        // Arrange
        System.out.print("suivant (" + jeu.toString() + ", " + jeuEssai.toString() + ", " + ligne + ") : ");
        // Act
        int noLigne = suivant(jeu, jeuEssai, ligne);
        // Assert
        System.out.println("\nnoLigne = " + noLigne + " jeuEssai = " + jeuEssai.toString());
        boolean equalBoards = jeuEssai.equals(resJeu);
        if (equalBoards && noLigne == resLigne) {
            System.out.println("OK\n");
        } else {
            System.err.println("ERROR\n");
        }
    }
}
