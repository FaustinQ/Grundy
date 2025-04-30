import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * GrundyRecPerdantNeutre class for the Grundy Game implementation.
 * This program contains methods that tests jouerGagnant() and testEstGagnantEfficacite()
 * Version 3 upgrade the efficiency by using theory 3.4
 *
 * @author F.Quintane and B.Toumelin
 */
class GrundyRecPerdantNeutre {
    // Global variables accessible across all methods
    long cpt;
    ArrayList<Integer> plateau = new ArrayList<Integer>();
    ArrayList<ArrayList<Integer>> posPerdantes = new ArrayList<ArrayList<Integer>>();
    ArrayList<ArrayList<Integer>> posGagnantes = new ArrayList<ArrayList<Integer>>();


    /**
     * Principal method of the program
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
        }   */
        testEstGagnanteEfficacite();
    }
	
    /**
     * Plays the winning move if it exists.
     * 
     * @param jeu the game board
     * @return true if there is a winning move, false otherwise.
     */
    boolean jouerGagnant(ArrayList<Integer> jeu) {

        boolean gagnant = false;

        if (jeu == null) {
            System.err.println("jouerGagnant(): the 'jeu' parameter is null");
        } else {
            ArrayList<Integer> essai = new ArrayList<>();

            // A very first decomposition is performed from the game board.
            // This first decomposition is stored in the variable `essai`.
            // `ligne` is the index of the pile (starting from zero) in the ArrayList
            // that records the pile (number of matches) that was decomposed.
            int ligne = premier(jeu, essai);

            // Implementation of Rule 2:
            // A situation (or position) is said to be winning for the machine
            // if there exists AT LEAST ONE decomposition (i.e., an action that
            // consists of decomposing a pile into 2 unequal piles) that is losing for the opponent.
            // The machine will choose this losing decomposition for the opponent.
            while (ligne != -1 && !gagnant) {
                // `estPerdante` is a recursive method.
                if (estPerdante(essai)) {
                    // If `estPerdante` (for the opponent) returns true, it means that `essai`
                    // is the decomposition chosen by the machine, ensuring its victory.
                    jeu.clear();
                    gagnant = true;
                    // `essai` is copied into `jeu` because `essai` represents the new game state
                    // after the machine has made a winning move.
                    for (int i = 0; i < essai.size(); i++) {
                        jeu.add(essai.get(i));
                    }
                } else {
                    // If `estPerdante` returns false, the machine tries another decomposition
                    // by calling the `suivant` method. If, after calling `suivant`, `ligne` is -1,
                    // it means no more decompositions are possible from `jeu`, and we exit the loop.
                    // In other words, the machine did not find a single winning decomposition
                    // starting from `jeu`.
                    ligne = suivant(jeu, essai, ligne);
                }
            }
        }

        return gagnant;
    }

    /**
     * Recursive method to determine if the current configuration (or test game state) is losing.
     * This method is used by the machine to evaluate if the opponent can lose (100% certainty).
     * 
     * @param jeu current game board state (at a specific moment during the game)
     * @return true if the configuration is losing, false otherwise.
     */
    boolean estPerdante(ArrayList<Integer> jeu) {
        if (jeu == null) {
            System.err.println("estPerdante(): the 'jeu' parameter is null");
            return true;
        }

        // Create a normalized version of the game board.
        ArrayList<Integer> copieJeu = new ArrayList<>(jeu);
        Collections.sort(copieJeu);
        copieJeu.removeIf(n -> n == 1 || n == 2);

        // Check if any of the values in the game board are in a losing position.
        for (ArrayList<Integer> posPerdante : posPerdantes) {
            for (int tas : copieJeu) {
                if (posPerdante.size() == 1 && posPerdante.get(0).equals(tas)) {
                    // If a value in the game corresponds to a losing pile, we can conclude.
                    return true;
                }
            }
        }

        // Check if this position is already known as losing.
        if (posPerdantes.contains(copieJeu)) {
            return true;
        }

        // If no moves are possible, this is a losing position.
        if (!estPossible(jeu)) {
            posPerdantes.add(new ArrayList<>(copieJeu));
            return true;
        }

        // Try all possible decompositions.
        ArrayList<Integer> essai = new ArrayList<>();
        int ligne = premier(jeu, essai);

        while (ligne != -1) {
            // If a decomposition is not losing, the current configuration is not losing.
            if (!estPerdante(essai)) {
                return false;
            }
            ligne = suivant(jeu, essai, ligne);
            cpt++;
        }

        // Add the position as losing after normalization.
        posPerdantes.add(new ArrayList<>(copieJeu));
        return true;
    }

    /**
     * Indicates whether the current configuration is winning.
     * This method simply calls `estPerdante`.
     * 
     * @param jeu the game board
     * @return true if the configuration is winning, false otherwise.
     */
    boolean estGagnante(ArrayList<Integer> jeu) {
        boolean ret = false;

        // Adds trivial losing positions to the list of losing positions.
        for (int i = 0; i < jeu.size(); i++) {
            posPerdantes.add(new ArrayList<>(Arrays.asList(i, i)));
        }

        // Check if the position is already known as winning.
        if (estConnueGagnantes(jeu)) {
            return true;
        }

        if (jeu == null) {
            System.err.println("estGagnante(): the 'jeu' parameter is null");
        } else {
            ret = !estPerdante(jeu);
        }
        return ret;
    }

    /**
     * Brief tests for the `jouerGagnant` method.
     */
    void testJouerGagnant() {
        System.out.println();
        System.out.println("*** testJouerGagnant() ***");

        System.out.println("Testing normal cases");
        ArrayList<Integer> jeu1 = new ArrayList<>();
        jeu1.add(6);
        ArrayList<Integer> resJeu1 = new ArrayList<>();
        resJeu1.add(4);
        resJeu1.add(2);

        testCasJouerGagnant(jeu1, resJeu1, true);
    }

    /**
     * Tests a specific case for the `jouerGagnant` method.
     *
     * @param jeu    the game board
     * @param resJeu the expected game board after a winning move
     * @param res    the expected result from `jouerGagnant`
     */
    void testCasJouerGagnant(ArrayList<Integer> jeu, ArrayList<Integer> resJeu, boolean res) {
        // Arrange
        System.out.print("jouerGagnant (" + jeu.toString() + ") : ");

        // Act
        boolean resExec = jouerGagnant(jeu);

        // Assert
        System.out.print(jeu.toString() + " " + resExec + " : ");
        boolean egaliteJeux = jeu.equals(resJeu);
        if (egaliteJeux && (res == resExec)) {
            System.out.println("OK\n");
        } else {
            System.err.println("ERROR\n");
        }
    }

    /**
     * Splits a pile of matches into two piles (1 pile = 1 line).
     * The new pile is always added at the end of the list.
     * The pile that is split decreases by the number of matches removed.
     *
     * @param jeu   the game board (list of piles of matches)
     * @param ligne the pile to split
     * @param nb    the number of matches to remove from the pile
     */
    void enlever(ArrayList<Integer> jeu, int ligne, int nb) {
        // Error handling
        if (jeu == null) {
            System.err.println("enlever(): the 'jeu' parameter is null");
        } else if (ligne >= jeu.size()) {
            System.err.println("enlever(): the pile index is too large");
        } else if (nb >= jeu.get(ligne)) {
            System.err.println("enlever(): the number of matches to remove is too large");
        } else if (nb <= 0) {
            System.err.println("enlever(): the number of matches to remove is too small");
        } else if (2 * nb == jeu.get(ligne)) {
            System.err.println("enlever(): the number of matches to remove is half of the pile");
        } else {
            // Add a new pile to the game board (always at the end of the list)
            // This new pile contains the number of matches removed (nb) from the split pile
            jeu.add(nb);
            // The remaining pile has "nb" fewer matches
            jeu.set(ligne, (jeu.get(ligne) - nb));
        }
    }

    /**
     * Checks if it is possible to split one of the piles.
     *
     * @param jeu the game board
     * @return true if there is at least one pile with 3 or more matches, false otherwise.
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
     * Creates an initial test configuration from the game board.
     *
     * @param jeu      the game board
     * @param jeuEssai the new test configuration
     * @return the index of the pile that was split, or -1 if no pile with at least 3 matches exists.
     */
    int premier(ArrayList<Integer> jeu, ArrayList<Integer> jeuEssai) {

        int numTas = -1; // Default: no pile to split
        int i;

        if (jeu == null) {
            System.err.println("premier(): the 'jeu' parameter is null");
        } else if (!estPossible(jeu)) {
            System.err.println("premier(): no pile can be split");
        } else if (jeuEssai == null) {
            System.err.println("premier(): the 'jeuEssai' parameter is null");
        } else {
            // Reset `jeuEssai` before copying `jeu` into it
            jeuEssai.clear(); // Reset size to 0
            i = 0;

            // Copy `jeu` into `jeuEssai` pile by pile
            // `jeuEssai` is initially identical to `jeu`
            while (i < jeu.size()) {
                jeuEssai.add(jeu.get(i));
                i++;
            }

            i = 0;
            // Search for a pile with at least 3 matches in `jeu`
            // If none exists, `numTas` will remain -1
            boolean trouve = false;
            while ((i < jeu.size()) && !trouve) {
                // If a pile with at least 3 matches is found
                if (jeuEssai.get(i) >= 3) {
                    trouve = true;
                    numTas = i;
                }
                i++;
            }

            // Split the pile (at index `numTas`) into a new pile with one match,
            // added at the end of the list. The original pile at `numTas` decreases by 1.
            if (numTas != -1) enlever(jeuEssai, numTas, 1);
        }

        return numTas;
    }

    /**
     * Brief tests for the `premier` method.
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
     * Tests a specific case for the `premier` method.
     *
     * @param jeu   the game board
     * @param ligne the index of the pile that was split first
     * @param res   the expected game board after the first split
     */
    void testCasPremier(ArrayList<Integer> jeu, int ligne, ArrayList<Integer> res) {
        // Arrange
        System.out.print("premier (" + jeu.toString() + ") : ");
        ArrayList<Integer> jeuEssai = new ArrayList<>();
        // Act
        int noLigne = premier(jeu, jeuEssai);
        // Assert
        System.out.println("\nnoLigne = " + noLigne + " jeuEssai = " + jeuEssai.toString());
        boolean egaliteJeux = jeuEssai.equals(res);
        if (egaliteJeux && noLigne == ligne) {
            System.out.println("OK\n");
        } else {
            System.err.println("ERROR\n");
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
     * Generates the next test configuration (i.e., one possible decomposition).
     * 
     * @param jeu      the game board
     * @param jeuEssai the test configuration of the game after splitting
     * @param ligne    the index of the pile that was last split
     * @return the index of the pile split in the new configuration, or -1 if no more decompositions are possible.
     */
    int suivant(ArrayList<Integer> jeu, ArrayList<Integer> jeuEssai, int ligne) {

        int numTas = -1; // Default: no further decomposition possible

        int i = 0;
        // Error handling
        if (jeu == null) {
            System.err.println("suivant(): the 'jeu' parameter is null");
        } else if (jeuEssai == null) {
            System.err.println("suivant(): the 'jeuEssai' parameter is null");
        } else if (ligne >= jeu.size()) {
            System.err.println("suivant(): the 'ligne' parameter is too large");
        } else {

            int nbAllumEnLigne = jeuEssai.get(ligne);
            int nbAllDernCase = jeuEssai.get(jeuEssai.size() - 1);

            // If more matches can be removed from the same pile (passed as a parameter),
            // i.e., if the difference between the matches in this pile and
            // the matches in the last pile of the array is > 2, remove 1 more match
            // from this pile and add it to the last pile.
            if ((nbAllumEnLigne - nbAllDernCase) > 2) {
                jeuEssai.set(ligne, (nbAllumEnLigne - 1));
                jeuEssai.set(jeuEssai.size() - 1, (nbAllDernCase + 1));
                numTas = ligne;
            }

            // Otherwise, check the next pile in the game for possible decomposition.
            // Recreate a new test configuration identical to the original game board.
            else {
                // Copy the game board into jeuEssai
                jeuEssai.clear();
                for (i = 0; i < jeu.size(); i++) {
                    jeuEssai.add(jeu.get(i));
                }

                boolean separation = false;
                i = ligne + 1; // Start with the next pile
                // If there is another pile and it has at least 3 matches,
                // make an initial split by removing 1 match.
                while (i < jeuEssai.size() && !separation) {
                    // The pile must contain at least 3 matches
                    if (jeu.get(i) > 2) {
                        separation = true;
                        // Begin by removing 1 match from this pile
                        enlever(jeuEssai, i, 1);
                        numTas = i;
                    } else {
                        i++;
                    }
                }
            }
        }

        return numTas;
    }

    /**
     * Brief tests for the `suivant` method.
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
     * Tests a specific case for the `suivant` method.
     * 
     * @param jeu      the game board
     * @param jeuEssai the game board obtained after splitting a pile
     * @param ligne    the index of the pile that was last split
     * @param resJeu   the expected `jeuEssai` after splitting
     * @param resLigne the expected index of the pile that was split
     */
    void testCasSuivant(ArrayList<Integer> jeu, ArrayList<Integer> jeuEssai, int ligne, ArrayList<Integer> resJeu, int resLigne) {
        // Arrange
        System.out.print("suivant (" + jeu.toString() + ", " + jeuEssai.toString() + ", " + ligne + ") : ");
        // Act
        int noLigne = suivant(jeu, jeuEssai, ligne);
        // Assert
        System.out.println("\nnoLigne = " + noLigne + " jeuEssai = " + jeuEssai.toString());
        boolean egaliteJeux = jeuEssai.equals(resJeu);
        if (egaliteJeux && noLigne == resLigne) {
            System.out.println("OK\n");
        } else {
            System.err.println("ERROR\n");
        }
    }

    /**
     * JavaDoc: Tests the efficiency of the `estGagnante` method.
     */
    void testEstGagnanteEfficacite() {
        // Local variables
        int n;
        long t1, t2, diffT;
        // Initialization
        n = 3;
        // Multiply n by 2 in each iteration
        for (int i = 1; i <= 50000; i++) {
            posPerdantes.clear();
            posGagnantes.clear();
            cpt = 0; // Global variable "long"

            t1 = System.nanoTime();
            estGagnante(new ArrayList<>(Arrays.asList(n)));
            t2 = System.nanoTime();
            diffT = (t2 - t1); // In nanoseconds
            System.out.println("n = " + n + " " + "cpt = " + cpt + " " + "Time = " + diffT + " ns ");
            n++;
        }
    }

}
