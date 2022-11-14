package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Servo;

public class servoFlipper {

    static Servo servoflipper;

    public servoFlipper(Servo servoflipper) {

        this.servoflipper = servoflipper;
    }

    public static void secondFlip() throws InterruptedException {

        servoflipper.setPosition(1);
        Thread.sleep(1000);

        servoflipper.setPosition(0);
    }
}
