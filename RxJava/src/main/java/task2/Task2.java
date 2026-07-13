package task2;

import java.util.LinkedList;
import java.util.Queue;

public class Task2 {

    public static void main(String[] args) throws InterruptedException {
        WorkerThread thread = new WorkerThread();
        thread.addTask(() -> {
            System.out.println("do work");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        thread.addTask(() -> {
            System.out.println("and another work");
        });
        thread.start();
        Thread.sleep(2000);
        thread.stopWorkerThread();
    }

}

class WorkerThread extends Thread {

    private final Queue<Runnable> tasks = new LinkedList<>();
    private volatile boolean isRunning = true;

    @Override
    public void run() {
        while (isRunning || !tasks.isEmpty()) {
            Runnable task;
            synchronized (tasks) {
                while (tasks.isEmpty() && isRunning) {
                    try {
                        tasks.wait();
                    } catch (InterruptedException e) {
                        System.out.println("worker was stopped");
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
                if (tasks.isEmpty()) {
                    return;
                }
                task = tasks.poll();
            }
            if (task != null) {
                task.run();
            }

        }

    }

    void addTask(Runnable task) {
        synchronized (tasks) {
            tasks.add(task);
            tasks.notify();
        }
    }


    void stopWorkerThread() {
        synchronized (tasks) {
            isRunning = false;
            tasks.notifyAll();
        }
    }


}







