package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;


/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 ] *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="GamepadYD", group="Linear Opmode")
//@Disabled
public class GamepadYD extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor CH_M_FL = null;
    private DcMotor CH_M_FR = null;
    private DcMotor CH_M_BL = null;
    private DcMotor CH_M_BR = null;

    //@Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        CH_M_FL = hardwareMap.get(DcMotor.class, "CH_M_FL");
        CH_M_FR = hardwareMap.get(DcMotor.class, "CH_M_FR");
        CH_M_BL = hardwareMap.get(DcMotor.class, "CH_M_BL");
        CH_M_BR = hardwareMap.get(DcMotor.class, "CH_M_BR");

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        CH_M_FL.setDirection(DcMotor.Direction.FORWARD);
        CH_M_FR.setDirection(DcMotor.Direction.FORWARD);
        CH_M_BL.setDirection(DcMotor.Direction.FORWARD);
        CH_M_BR.setDirection(DcMotor.Direction.FORWARD);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // Setup a variable for each drive wheel to save power level for telemetry
            double FLpx;
            double FRpx;
            double BLpx;
            double BRpx;
            double FLpy;
            double FRpy;
            double BLpy;
            double BRpy;
            double FLpyt;
            double FRpyt;
            double BLpyt;
            double BRpyt;


            // Choose to drive using either Tank Mode, or POV Mode
            // Comment out the method that's not used.  The default below is POV.

            // POV Mode uses left stick to go forward, and right stick to turn.
            // - This uses basic math to combine motions and is easier to drive straight.
            //   double drive = -gamepad1.left_stick_y;
            //  double turn  =  gamepad1.right_stick_x;
            //   leftPower    = Range.clip(drive + turn, -1.0, 1.0) ;
            //   rightPower   = Range.clip(drive - turn, -1.0, 1.0) ;

            // Tank Mode uses one stick to control each wheel.
            // - This requires no math, but it is hard to drive forward slowly and keep straight.
            FLpx = gamepad1.left_stick_x;
            FRpx = FLpx;
            BLpx = FLpx;
            BRpx = FLpx;
            FLpy = gamepad1.left_stick_y;
            FRpy = FLpy;
            BLpy = FLpy;
            BRpy = FLpy;
            FLpyt = gamepad1.right_stick_y;
            FRpyt = FLpyt;
            BLpyt = FLpyt;
            BRpyt = FLpyt;


            CH_M_BR.setDirection(DcMotorSimple.Direction.REVERSE);
            CH_M_FL.setDirection(DcMotorSimple.Direction.REVERSE);
            CH_M_BL.setDirection(DcMotorSimple.Direction.REVERSE);

            // Send calculated power to wheels
            CH_M_FL.setPower(FLpy);
            CH_M_FR.setPower(FRpy);
            CH_M_BL.setPower(BLpy);
            CH_M_BR.setPower(BRpy);
            CH_M_FL.setPower(-FLpx);
            CH_M_FR.setPower(FRpx);
            CH_M_BL.setPower(BLpx);
            CH_M_BR.setPower(-BRpx);

            CH_M_FL.setDirection(DcMotorSimple.Direction.FORWARD);
            CH_M_BL.setDirection(DcMotorSimple.Direction.FORWARD);
            CH_M_FL.setPower(FLpyt);
            CH_M_FR.setPower(FRpyt);
            CH_M_BL.setPower(BLpyt);
            CH_M_BR.setPower(BRpyt);


            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Motors", "left (FLpx), right (%.2f)");
            telemetry.update();
        }
    }
}
