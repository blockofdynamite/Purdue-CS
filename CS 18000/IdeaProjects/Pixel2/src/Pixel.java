public class Pixel {

    private int pixel;

    public void testPixel() {
        System.out.printf("%x \n", pixel);
    }

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

    public void setBlue(int blueValue) {

        int manipulate = 0xFF;

        manipulate = ~manipulate;

        manipulate = manipulate & pixel;

        pixel = manipulate;

        blueValue &= 0xFF;

        int blueValueManipulate = blueValue;

        blueValueManipulate = blueValueManipulate | pixel;

        pixel = blueValueManipulate;

    }

    public void setGreen(int greenValue) {

        int manipulate = 0xFF << 8;

        manipulate = ~manipulate;

        manipulate = manipulate & pixel;

        pixel = manipulate;

        greenValue &= 0xFF;

        int greenValueManipulate = greenValue << 8;

        greenValueManipulate = greenValueManipulate | pixel;

        pixel = greenValueManipulate;

    }

    public void setRed(int redValue) {

        int manipulate = 0xFF << 16;

        manipulate = ~manipulate;

        manipulate = manipulate & pixel;

        pixel = manipulate;

        redValue &= 0xFF;

        int redValueManipulate = redValue << 16;

        redValueManipulate = redValueManipulate | pixel;

        pixel = redValueManipulate;

    }

    public static void main(String[] args) {

        Pixel p3 = new Pixel(0xFF000000);
        System.out.printf("rgb = (%d, %d, %d)\n", p3.getRed(), p3.getGreen(), p3.getBlue());

        System.out.println("Pixel is: ");
        p3.testPixel();

        p3.setRed(42);
        p3.setGreen(18);
        p3.setBlue(225);
        System.out.println("Pixel is: ");
        p3.testPixel();
        System.out.printf("rgb = (%d, %d, %d)\n", p3.getRed(), p3.getGreen(), p3.getBlue());

        p3.setRed(-1);
        p3.setGreen(500);
        p3.setBlue(1000);
        System.out.println("Pixel is: ");
        p3.testPixel();
        System.out.printf("rgb = (%d, %d, %d)\n", p3.getRed(), p3.getGreen(), p3.getBlue());
    }
}
