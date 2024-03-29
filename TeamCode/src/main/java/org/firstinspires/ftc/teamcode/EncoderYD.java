/* Copyright (c) 2017 FIRST. All rights reserved.
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

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcontroller.external.samples.HardwarePushbot;

/**
 * This file illustrates the concept of driving a path based on encoder counts.
 * It uses the common Pushbot hardware class to define the drive on the robot.
 * The code is structured as a LinearOpMode
 *
 * The code REQUIRES that you DO have encoders on the wheels,
 *  *   otherwise you would use: PushbotAutoDriveByTime;
 *
 *  This code ALSO requires that the drive Motors have been configured such that a positive
 *  power command moves them forwards, and causes the encoders to count UP.
 *
 *   The desired path in this example is:
 *   - Drive forward for 48 inches
 *   - Spin right for 12 Inches
 *   - Drive Backwards for 24 inches
 *   - Stop and close the claw.
 *
 *  The code is written using a method called: encoderDrive(speed, leftInches, rightInches, timeoutS)
 *  that performs the actual movement.
 *  This methods assumes that each movement is relative to the last stopping place.
 *  There are other ways to perform encoder based moves, but this method is probably the simplest.
 *  This code uses the RUN_TO_POSITION mode to enable the Motor controllers to generate the run profile
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Autonomous(name="EncoderYD", group="Pushbot")
//@Disabled
public class EncoderYD extends LinearOpMode {

    /* Declare OpMode members. */
    HardwarePushbotSN         robot   = new HardwarePushbotSN();   // Use a Pushbot's hardware
    private ElapsedTime     runtime = new ElapsedTime();

    static final double     COUNTS_PER_MOTOR_REV    = 383.6*2  ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 3.937 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
                                                      (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     DRIVE_SPEED             = 0.5;
    static final double     TURN_SPEED              = 0.4;


    @Override
    public void runOpMode() {

        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Resetting Encoders");    //
        telemetry.update();

        robot.prepEncoderDrive();

        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData("Path0",  "Starting at %7d :%7d",
                          robot.CH_M_FL.getCurrentPosition(),
                          robot.CH_M_FR.getCurrentPosition(),
                          robot.CH_M_BL.getCurrentPosition(),
                          robot.CH_M_BR.getCurrentPosition());
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();


        // Step through each leg of the path,
        // Note: Reverse movement is obtained by setting a negative distance (not speed)
        //Forward         encoderDrive(DRIVE_SPEED,  20,   20, 20, -20);
        //Reverse        encoderDrive(DRIVE_SPEED,  -20,   -20, -20, 20);
        //Strafe Left        encoderDrive(DRIVE_SPEED,  -38,   38, 38, 38);
        //Strafe Right        encoderDrive(DRIVE_SPEED,  38,   -38, -38, -38);

        Forward(20);
        StrafeL(48);
        StrafeL(40);
        Forward(20);
        StrafeL(5);
        Backward(30);
        StrafeR(40);



        //      robot.leftClaw.setPosition(1.0);            // S4: Stop and close the claw.
        //      robot.rightClaw.setPosition(0.0);
        //     sleep(1000);     // pause for servos to move

        telemetry.addData("Path", "Complete");
        telemetry.update();
    }
    public void Forward(double distance){
        encoderDrive(DRIVE_SPEED,  distance,   distance, distance, -distance);
    }
    public void Backward(double distance){
        encoderDrive(DRIVE_SPEED,  -distance,   -distance, -distance, distance);
    }
    public void StrafeL(double distance){
        encoderDrive(DRIVE_SPEED,  -distance,   distance, distance, distance);
    }
    public void StrafeR(double distance){
        encoderDrive(DRIVE_SPEED,  distance,   -distance, -distance, -distance);
    }
    /*
     *  Method to perfmorm a relative move, based on encoder counts.
     *  Encoders are not reset as the move is based on the current position.
     *  Move will stop if any of three conditions occur:
     *  1) Move gets to the desired position
     *  2) Move runs out of time
     *  3) Driver stops the opmode running.
     */
    public void encoderDrive(double speed,
                             double FL, double FR, double BL, double BR) {
        int newLeftTarget;
        int newLeftTarget1;
        int newRightTarget;
        int newRightTarget1;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller

            newLeftTarget = robot.CH_M_FL.getCurrentPosition() + (int)(FL * COUNTS_PER_INCH);
            newLeftTarget1 = robot.CH_M_BL.getCurrentPosition() + (int)(BL * COUNTS_PER_INCH);
            newRightTarget = robot.CH_M_FR.getCurrentPosition() + (int)(FR * COUNTS_PER_INCH);
            newRightTarget1 = robot.CH_M_BR.getCurrentPosition() + (int)(BR * COUNTS_PER_INCH);


            robot.CH_M_FL.setTargetPosition(newLeftTarget);
            robot.CH_M_FR.setTargetPosition(newRightTarget);
            robot.CH_M_BL.setTargetPosition(newLeftTarget1);
            robot.CH_M_BR.setTargetPosition(newRightTarget1);

            // Turn On RUN_TO_POSITION
            robot.CH_M_FL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.CH_M_FR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.CH_M_BL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.CH_M_BR.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            robot.CH_M_FR.setPower(Math.abs(speed));
            robot.CH_M_FL.setPower(Math.abs(speed));
            robot.CH_M_BR.setPower(Math.abs(speed));
            robot.CH_M_BL.setPower(Math.abs(speed));


            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                   (robot.CH_M_FL.isBusy() && robot.CH_M_FR.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
                telemetry.addData("Path2",  "Running at %7d :%7d",
                                            robot.CH_M_FL.getCurrentPosition(),
                                            robot.CH_M_FR.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            robot.CH_M_FL.setPower(0);
            robot.CH_M_FR.setPower(0);
            robot.CH_M_BL.setPower(0);
            robot.CH_M_BR.setPower(0);

            // Turn off RUN_TO_POSITION
//            robot.CH_M_FR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//            robot.CH.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //  sleep(250);   // optional pause after each move
        }
    }
}
