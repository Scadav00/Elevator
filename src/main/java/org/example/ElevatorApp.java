package org.example;

import java.util.TreeSet;

public class ElevatorApp {
    public static void main(String[] args) {
        System.out.println("Hello to the elevator app!");


    }
}

class Elevator {

    private int currentFloor = 0;

    private TreeSet requestSet = new TreeSet();

//    default is up?
    private Direction direction = Direction.UP;
    private Elevator() {};



}

enum Direction {
    UP,DOWN

}