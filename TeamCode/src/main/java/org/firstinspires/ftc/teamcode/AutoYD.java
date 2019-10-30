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

@TeleOp(name="AutoYD", group="Linear Opmode")
//@Disabled
public class AutoYD extends LinearOpMode {

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


            CH_M_FR.setDirection(DcMotorSimple.Direction.REVERSE);
            CH_M_BR.setDirection(DcMotorSimple.Direction.REVERSE);

            // Send calculated power to wheels
            CH_M_FL.setPower(1);
            CH_M_FR.setPower(1);
            CH_M_BL.setPower(1);
            CH_M_BR.setPower(1);

            sleep(500);

            CH_M_FL.setPower(0);
            CH_M_FR.setPower(0);
            CH_M_BL.setPower(0);
            CH_M_BR.setPower(0);

            sleep(150);

            CH_M_FL.setPower(-1);
            CH_M_FR.setPower(-1);
            CH_M_BL.setPower(-1);
            CH_M_BR.setPower(-1);

            sleep(500);

            CH_M_FL.setPower(0);
            CH_M_FR.setPower(0);
            CH_M_BL.setPower(0);
            CH_M_BR.setPower(0);

        stop();
        }
    }
}
