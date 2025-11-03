package com.codenjoy.dojo.snake.client;

import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.RandomDice;

import java.sql.Driver;
import java.util.List;

public class YourSolver implements Solver<Board> {

  private Dice dice;
  private Board board;

  public YourSolver(Dice dice) {
    this.dice = dice;
  }

  @Override
  public String get(Board b) {
    this.board = b;

    List<Point> snake = b.getSnake();
    Point head = b.getHead();
    Point apple = b.getApples().get(0);
    Point stone = b.getStones().get(0);

    System.out.println(b);

    Direction dir = Solution.solve(b);

    return dir.toString();
  }

  public static void main(String[] args) {
    String addr = "http://165.227.150.31/codenjoy-contest/board/player/tuxsefy07z3lxvo3w45h?code=877459404227811781";
    WebSocketRunner.runClient(
        addr,
        new YourSolver(new RandomDice()),
        new Board());
  }

}
