package pathfinder;

import at.andiwand.commons.math.geometry.GeometryLine2D;

public interface Wall {

    public boolean crosses(GeometryLine2D line);

}