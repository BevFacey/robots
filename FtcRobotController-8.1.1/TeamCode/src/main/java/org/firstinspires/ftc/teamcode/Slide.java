package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

public class Slide {

    // Initialize Device
    static DcMotor slide;

    // Setting max and min heights
    int maximum = -5000;
    int minimum = 0;

    // Setting up slide object
    public Slide(DcMotor slide) {

        this.slide = slide;

        // Stops the slide if there is no input
        this.slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        // Sets the current encoder position to 0
        this.slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        // Tells the robot to interpret all motor input as changes in the encoder value
        this.slide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    // Sets the slide speed
    public void setSlidePower(double slidePower) {

        // Prevents the slide from moving past the min and max heights
        if (slide.getCurrentPosition() > minimum && slidePower > 0 || slide.getCurrentPosition() < maximum && slidePower < 0) {

            slide.setPower(0);
        } else {

            // Runs the slide if it is not above or below the min or max
            slide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            slide.setPower(slidePower / 2);
        }
    }

    // Moves the slide to a certain position
    public static void goToPosition(int position, double speed) {

        slide.setTargetPosition(position);
        slide.setPower(speed);
        slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    // Returns the slides positon
    public int getPosition() {

        return slide.getCurrentPosition();
    }
}