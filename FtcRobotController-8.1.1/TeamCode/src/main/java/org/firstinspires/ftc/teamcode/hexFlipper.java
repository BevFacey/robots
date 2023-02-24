package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.qualcomm.robotcore.hardware.DcMotor;

public class hexFlipper {

    // Initialize Device
    static DcMotor hexflipper;

    // Setting up hexflipper object
    public hexFlipper(DcMotor hexflipper) {

        this.hexflipper = hexflipper;

        this.hexflipper.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.hexflipper.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.hexflipper.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    // Flips the hex flipper into the robot, releases cone, flips the hexflipper out of the robot
    public static void firstFlip() throws InterruptedException {

        // Creating a new thread to allow functions to be run simultaneously
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {

                // Flips into the robot
                hexflipper.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

                hexflipper.setTargetPosition(70);
                hexflipper.setPower(1);
                hexflipper.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // Resets the encoder to make the hexflipper go limp which accounts for any differences between different executions
                hexflipper.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

                // Waits for a bit before releasing the cone
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Releases the cone
                try {
                    Grabber.setGrabberPosition(true, false);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                hexflipper.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Flips out of the robot
                hexflipper.setTargetPosition(-70);
                hexflipper.setPower(1);
                hexflipper.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                try {
                    Thread.sleep(700);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                hexflipper.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            }
        });

        thread.start();
    }

    // Flips the hex flipper out of the robot
    public static void hexFlipperDown() throws InterruptedException {

        // Creating a new thread to allow functions to be run simultaneously
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {

                hexflipper.setTargetPosition(-70);
                hexflipper.setPower(1);
                hexflipper.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                try {
                    Thread.sleep(700);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                hexflipper.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            }
        });

        thread.start();
    }

    // Flips the hex flipper into the robot
    public static void hexFlipperUp() throws InterruptedException {

        // Creating a new thread to allow functions to be run simultaneously
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {

                hexflipper.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

                hexflipper.setTargetPosition(70);
                hexflipper.setPower(1);
                hexflipper.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                hexflipper.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            }
        });

        thread.start();
    }

    // Returns current position
    public int getPosition() {

        return hexflipper.getCurrentPosition();
    }
}

