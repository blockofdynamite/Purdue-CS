public class Pixel {

    private int pixel;

    public Pixel(int pixel) {
        this.pixel = pixel;
    }

    public int getBlue() {
        int bluePixelValue = pixel;

        bluePixelValue = bluePixelValue & 255;

        return bluePixelValue;
    }

    public int getGreen() {
        int greenPixelValue = 0;

        greenPixelValue = pixel >> 8;

        greenPixelValue = greenPixelValue & 255;

        return greenPixelValue;
    }

    public int getRed() {
        int redPixelValue = 0;

        redPixelValue = pixel >> 16;

        redPixelValue = redPixelValue & 255;

        return redPixelValue;
    }

    public static void main(String[] args) {
        Pixel p1 = new Pixel(0xFFFF00FF);
        System.out.printf("rgb = (%2x, %2x, %2x)\n", p1.getRed(), p1.getGreen(), p1.getBlue());
        System.out.printf("rgb = (%d, %d, %d)\n", p1.getRed(), p1.getGreen(), p1.getBlue());

        Pixel p2 = new Pixel(0xFF43BF11);
        System.out.printf("rgb = (%2x, %2x, %2x)\n", p2.getRed(), p2.getGreen(), p2.getBlue());
        System.out.printf("rgb = (%d, %d, %d)\n", p2.getRed(), p2.getGreen(), p2.getBlue());
    }
}
