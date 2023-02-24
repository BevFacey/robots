package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Servo;

public class Grabber {

    // Initialize devices
    static Servo leftclaw;
    static Servo rightclaw;

    static boolean isOpen = false;

    // Setting up grabber object
    public Grabber(Servo leftclaw, Servo rightclaw) {

        this.leftclaw = leftclaw;
        this.rightclaw = rightclaw;
    }

    // Sets grabber to either open or close
    public static void setGrabberPosition(boolean open, boolean close) throws InterruptedException {

        // Creating a new thread to allow functions to be run simultaneously
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {

                if (open) {

                    leftclaw.setPosition(1);
                    rightclaw.setPosition(0);
                }

                if (close) {

                    leftclaw.setPosition(0);
                    rightclaw.setPosition(1);
                }
            }
        });

        thread.start();
    }
}
