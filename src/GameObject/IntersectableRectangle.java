package GameObject;

// This interface allows for specifying a rectangle that can be used in intersection logic by the Rectangle class
public interface IntersectableRectangle {
    Rectangle getIntersectRectangle();

    default float getIntersectX1() {
        return getIntersectRectangle().getX1();
    }

    default float getIntersectY1() {
        return getIntersectRectangle().getY1();
    }

    default float getIntersectX2() {
        return getIntersectRectangle().getX2();
    }

    default float getIntersectY2() {
        return getIntersectRectangle().getY2();
    }
}
