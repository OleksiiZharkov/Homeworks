package com.codenjoy.dojo.snake.client;

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;

import java.util.ArrayList;
import java.util.List;

public class Solution {

    public static Direction solve(Board board) {

        if (board.isGameOver()) {
            return Direction.UP;
        }

        Point head = board.getHead();
        Point apple = board.getApples().get(0);
        Direction currentDirection = board.getSnakeDirection();

        List<Point> obstacles = new ArrayList<>();
        obstacles.addAll(board.getWalls());
        obstacles.addAll(board.getStones());
        obstacles.addAll(board.getSnake());

        List<Direction> safeMoves = new ArrayList<>();
        for (Direction dir : Direction.values()) {

            if (isOpposite(dir, currentDirection)) {
                continue;
            }

            Point nextHead = dir.apply(head);

            if (!obstacles.contains(nextHead)) {
                safeMoves.add(dir);
            }
        }

        if (safeMoves.isEmpty()) {
            return Direction.UP;
        }

        return findBestMove(safeMoves, head, apple);
    }

    private static Direction findBestMove(List<Direction> safeMoves, Point head, Point apple) {
        Direction bestMove = safeMoves.get(0);


        double minDistance = Double.MAX_VALUE;

        for (Direction dir : safeMoves) {

            Point newHead = dir.apply(head);


            double distance = newHead.distance(apple);

            if (distance < minDistance) {
                minDistance = distance;
                bestMove = dir;
            }
        }
        return bestMove;
    }

    private static boolean isOpposite(Direction newDir, Direction currentDir) {
        if (newDir == Direction.UP && currentDir == Direction.DOWN) return true;
        if (newDir == Direction.DOWN && currentDir == Direction.UP) return true;
        if (newDir == Direction.LEFT && currentDir == Direction.RIGHT) return true;
        if (newDir == Direction.RIGHT && currentDir == Direction.LEFT) return true;
        return false;
    }
}