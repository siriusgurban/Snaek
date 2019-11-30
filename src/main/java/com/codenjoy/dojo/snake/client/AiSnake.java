package com.codenjoy.dojo.snake.client;

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AiSnake {
    private Point stone;
    private Point apple;
    private Lee lee;
    private Point snake_head;
    private boolean gameOver;
    private ArrayList<Point> obstacles = new ArrayList<>();

    List<Point> snake = new ArrayList<>();

    public AiSnake(Board board) {
        List<Point> walls = board.getWalls();
        gameOver = board.isGameOver();
        stone = board.getStones().get(0);
        apple = board.getApples().get(0);
        snake_head = board.getHead();
        int dimX = walls.stream().mapToInt(Point::getX).max().orElse(0) + 1;
        int dimY = walls.stream().mapToInt(Point::getY).max().orElse(0) + 1;
        obstacles.addAll(walls);
        obstacles.add(stone);
        snake = board.getSnake();
        obstacles.addAll(snake);
        lee = new Lee(dimX, dimY);
    }

    Direction solve() {
        if (gameOver) return Direction.UP;
        Optional<List<LeePoint>> solution = lee.path(snake_head, apple, obstacles);
        Optional<List<LeePoint>> solution_stone = lee.path(snake_head, stone, obstacles);
        if (snake.size() > 10) {
            if (solution_stone.isPresent()) {

                return to_direct(snake_head, solution_stone.get().get(1));
            }
        }
        if (solution.isPresent()) {
            return to_direct(snake_head, solution.get().get(1));
        }


        return Direction.UP;
    }

    private Direction to_direct(Point from, LeePoint to) {
        if (to.x() < from.getX()) return Direction.LEFT;
        if (to.x() > from.getX()) return Direction.RIGHT;
        if (to.y() > from.getY()) return Direction.UP;
        if (to.y() < from.getY()) return Direction.DOWN;
        throw new RuntimeException("error");
    }
}
