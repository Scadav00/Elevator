package org.example;

import java.util.TreeSet;

public class ElevatorApp {
    public static void main(String[] args) {
        System.out.println("Hello to the elevator app!");

        Thread processorThread = new Thread(new RequestProcessor(), "processorThread");
        Thread listenerThread = new Thread(new RequestListener(), "listenerThread");
        processorThread.start();
        listenerThread.start();


    }
}

class Elevator {

    private static Elevator elevator_instance = null;

    public int currentFloor;

    private TreeSet requestSet = new TreeSet();

//    default is up?
    private Direction direction = Direction.UP;
    private Elevator()
    {
        currentFloor = 0;
    }

    public static Elevator getInstance()
    {
        if (elevator_instance == null)
            elevator_instance = new Elevator();
        return elevator_instance;
    }



}

class RequestListener implements Runnable {

    @Override
    public void run() {

    }
}

class RequestProcessor implements Runnable {

}

enum Direction {
    UP,DOWN

}