package p01_intro;

class ThreadsIntroMain {

    // There are two ways to run something in a different thread in Java
    //      1. extend Thread class
    //      2. implement Runnable interface

    /**************** 1. ****************/

    private static class MyThread extends Thread {
        @Override
        public void run() {
            // override method run with your logic
        }
    }

    /************************************/

    /**************** 2. ****************/

    private static class MyRunnableThread implements Runnable {
        @Override
        public void run() {
            // override method run with your logic
        }
    }

    /************************************/

    // What is the difference? If the model of the application is such that you need
    // to subclass another class, then you must implement Runnable because multiple
    // inheritance is not allowed in Java

    // How to use these classes?

    public static void main(String[] args) {
        Thread t1 = new MyThread();
        t1.run();   // WRONG
        t1.start(); // CORRECT - this sets up the actual system thread and invokes run() method

        Thread t2 = new Thread(new MyRunnableThread());
        t2.start();
    }

}
