package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Servo;

public class servoFlipper {

    // Initialize devices
    static Servo servoflipper;

    // Setting up servo flipper object
    public servoFlipper(Servo servoflipper) {

        this.servoflipper = servoflipper;
    }

    // Flips the cone onto the pole
    public static void secondFlip() throws InterruptedException {

        // Creating a new thread to allow functions to be run simultaneously
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {

                servoflipper.setPosition(1);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                servoflipper.setPosition(0);
            }
        });

        thread.start();
    }

    // Function to be used during robot initialization to make sure the servo flipper isn't limp
    public static void setServoPosition(double position) {

        servoflipper.setPosition(position);
    }
}
