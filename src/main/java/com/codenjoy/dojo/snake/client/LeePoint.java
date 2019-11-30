package com.codenjoy.dojo.snake.client;

import com.codenjoy.dojo.services.Point;

public class LeePoint {

    private int x;
    private int y;

    public LeePoint(Point point) {
        this(point.getX(), point.getY());
    }

    LeePoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int x(){
        return this.x;
    }

    public int y(){
        return this.y;
    }

    public LeePoint delta(LeePoint delta){
        return new LeePoint(
                this.x + delta.x,
                this.y + delta.y);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("LeePoint{");
        sb.append("x=").append(x);
        sb.append(", y=").append(y);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) return true;
        if (that == null || getClass() != that.getClass()) return false;
        LeePoint leePoint = (LeePoint) that;
        return x == leePoint.x &&
                y == leePoint.y;
    }

    @Override
    public int hashCode() {
        return x << 16 + y;
    }
}
