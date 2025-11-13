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

        if (board.getApples().isEmpty()) {
            return Direction.UP;
        }
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

            Point nextHead = getNextHead(board, head, dir);
            if (nextHead == null || obstacles.contains(nextHead)) {
                continue;
            }

            safeMoves.add(dir);
        }

        if (safeMoves.isEmpty()) {
            if (!isOpposite(Direction.UP, currentDirection)) return Direction.UP;
            if (!isOpposite(Direction.RIGHT, currentDirection)) return Direction.RIGHT;
            if (!isOpposite(Direction.LEFT, currentDirection)) return Direction.LEFT;
            return Direction.DOWN;
        }

        return findBestMove(safeMoves, head, apple, board);
    }

    private static Direction findBestMove(List<Direction> safeMoves, Point head, Point apple, Board board) {
        Direction bestMove = safeMoves.get(0);
        double minDistance = Double.MAX_VALUE;

        for (Direction dir : safeMoves) {
            Point newHead = getNextHead(board, head, dir);
            if (newHead == null) continue;

            double distance = newHead.distance(apple);

            if (distance < minDistance) {
                minDistance = distance;
                bestMove = dir;
            }
        }
        return bestMove;
    }

    private static Point getNextHead(Board board, Point head, Direction dir) {
        int nextX = head.getX();
        int nextY = head.getY();

        switch (dir) {
            case UP:
                nextY--;
                break;
            case DOWN:
                nextY++;
                break;
            case LEFT:
                nextX--;
                break;
            case RIGHT:
                nextX++;
                break;
        }

        try {
            return board.getPointAt(nextX, nextY);
        } catch (Exception e) {
            return null;
        }
    }
    private static boolean isOpposite(Direction newDir, Direction currentDir) {
        if (newDir == Direction.UP && currentDir == Direction.DOWN) return true;
        if (newDir == Direction.DOWN && currentDir == Direction.UP) return true;
        if (newDir == Direction.LEFT && currentDir == Direction.RIGHT) return true;
        if (newDir == Direction.RIGHT && currentDir == Direction.LEFT) return true;
        return false;
    }
}