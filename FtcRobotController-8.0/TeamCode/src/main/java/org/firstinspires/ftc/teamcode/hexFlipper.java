package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class hexFlipper {

    static DcMotor hexflipper;

    public hexFlipper(DcMotor hexflipper) {

        this.hexflipper = hexflipper;

        this.hexflipper.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.hexflipper.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.hexflipper.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public static void firstFlip() throws InterruptedException {

        hexflipper.setTargetPosition(110);
        hexflipper.setPower(1);
        hexflipper.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Thread.sleep(1500);

        while (hexflipper.getCurrentPosition() != 110) {

            int i = 1;
        }

        Grabber.setGrabberPosition(false, true);
        Thread.sleep(2000);

        hexflipper.setTargetPosition(0);
        hexflipper.setPower(0.4);
        hexflipper.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

//    public static void firstFlip2() throws InterruptedException {
//
//
//        Thread.sleep(1500);
//        Grabber.setGrabberPosition(false, true);
//        Thread.sleep(2000);
//
//        hexflipper.setTargetPosition(0);
//        hexflipper.setPower(0.4);
//        hexflipper.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//    }
}
