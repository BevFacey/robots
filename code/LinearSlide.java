package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

public class LinearSlide {
    DcMotor slide;
    Servo grabber;
    boolean slideOverride = false;
    boolean rotatorOverride = false;

    public LinearSlide(DcMotor slide, Servo grabber) {
        this.slide = slide;
        this.slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.slide.setDirection(DcMotorSimple.Direction.REVERSE);
        this.slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.slide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        this.grabber = grabber;
    }

    public void setSlidePower(double slidePower) {
        setSlidePower(slidePower, false);
    }

    public void setSlidePower(double slidePower, boolean override) {

        slide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        if (slidePower <= 0 && slide.getCurrentPosition() < 50 && !override) {
            slide.setPower(0);
        } else {
            slide.setPower(slidePower);
        }

        if (this.slideOverride && !override) {
            slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }
        this.slideOverride = override;
    }

    public int getPosition() {
        return slide.getCurrentPosition();
    }

    public void goDown() {
        slide.setTargetPosition(0);
        slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slide.setPower(1);
    }

    public void setGrabberPosition(boolean close, boolean open) {
        if (open || close) {
            grabber.setPosition(close ? 1 : 0.6);
        }
    }

    public void goToPosition(int position) {
        slide.setTargetPosition(position);
        slide.setPower(slide.getCurrentPosition() > position ? -0.8 : 0.8);
        slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
}