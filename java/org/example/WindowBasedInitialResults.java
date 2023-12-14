package org.example;

public class WindowBasedInitialResults {
    int privatePatternID;
    String SelectedOT;
    int windowNumber;
    int publicMatches;
    int privateMatches;
    int otherPrivateMatches;

    public void setOtherPrivateMatches(int otherPrivateMatches) {
        this.otherPrivateMatches = otherPrivateMatches;
    }

    public int getOtherPrivateMatches() {
        return otherPrivateMatches;
    }

    int utility;

    boolean dependencyViolation;

    int actualPrivatePatternMatches;

    public void setActualPrivatePatternMatches(int actualPrivatePatternMatches) {
        this.actualPrivatePatternMatches = actualPrivatePatternMatches;
    }

    public int getActualPrivatePatternMatches() {
        return actualPrivatePatternMatches;
    }

    public void setDependencyViolation(boolean dependencyViolation) {
        this.dependencyViolation = dependencyViolation;
    }

    public boolean isDependencyViolation() {
        return dependencyViolation;
    }

    public WindowBasedInitialResults(
            int privatePatternID,
            String ot,
            int windowNumber,
            int publicMatches,
            int privateMatches,
            int utility,
            boolean DependencyViolation,
            int actualPrivatePatternMatches) {

        this.privatePatternID = privatePatternID;
        this.SelectedOT = ot;
        this.windowNumber = windowNumber;
        this.publicMatches= publicMatches;
        this.privateMatches = privateMatches;
        this.utility = utility;
        this.dependencyViolation = DependencyViolation;
        this.actualPrivatePatternMatches=actualPrivatePatternMatches;
    }

    public WindowBasedInitialResults() {
    }

    public void setUtility(int utility) {
        this.utility = utility;
    }

    public int getUtility() {
        return utility;
    }

    public int getPrivatePatternID() {
        return privatePatternID;
    }

    public String getSelectedOT() {
        return SelectedOT;
    }


    public int getWindowNumber() {
        return windowNumber;
    }

    public int getPublicMatches() {
        return publicMatches;
    }
    public int getPrivateMatches() {
        return privateMatches;
    }

    public void setPrivatePatternID(int privatePatternID) {
        this.privatePatternID = privatePatternID;
    }

    public void setSelectedOT(String selectedOT) {
        this.SelectedOT = selectedOT;
    }



    public void setWindowNumber(int windowNumber) {
        this.windowNumber = windowNumber;
    }

    public void setPublicMatches(int publicMatches) {
        this.publicMatches = publicMatches;
    }

    public void setPrivateMatches(int privateMatches) {
        this.privateMatches = privateMatches;
    }
}
