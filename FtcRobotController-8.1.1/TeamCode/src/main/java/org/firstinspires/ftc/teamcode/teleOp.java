package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "TeleOp")
public class teleOp extends LinearOpMode {

    // Initialize devices
    private Motors motors;
    private Slide slide;
    private Grabber grabber;
    private servoFlipper servoflipper;
    private hexFlipper hexflipper;

    // Variables for slow override option
    boolean slowOverride = false;
    boolean slowOverridePressed = false;

    @Override
    public void runOpMode() throws InterruptedException {

        // Assigns devices a port location in relation to the control hub
        DcMotor frontLeft = hardwareMap.get(DcMotor.class, "frontleft");
        DcMotor frontRight = hardwareMap.get(DcMotor.class, "frontright");
        DcMotor backLeft = hardwareMap.get(DcMotor.class, "backleft");
        DcMotor backRight = hardwareMap.get(DcMotor.class, "backright");
        BNO055IMU imu = hardwareMap.get(BNO055IMU.class, "imu");

        // Creating objects of each device for use in functions
        motors = new Motors(frontLeft, frontRight, backLeft, backRight, imu);
        slide = new Slide(hardwareMap.get(DcMotor.class, "slide"));
        grabber = new Grabber(hardwareMap.get(Servo.class, "leftclaw"), hardwareMap.get(Servo.class, "rightclaw"));
        servoflipper = new servoFlipper(hardwareMap.get(Servo.class, "servoflipper"));
        hexflipper = new hexFlipper(hardwareMap.get(DcMotor.class, "hexflipper"));

        // Movement variables
        double forward;
        double strafe;
        double rotate;
        double modifier;

        // Initializers servo in proper starting position
        servoflipper.setServoPosition(0);

        waitForStart();

        // Main loop
        while (opModeIsActive()) {

            slowOverridePressed = gamepad1.right_bumper;

            // Moves the hex flipper if right or left bumpers are pressed
            if (gamepad2.right_bumper) {

                hexFlipper.hexFlipperDown();
            }

            if (gamepad2.left_bumper) {

                hexFlipper.hexFlipperUp();
            }

            // Turns on slow override if right bumper is pressed and it isn't already active
            if (gamepad1.right_bumper && slowOverridePressed == false) {

                slowOverride = true;
                slowOverridePressed = true;
            }

            // Turns off slow override of not holding down the right bumper
            if (gamepad1.right_bumper && slowOverridePressed == true) {

                slowOverride = false;
                slowOverridePressed = false;
            }

            // Changes movement speed based on whether slow override is pressed
            modifier = 1 - 0.8 * (slowOverride ? 1 : gamepad1.right_trigger);

            // Turns controller input into movement
            forward = toExp2(-gamepad1.left_stick_y);
            strafe = toExp2(gamepad1.left_stick_x);
            rotate = toExp2(gamepad1.right_stick_x);

            // Combines the directional movement into what one single wheel is doing
            double frontLeftPow = (forward + strafe + rotate) * modifier;
            double backLeftPow = (forward - strafe + rotate) * modifier;
            double frontRightPow = (forward - strafe - rotate) * modifier;
            double backRightPow = (forward + strafe - rotate) * modifier;

            // Tells the motors to move
            motors.setMotorPowers(frontLeftPow, frontRightPow, backLeftPow, backRightPow);

            // If the joystick is being moved move the robot
            if (gamepad2.left_stick_y != 0) {

                slide.setSlidePower(gamepad2.left_stick_y);
            // Stops slide movement if you aren't holding down the joystick
            } else {

                slide.setSlidePower(gamepad2.left_stick_y);
            }

            // Height preset for linear slide
            // Zero
            if (gamepad2.dpad_down) {

                while (slide.getPosition() < 0) {

                    slide.setSlidePower(0.7);
                }
            }

            //Highest point
            if (gamepad2.dpad_up) {

                Thread thread = new Thread(new Runnable() {

                    @Override
                    public void run() {

                        while (slide.getPosition() > -2900) {

                            slide.setSlidePower(-0.7);
                        }
                    }
                });

                thread.start();
            }

            // Middle point
            if (gamepad2.dpad_left) {

                Thread thread = new Thread(new Runnable() {

                    @Override
                    public void run() {

                        while (slide.getPosition() > -2100) {

                            slide.setSlidePower(-0.7);
                        }
                    }
                });

                thread.start();
            }

            // Lowest point
            if (gamepad2.dpad_right) {

                while (slide.getPosition() > -1000) {

                    slide.setSlidePower(-0.7);
                }
            }

            // Tells the grabber to open and close when x or b are pressed
            grabber.setGrabberPosition(gamepad2.x, gamepad2.b);

            // Flips the cone into the slide lifter
            if (gamepad2.a) {

                hexFlipper.firstFlip();
            }

            // Flips the cone onto the target pole
            if (gamepad2.y) {

                servoFlipper.secondFlip();
            }

            // Update Telemetry  
            telemetry.addData("Open", gamepad2.x);
            telemetry.addData("Closed", gamepad2.b);

            telemetry.addData("Linear Slide Position", slide.getPosition());
            telemetry.addData("Hex Flipper", hexflipper.getPosition());
            telemetry.update();
        }
    }

    // Account for error caused by weight differences in the movement code
    double toExp2(double num) {

        return Math.pow(num, 2) * (num > 0 ? 1 : -1);
    }
}
