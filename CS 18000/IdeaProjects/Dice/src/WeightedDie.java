class WeightedDie extends Die {
    public int roll() {
        if (Math.random() > .5) {
            return 6;
        }

        else {
            return super.roll();
        }
    }

    public static void main(String[] args) {
        WeightedDie r = new WeightedDie();
        System.out.println(r.roll());
    }
}
