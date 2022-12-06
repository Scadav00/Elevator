package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.TreeSet;

public class ElevatorApp {
    public static void main(String[] args) {
        System.out.println("Hello to the elevator app!");

        Thread processorThread = new Thread(new RequestProcessor(), "processorThread");
        Thread listenerThread = new Thread(new RequestListener(), "listenerThread");


        Elevator.getElevator_instance().setProcessorThread(processorThread);
        processorThread.start();
        listenerThread.start();

    }
}

class Elevator {

    private static Elevator elevator_instance = null;

    public int currentFloor;
    //  creating an empty set
    private TreeSet ts = new TreeSet();

    //  default is up?
    private Direction direction = Direction.UP;

    private Thread processorThread;
    private Elevator()
    {
        currentFloor = 0;
    }


    public static Elevator getElevator_instance()
    {
        if (elevator_instance == null)
            elevator_instance = new Elevator();
        return elevator_instance;
    }

    public synchronized void addFloor(int floor) {
        ts.add(floor);
    }

    public synchronized int moveToFloor() {
        Integer floor = null;
//        >
        Integer greaterFloor = (Integer) ts.ceiling(currentFloor);
//        <
        Integer lowerFloor = (Integer) ts.floor(currentFloor);

        switch (direction) {
            case UP:
                if (greaterFloor != null) {
                    floor = greaterFloor;
                } else {
                    floor = lowerFloor;
                } break;
            case DOWN:
                if (lowerFloor != null) {
                    floor = lowerFloor;
                } else {
                    floor = greaterFloor;
                }
        }
        if (floor == null) {
            try {
                System.out.println("Waiting at Floor :" + getCurrentFloor());
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            ts.remove(floor);
        }
        return (floor == null) ? -1 : floor;
}

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(int currentFloor) throws InterruptedException {
        if (this.currentFloor < currentFloor)
            setDirection(Direction.UP);
        else {
            setDirection(Direction.DOWN);
        }
        this.currentFloor = currentFloor;
        System.out.println("Floor number: " + currentFloor);
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setProcessorThread(Thread processorThread) {
        this.processorThread = processorThread;
    }
}

class RequestListener implements Runnable {

    @Override
    public void run() {
        while (true) {
            Elevator elevator = Elevator.getElevator_instance();
            int floor = elevator.moveToFloor();
            int currentFloor = elevator.getCurrentFloor();
            try {
                if (floor >= 0){
                   while (currentFloor < floor) {
                       elevator.setCurrentFloor(++currentFloor);
                   }
                }else {
                    while (currentFloor > floor) {
                        elevator.setCurrentFloor(--currentFloor);
                    }
                }
                System.out.println("You have arrived at the Floor: " + elevator.getCurrentFloor());

            }catch (InterruptedException e){
                if(elevator.getCurrentFloor() != floor) {
                    elevator.getElevator_instance().addFloor(floor);
                }

            }

        }

    }
}
class RequestProcessor implements Runnable {

    @Override
    public void run() {
        while (true) {
            String inputFloorNumber = null;
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
                inputFloorNumber = bufferedReader.readLine();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("User Pressed : " + inputFloorNumber);
            Elevator elevator = Elevator.getElevator_instance();
            elevator.addFloor(Integer.parseInt(inputFloorNumber));

        }

    }
}

enum Direction {
    UP,DOWN

}