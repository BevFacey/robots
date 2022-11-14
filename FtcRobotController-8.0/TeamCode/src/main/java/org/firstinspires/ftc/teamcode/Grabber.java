package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Servo;

public class Grabber {

    static Servo leftclaw;
    static Servo rightclaw;

    static boolean isOpen = false;

    public Grabber(Servo leftclaw, Servo rightclaw) {

        this.leftclaw = leftclaw;
        this.rightclaw = rightclaw;
    }

    public static void setGrabberPosition(boolean open, boolean close) throws InterruptedException {

        if (open) {

            leftclaw.setPosition(1);
            rightclaw.setPosition(0);
        }

        if (close) {

            leftclaw.setPosition(0);
            rightclaw.setPosition(1);
        }
    }
}
