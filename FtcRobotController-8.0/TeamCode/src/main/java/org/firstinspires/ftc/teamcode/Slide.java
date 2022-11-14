package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

public class Slide {

    DcMotor slide;

//    boolean slideOverride = false;

    public Slide(DcMotor slide) {

        this.slide = slide;

        // Stops the slide if there is no input
        this.slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        // Sets the logical direction of the motor
        this.slide.setDirection(DcMotorSimple.Direction.REVERSE);
        // Sets the current encoder position to 0
        this.slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        // Tells the robot to interpret all motor input as changes in the encoder value
        this.slide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void setSlidePower(double slidePower, boolean override) {

        slide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slide.setPower(slidePower * 0.6);

//        slide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        if (slidePower <= 0 && slide.getCurrentPosition() < 50 && !override) {
//
//            slide.setPower(0);
//
//        } else {
//
//            slide.setPower(slidePower);
//        }
//
//        if (this.slideOverride && !override) {
//
//            slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        }
//
//        this.slideOverride = override;
    }

    public void setSlidePower(double slidePower) {

        setSlidePower(slidePower, false);
    }

    public void goToPosition(int position) {

        slide.setTargetPosition(position);
        slide.setPower(slide.getCurrentPosition() > position ? -0.8 : 0.8);
        slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public int getPosition() {

        return slide.getCurrentPosition();
    }
}