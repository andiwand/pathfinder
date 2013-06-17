package util;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.util.List;

import at.andiwand.commons.math.geometry.GeometryCircle2D;
import at.andiwand.commons.math.geometry.GeometryLine2D;
import at.andiwand.commons.math.vector.Vector2d;

public class GraphicsUtil {

    private final Graphics g;

    public GraphicsUtil(Graphics g) {
	this.g = g;
    }

    public void fillCircle(Vector2d center, double radius) {
	int size = (int) (radius * 2);
	g.fillOval((int) (center.getX() - radius),
		(int) (center.getY() - radius), size, size);
    }

    public void fillCircle(GeometryCircle2D circle) {
	fillCircle(circle.getCenter(), circle.getRadius());
    }

    public void drawLine(Vector2d a, Vector2d b) {
	g.drawLine((int) a.getX(), (int) a.getY(), (int) b.getX(),
		(int) b.getY());
    }

    public void drawLine(GeometryLine2D line) {
	drawLine(line.getPointA(), line.getPointB());
    }

    public void drawLines(List<GeometryLine2D> lines) {
	for (GeometryLine2D line : lines) {
	    drawLine(line);
	}
    }

    public void drawString(Vector2d start, String string) {
	g.drawString(string, (int) start.getX(), (int) start.getY());
    }

    public void drawXCenterString(Vector2d xCenter, String string) {
	FontMetrics fontMetrics = g.getFontMetrics();
	Rectangle2D bounds = fontMetrics.getStringBounds(string, g);

	g.drawString(string, (int) (xCenter.getX() - bounds.getWidth() / 2),
		(int) xCenter.getY());
    }

}