public interface MenuController {
    void setup();
    void sizing(int width, int height);
    void addComponents();
    void coloring(ColorPalette palette, int theme);
    void removeComponents();
}