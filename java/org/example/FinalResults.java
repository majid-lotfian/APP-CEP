package org.example;

public class FinalResults {
    String privatePatternID;

    int numOfMatchesWithoutWindowing;

    int numOfMatchesWindowing;

    int numOfRevealedPrivatePatterns;
    int numOfRevealedOtherPrivatePatterns;

    public void setNumOfRevealedOtherPrivatePatterns(int numOfRevealedOtherPrivatePatterns) {
        this.numOfRevealedOtherPrivatePatterns = numOfRevealedOtherPrivatePatterns;
    }

    public int getNumOfRevealedOtherPrivatePatterns() {
        return numOfRevealedOtherPrivatePatterns;
    }

    int numOfRevealedByAdversary;

    int numOfTrulyDetectedPublicPattern;

    public void setNumOfRevealedByAdversary(int numOfRevealedByAdversary) {
        this.numOfRevealedByAdversary = numOfRevealedByAdversary;
    }

    public int getNumOfRevealedByAdversary() {
        return numOfRevealedByAdversary;
    }




    public void setPrivatePatternID(String privatePatternID) {
        this.privatePatternID = privatePatternID;
    }

    public void setNumOfMatchesWithoutWindowing(int numOfMatchesWithoutWindowing) {
        this.numOfMatchesWithoutWindowing = numOfMatchesWithoutWindowing;
    }

    public void setNumOfMatchesWindowing(int numOfMatchesWindowing) {
        this.numOfMatchesWindowing = numOfMatchesWindowing;
    }

    public void setNumOfRevealedPrivatePatterns(int numOfRevealedPrivatePatterns) {
        this.numOfRevealedPrivatePatterns = numOfRevealedPrivatePatterns;
    }

    public void setNumOfTrulyDetectedPublicPattern(int numOfTrulyDetectedPublicPattern) {
        this.numOfTrulyDetectedPublicPattern = numOfTrulyDetectedPublicPattern;
    }

    public String getPrivatePatternID() {
        return privatePatternID;
    }

    public int getNumOfMatchesWithoutWindowing() {
        return numOfMatchesWithoutWindowing;
    }

    public int getNumOfMatchesWindowing() {
        return numOfMatchesWindowing;
    }

    public int getNumOfRevealedPrivatePatterns() {
        return numOfRevealedPrivatePatterns;
    }

    public int getNumOfTrulyDetectedPublicPattern() {
        return numOfTrulyDetectedPublicPattern;
    }

    public FinalResults(
            String privatePatternID,
            int numOfMatchesWithoutWindowing,
            int numOfMatchesWindowing,
            int numOfRevealedPrivatePatterns,
            int numOfRevealedByAdversary,
            int numOfTrulyDetectedPublicPattern,
            int numOfRevealedOtherPrivatePatterns) {
        this.privatePatternID = privatePatternID;
        this.numOfMatchesWithoutWindowing = numOfMatchesWithoutWindowing;
        this.numOfMatchesWindowing = numOfMatchesWindowing;
        this.numOfRevealedPrivatePatterns = numOfRevealedPrivatePatterns;
        this.numOfRevealedByAdversary = numOfRevealedByAdversary;
        this.numOfTrulyDetectedPublicPattern = numOfTrulyDetectedPublicPattern;
        this.numOfRevealedOtherPrivatePatterns=numOfRevealedOtherPrivatePatterns;
    }
}
