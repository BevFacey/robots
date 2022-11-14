package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

@TeleOp(name = "TeleOp")
public class teleOp extends LinearOpMode {

    private Motors motors;
    private Slide slide;
    private Grabber grabber;
    private servoFlipper servoflipper;
    private hexFlipper hexflipper;

//    public static final int[] heightPresets = {0, 1300, 3050, 5500, 7500};
//    private int currentTarget = 0;
    boolean slowOverride = false;
    boolean slowOverridePressed = false;

    @Override
    public void runOpMode() throws InterruptedException {

        DcMotor frontLeft = hardwareMap.get(DcMotor.class, "frontleft");
        DcMotor frontRight = hardwareMap.get(DcMotor.class, "frontright");
        DcMotor backLeft = hardwareMap.get(DcMotor.class, "backleft");
        DcMotor backRight = hardwareMap.get(DcMotor.class, "backright");
        BNO055IMU imu = hardwareMap.get(BNO055IMU.class, "imu");

        motors = new Motors(frontLeft, frontRight, backLeft, backRight, imu);
        slide = new Slide(hardwareMap.get(DcMotor.class, "slide"));
        grabber = new Grabber(hardwareMap.get(Servo.class, "leftclaw"), hardwareMap.get(Servo.class, "rightclaw"));
        servoflipper = new servoFlipper(hardwareMap.get(Servo.class, "servoflipper"));
        hexflipper = new hexFlipper(hardwareMap.get(DcMotor.class, "hexflipper"));

        double forward;
        double strafe;
        double rotate;
        double modifier;

//        boolean targetMode = false;
//        boolean prevTargetMode;
//        boolean previouslyUpDownPressed = false;
//
//        long prevPress = 0;

        waitForStart();

        while (opModeIsActive()) {

            if (gamepad1.right_bumper && slowOverridePressed == false) {

                slowOverride = true;
            }

            slowOverridePressed = gamepad1.right_bumper;

//            prevTargetMode = targetMode;
//            targetMode = gamepad2.dpad_up || gamepad2.dpad_down || prevTargetMode;

            modifier = 1 - 0.8 * (slowOverride ? 1 : gamepad1.right_trigger);

            forward = toExp2(-gamepad1.left_stick_y);
            strafe = toExp2(gamepad1.left_stick_x);
            rotate = toExp2(gamepad1.right_stick_x);

            double frontLeftPow = (forward + strafe + rotate) * modifier;
            double backLeftPow = (forward - strafe + rotate) * modifier;
            double frontRightPow = (forward - strafe - rotate) * modifier;
            double backRightPow = (forward + strafe - rotate) * modifier;

            motors.setMotorPowers(frontLeftPow, frontRightPow, backLeftPow, backRightPow);

            if (gamepad2.left_stick_y != 0) {

//                targetMode = false;

                slide.setSlidePower(gamepad2.left_stick_y, gamepad2.back);
                telemetry.addData("Slide Mode", "Normal");

                if (gamepad2.left_stick_y > 0) {

                    telemetry.addData("Slide Mode", "Up");

                } else if (gamepad2.left_stick_y < 0) {

                    telemetry.addData("Slide Mode", "Down");

                } else {

                    telemetry.addData("Slide Mode", "Stopped");
                }

//            else if (targetMode) {
//
//                if ((gamepad2.dpad_up || gamepad2.dpad_down) && !previouslyUpDownPressed && System.currentTimeMillis() - prevPress > 200) {
//
//                    previouslyUpDownPressed = true;
//                    currentTarget = Math.max(currentTarget + (gamepad2.dpad_up ? 1 : -1), 0) % heightPresets.length;
//                    prevPress = System.currentTimeMillis();
//
//                } else {
//
//                    previouslyUpDownPressed = false;
//                }
//
//                slide.goToPosition(heightPresets[currentTarget]);
//                telemetry.addData("Slide Mode", "Automated");
//                telemetry.addData("Previous Press", previouslyUpDownPressed);
//                telemetry.addData("Current Target", heightPresets[currentTarget]);
//            }

                // Stops slide movement if you aren't holding down the joystick
            } else {

                slide.setSlidePower(gamepad2.left_stick_y, gamepad2.back);
            }

            grabber.setGrabberPosition(gamepad2.x, gamepad2.b);

            if (gamepad2.a) {

                hexFlipper.firstFlip();
            }

            if (gamepad2.y) {

                servoFlipper.secondFlip();
            }

            telemetry.addData("Open", gamepad2.x);
            telemetry.addData("Closed", gamepad2.b);

            telemetry.addData("Drive", forward);
            telemetry.addData("Strafe", strafe);
            telemetry.addData("Rotate", rotate);

            telemetry.addData("Linear Slide Position", slide.getPosition());

            telemetry.update();
        }
    }

    double toExp2(double num) {

        return Math.pow(num, 2) * (num > 0 ? 1 : -1);
    }
}
