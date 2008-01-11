package rcssjava.geom;

import rcssjava.VecPosition;

/**
 * A 2d shape that can be tested to determine if a point lies
 * inside it or not
 */
public interface Region
{
    /**
     * Is the given point inside?
     * @param p test point
     * @return <code>true</code> if point lies inside
     */
    boolean isInside( VecPosition p );
}
