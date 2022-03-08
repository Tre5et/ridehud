package net.treset.ridehud.hud;

public enum TextureCoordinate {
    JUMP_BAR_FULL (new TextureArea(0,0,91, 5)),
    JUMP_BAR_EMPTY (new TextureArea(0,5,91, 5)),
    SPEED_BAR_FULL (new TextureArea(0,10,91, 5)),
    SPEED_BAR_EMPTY (new TextureArea(0,15,91, 5)),
    JUMP_BAR_ICON (new TextureArea(0,20,18, 18)),
    SPEED_BAR_ICON (new TextureArea(18,20,18, 18)),
    HEART_UNAVAILABLE (new TextureArea(36, 20, 9, 9)),
    HEART_FULL (new TextureArea(45, 20, 9, 9)),
    HEART_EMPTY (new TextureArea(54, 20, 9, 9));

    public final TextureArea area;

    TextureCoordinate(TextureArea area) { this.area = area; };

    public TextureArea getArea() { return this.area; }
}
