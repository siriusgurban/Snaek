package com.codenjoy.dojo.snake.client;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.snake.client.LeePoint;

import java.util.*;
import java.util.stream.Collectors;

public class Lee {
    private final static ArrayList<LeePoint> deltas = new ArrayList<>() {{
        add(new LeePoint(1, 0));
        add(new LeePoint(0, 1));
        add(new LeePoint(-1, 0));
        add(new LeePoint(0, -1));
    }};
    //board
    private int dimX;
    private int dimY;
    private int[][] board;
    //constants
    private final static int START = -1;
    private final static int OBSTACLE = -10;

    //constructor
    public Lee(int dimX, int dimY) {
        this.dimX = dimX;
        this.dimY = dimY;
        this.board = new int[dimX][dimY];
    }

    public int get(LeePoint point) {
        return this.board[point.x()][point.y()];
    }

    public void set(LeePoint point, int value) {
        this.board[point.x()][point.y()] = value;
    }

    public boolean isOnBoard(LeePoint point) {
        return (point.x() >= 0 && point.x() <= dimX && point.y() >= 0 && point.y() <= dimY);
    }

    public boolean isUnvisited(LeePoint point) {
        return get(point) == 0;
    }

    Set<LeePoint> neighbors(LeePoint point) {
        return deltas.stream().map(d -> d.delta(point)).filter(this::isOnBoard).collect(Collectors.toSet());
    }

    Set<LeePoint> neighborsUnvisited(LeePoint point) {
        return neighbors(point).stream().filter(this::isUnvisited).collect(Collectors.toSet());
    }

    LeePoint neighborValue(LeePoint point, int value) {
        return neighbors(point).stream().filter(p -> get(p) == value).findFirst().get();
    }

    public void inBoard(List<LeePoint> obstacles) {
        for (int i = 0; i < dimX; i++) {
            for (int j = 0; j < dimY; j++) {
                board[i][j] = 0;
            }
        }
        obstacles.forEach(p -> set(p, OBSTACLE));
    }


    public Optional<List<LeePoint>> path(Point start, Point end, List<Point> obstacles) {
        return path(new LeePoint(start), new LeePoint(end),
                obstacles.stream().map(LeePoint::new).collect(Collectors.toList()));
    }

    public Optional<List<LeePoint>> path(LeePoint start, LeePoint finish, List<LeePoint> obstacles) {
        inBoard(obstacles);
        boolean pathFound = false;
        set(start, START);
        Set<LeePoint> curr = new HashSet<>();
        int[] counter = {0};
        curr.add(start);
        while (!curr.isEmpty() && !pathFound) {
            counter[0]++;
            Set<LeePoint> nextPoint = curr.stream()
                    .map(this::neighborsUnvisited)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toSet());
            nextPoint.forEach(p -> set(p, counter[0]));
            if (nextPoint.contains(finish)) {
                pathFound = true;
            }
        }
        if (!pathFound) return Optional.empty();
        set(start, 0);
        ArrayList<LeePoint> snakePath = new ArrayList<>();
        snakePath.add(finish);
        LeePoint curr_p = finish;
        while (counter[0] > 0) {
            counter[0]--;
            LeePoint prev_p = neighborValue(curr_p, counter[0]);
            snakePath.add(prev_p);
            curr_p = prev_p;
        }
        Collections.reverse(snakePath);
        return Optional.of(snakePath);
    }
}
