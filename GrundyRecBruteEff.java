import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Grundy game with AI for the computer
 * This program contains methods that tests jouerGagnant() and testEstGagnantEfficacite()
 * Version 0 is just the game and a test of efficency without upgrading it
 *
 * @author F.Quintane and B.Toumelin
 */
class GrundyRecBruteEff {
    // Global variables accessible by all methods
    long cpt; // Counter to track recursive calls
    ArrayList<Integer> plateau = new ArrayList<Integer>(); // Represents the game board

    /**
     * Main method to execute the program.
     */
    void principal(){
        /**
        int nbrcase = SimpleInput.getInt("Avec combien d'allumettes voulez-vous jouer ? ");
        while(nbrcase < 3){
            nbrcase = SimpleInput.getInt("Veuillez entrer un nombre d'allumettes supérieur à 2");
        }

        plateau.add(nbrcase);
        System.out.println("~~ Le Plateau " + plateau + " ~~");
        
        while(estPossible(plateau)){
            boolean check = true;
            System.out.println("~~ C'est à votre tour de jouer ~~");
            int tas = SimpleInput.getInt("Sur quel tas voulez-vous jouer ? ");
            while (tas < 0 || tas >= plateau.size() || plateau.get(tas) < 3){
                tas = SimpleInput.getInt("Veuillez entrer un tas valide ! ");
            }
            int all = SimpleInput.getInt("Combien d'allumettes séparez-vous du tas ? ");
            while (all >= plateau.get(tas) || all < 1){
                all = SimpleInput.getInt("Veuillez entrer un nombre d'allumettes valide  !");

            }
            enlever(plateau, tas, all);
            String sa = plateau.toString();
            System.out.println ( sa );
            System.out.println ();

            if (!estPossible(plateau)){
                System.out.println("Vous avez gagné!");
            }
            
            System.out.println("~~ L'ordinateur joue ~~");
            if(estGagnante(plateau)){
                jouerGagnant(plateau);
                String st = plateau.toString();
                System.out.println ( st );
                System.out.println ();
            } else {
                for(int i = 0; i < plateau.size(); i++){
                    if(plateau.get(i) > 2 && check){
                        enlever(plateau, i, 1);
                        check = false;
                        String st = plateau.toString();
                        System.out.println ( st );
                        System.out.println ();

                    }
                } 
            }
            if (!estPossible(plateau)){
                System.out.println("Vous avez perdu!");
            }
        }   
        */
        testEstGagnanteEfficacite();
    }
	
    /**
     * Plays the winning move if it exists.
     * 
     * @param jeu The current game board.
     * @return True if there is a winning move, false otherwise.
     */
    boolean jouerGagnant(ArrayList<Integer> jeu) {
        boolean gagnant = false;

        if (jeu == null) {
            System.err.println("jouerGagnant(): The parameter jeu is null");
        } else {
            ArrayList<Integer> essai = new ArrayList<Integer>(); // Temporary board for testing moves
            int ligne = premier(jeu, essai); // First decomposition

            // Rule 2: A position is winning if at least one decomposition leads to a losing configuration for the opponent.
            while (ligne != -1 && !gagnant) {
                if (estPerdante(essai)) {
                    jeu.clear();
                    gagnant = true;
                    jeu.addAll(essai); // Copy winning configuration to the main board
                } else {
                    ligne = suivant(jeu, essai, ligne); // Try the next decomposition
                }
            }
        }
        return gagnant;
    }

    /**
     * Recursive method to check if a configuration is losing.
     * 
     * @param jeu The current game board.
     * @return True if the configuration is losing, false otherwise.
     */
    boolean estPerdante(ArrayList<Integer> jeu) {
        boolean ret = true; // Default to losing configuration

        if (jeu == null) {
            System.err.println("estPerdante(): The parameter jeu is null");
        } else if (!estPossible(jeu)) {
            ret = true; // No more valid moves
        } else {
            ArrayList<Integer> essai = new ArrayList<Integer>(); // Temporary board for testing moves
            int ligne = premier(jeu, essai);

            while (ligne != -1 && ret) {
                if (estPerdante(essai)) {
                    ret = false; // If one decomposition is not losing, the configuration is not losing
                } else {
                    ligne = suivant(jeu, essai, ligne); // Try the next decomposition
                }
                cpt++; // Increment recursion counter
            }
        }
        return ret;
    }

    /**
     * Checks if a configuration is winning.
     * Calls "estPerdante" and negates the result.
     * 
     * @param jeu The current game board.
     * @return True if the configuration is winning, false otherwise.
     */
    boolean estGagnante(ArrayList<Integer> jeu) {
        boolean ret = false;
        if (jeu == null) {
            System.err.println("estGagnante(): The parameter jeu is null");
        } else {
            ret = !estPerdante(jeu);
        }
        return ret;
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

    /**
     * Tests the efficiency of the "estGagnante" method.
     */
    void testEstGagnanteEfficacite() {
        int n = 3;
        long t1, t2, diffT;

        for (int i = 1; i <= 30; i++) {
            ArrayList<Integer> plateauEff = new ArrayList<Integer>();
            plateauEff.add(n);
            cpt = 0; // Reset recursion counter
            t1 = System.nanoTime();
            estGagnante(plateauEff);
            t2 = System.nanoTime();
            diffT = t2 - t1;
            System.out.println("n = " + n + " cpt = " + cpt + " Time = " + diffT + " ns");
            n++;
        }
    }
}