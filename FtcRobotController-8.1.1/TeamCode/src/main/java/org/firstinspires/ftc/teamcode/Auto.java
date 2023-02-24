/* Copyright (c) 2019 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import java.util.List;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

/**
 * This 2022-2023 OpMode illustrates the basics of using the TensorFlow Object Detection API to
 * determine which image is being presented to the robot.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list.
 *
 * IMPORTANT: In order to use this OpMode, you need to obtain your own Vuforia license key as
 * is explained below.
 */

@TeleOp(name = "Auto", group = "Concept")
public class Auto extends LinearOpMode {

    private static final double SPEED = 0.5;
    private static final long DELAY = 2250;
    //private static final String TFOD_MODEL_ASSET = "PowerPlay.tflite";
    private static final String TFOD_MODEL_ASSET = "RainbowModel.tflite";

    private static final String[] LABELS = {
            "c",
            "g",
            "m"
    };


    private static final String VUFORIA_KEY =
            "AROc193/////AAABmR+zWoImb0XApfkidJTQwrEsswY+0QREMjXIRO5x6CyTp9ncFY6rMwFIyfY8TBO8c0qV+aotX2RVX9TBHh4k58NGQab+5NHpW1OoTUEtadm7j58GiRRrOrJ4HBEKM0TFb9tFcHhZZorPW1+rytNjZH9Gqb1piRzNiFEDGrzmcyiR0bQgrW5L9vwToBKP7ZGv35oExVGWHlk4q1HQLU7ie6QbLUCfHsZd7mMdh0lOcV4fjTqzwYDQZKUTJ20+cqtHqblLRzHPDX+0p8T30q80eUXymmkwG5GK7GYs2pyszB3+yQKOif9lik1JO79xTtSdRMXHmBT3A0pwgaPVMoqrbJplF/7h6DqXmOMJwrIAQbAm ";


    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;
    private Motors motors;
    private Slide slide;
    private servoFlipper servoflipper;
    private hexFlipper hexflipper;

    @Override
    public void runOpMode() throws InterruptedException {

		// must init vforia first because TFOD uses it for camera frames
        initVuforia();
        initTfod();

        if (tfod != null) {
            tfod.activate();
            tfod.setZoom(1.0, 16.0/9.0);
        }

        DcMotor frontLeft = hardwareMap.get(DcMotor.class, "frontleft");
        DcMotor frontRight = hardwareMap.get(DcMotor.class, "frontright");
        DcMotor backLeft = hardwareMap.get(DcMotor.class, "backleft");
        DcMotor backRight = hardwareMap.get(DcMotor.class, "backright");
        BNO055IMU imu = hardwareMap.get(BNO055IMU.class, "imu");

        servoflipper = new servoFlipper(hardwareMap.get(Servo.class, "servoflipper"));
        slide = new Slide(hardwareMap.get(DcMotor.class, "slide"));
        motors = new Motors(frontLeft, frontRight, backLeft, backRight, imu);
        hexflipper = new hexFlipper(hardwareMap.get(DcMotor.class, "hexflipper"));

        servoflipper.setServoPosition(0);
        waitForStart();

        /*if (opModeIsActive()) {

            motors.setMotorPowers(1,1,1,1);

            Thread.sleep(1000);

            motors.setMotorPowers(0,0,0,0);
        }*/


        if (opModeIsActive()) {

            while (opModeIsActive()) {

                Thread.sleep(DELAY);

                // 0 = left, 1 = center, 2 = right
                // default to magenta because we don't test for it due to false positive issues
                int position = 2;
                if (tfod != null) {

                    List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();

                    if (updatedRecognitions != null) {

                        telemetry.addData("# Objects Detected", updatedRecognitions.size());
						float maxConfidence = 0.29f;
                        Recognition theOne = null;

                        for (Recognition recognition : updatedRecognitions) {

                            double x = (recognition.getLeft() + recognition.getRight()) / 2 ;
                            double y = (recognition.getTop()  + recognition.getBottom()) / 2 ;
                            double width  = Math.abs(recognition.getRight() - recognition.getLeft()) ;
                            double height = Math.abs(recognition.getTop()  - recognition.getBottom()) ;

                            if (recognition.getConfidence() > maxConfidence && !recognition.getLabel().equals("m")) {

                                maxConfidence = recognition.getConfidence();
                                theOne = recognition;
                            }

                            telemetry.addData(""," ");
                            telemetry.addData("Image", "%s (%.0f %% Conf.)", recognition.getLabel(), recognition.getConfidence() * 100 );
                            telemetry.addData("- Position (x/y)","%.0f / %.0f", x, y);
                            telemetry.addData("- Size (Width/Height)","%.0f / %.0f", width, height);
                        }

                        telemetry.update();

                        if (theOne != null) {


                            if (theOne.getLabel().equals("g")) {

                                // move to position 1
                                position = 0;

                            } else if (theOne.getLabel().equals("c")) {

                                // move to position 2
                                position = 1;

                            }

                        }
                    }
                }
                // end of tfod


//                while (position != 0 && position != 1 && position != 2) {
//
//                    int i = 0;
//                }

                // go forward to cone position
                motors.setMotorPowers(SPEED,SPEED,SPEED,SPEED);
                Thread.sleep(3500);
                motors.setMotorPowers(0,0,0,0);

                hexFlipper.hexFlipperDown();

                // turn to face tower
                motors.setMotorPowers(-SPEED,SPEED,-SPEED,SPEED);
                Thread.sleep(800);
                motors.setMotorPowers(0,0,0,0);

                motors.setMotorPowers(SPEED,SPEED,SPEED,SPEED);
                Thread.sleep(250);
                motors.setMotorPowers(0,0,0,0);

                // do tower shit
                while (slide.getPosition() > -3700) {
                    slide.setSlidePower(-0.7);
                }
                slide.setSlidePower(0);

                // flip
                servoFlipper.secondFlip();
                Thread.sleep(1000);

                // lower slide
                while (slide.getPosition() < 0) {

                    slide.setSlidePower(0.5);
                }
                slide.setSlidePower(0);

                hexFlipper.hexFlipperUp();

                motors.setMotorPowers(-SPEED,-SPEED,-SPEED,-SPEED);
                Thread.sleep(250);
                motors.setMotorPowers(0,0,0,0);

                // turn back
                motors.setMotorPowers(SPEED,-SPEED,SPEED,-SPEED);
                Thread.sleep(800);
                motors.setMotorPowers(0,0,0,0);

                // go back and park
                motors.setMotorPowers(-SPEED,-SPEED,-SPEED,-SPEED);
                Thread.sleep(1700);
                motors.setMotorPowers(0,0,0,0);

                if (position == 0) {
                    moveLeft();
                } else if (position == 2) {
                    moveRight();
                } else {

                    Thread.sleep(200);

                    motors.setMotorPowers(SPEED,-SPEED,SPEED,-SPEED);

                    Thread.sleep(1500);

                    motors.setMotorPowers(0,0,0,0);
                }

                Thread.sleep(1000000000);
            }
        }
    }


	// take a wild guess what this does
    private void initVuforia() {

        // vuforia needs this for some reason
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");

        // I have an irrational dislike of this line
        vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }


    private void initTfod() {

		// magic numbers don't touch unless you know what you're doing
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
            "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.75f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 300;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);

        // Use loadModelFromAsset() if the TF Model is built in as an asset by Android Studio
        // Use loadModelFromFile() if you have downloaded a custom team model to the Robot Controller's FLASH.
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS);
        //tfod.loadModelFromFile(TFOD_MODEL_FILE, LABELS);
    }

    private void moveRight() throws InterruptedException {

        double frontLeftPower = SPEED;
        double frontRightPower = -SPEED;
        double backLeftPower = -SPEED;
        double backRightPower = SPEED;

        motors.setMotorPowers(SPEED, -SPEED, -SPEED, SPEED);

        Thread.sleep(2550);

        motors.setMotorPowers(0,0,0,0);
    }

    private void moveLeft() throws InterruptedException {

        double frontLeftPower = -SPEED;
        double frontRightPower = SPEED;
        double backLeftPower = SPEED;
        double backRightPower = -SPEED;

        motors.setMotorPowers(-SPEED, SPEED, SPEED, -SPEED);

        Thread.sleep(2300);

        motors.setMotorPowers(0,0,0,0);
    }
}
