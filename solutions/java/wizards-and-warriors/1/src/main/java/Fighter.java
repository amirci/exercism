class Fighter {

    boolean isVulnerable() {return true;}

    int getDamagePoints(Fighter fighter) {return 1;}

    @Override
    public String toString() {return "Fighter is a " + getClass().getSimpleName();}
}

class Warrior extends Fighter {
    /**
       This is not needed but the test _actually_ check for the method declaration.
     */
    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    boolean isVulnerable() {return false;}

    @Override
    int getDamagePoints(Fighter fighter) {return fighter.isVulnerable() ? 10 : 6;}
}

class Wizard extends Fighter {
    private boolean spellPrepared;

    @Override
    public String toString() {return super.toString();}

    void prepareSpell() {
        spellPrepared = true;
    }

    @Override
    boolean isVulnerable() {return !spellPrepared;}

    @Override
    int getDamagePoints(Fighter fighter) {return spellPrepared ? 12 : 3;}
}
