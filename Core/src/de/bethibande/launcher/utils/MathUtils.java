package de.bethibande.launcher.utils;

public class MathUtils {

    public static float map(float val, float a, float bA) {
        float d = val/a;
        return d*bA;
    }

    public static int[] rotatePoint(int x, int y, int xCenter, int yCenter, float angle) {
        int xRot = (int)(xCenter + Math.cos(angle) * (x - xCenter) - Math.sin(angle) * (y - yCenter));
        int yRot = (int)(yCenter + Math.sin(angle) * (x - xCenter) + Math.cos(angle) * (y - yCenter));
        return new int[]{xRot, yRot};
    }

}
