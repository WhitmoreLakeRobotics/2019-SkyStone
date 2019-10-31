package org.firstinspires.ftc.teamcode;

/* controls all actions the intake arm

 */


import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


//@TeleOp(name = "IntakeArm", group = "CHASSIS")  // @Autonomous(...) is the other common choice

public class Gripper extends BaseHardware {

    private static final String TAGIntakeArm = "8492-GripperServo";

    /* Declare OpMode members. */

    private GRIPPER_STATES GripperState_desired = GRIPPER_STATES.UNKNOWN;
    private GRIPPER_STATES GripperState_current = GRIPPER_STATES.UNKNOWN;

    private ElapsedTime GripperTimer = null;
    private int GripperMoveTime = 1250;

    // Define the hardware
    private Servo gripperSvo = null;
    private double gripperSvoPos_start = 0.0;
    private double gripperSvoPos_open = 0.0;
    private double gripperSvoPos_close = 1.0;


    @Override
    public void init() {
        gripperSvo = hardwareMap.servo.get("GSVR1");
        if (gripperSvo == null) {
            telemetry.log().add("GSVR1 is null...");
        }
        GripperTimer = new ElapsedTime();
        telemetry.addData("GRIPPER", "Initialized");
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {

    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        GripperState_desired = GRIPPER_STATES.OPEN;
        GripperState_current = GRIPPER_STATES.UNKNOWN;
        GripperTimer.reset();
        cmd_open();

    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {

        if (GripperState_current != GripperState_desired) {

            switch (GripperState_desired) {
                case OPEN: {
                    GripperState_current = GRIPPER_STATES.OPENING;
                    GripperState_desired = GRIPPER_STATES.OPEN;
                    GripperTimer.reset();
                    break;
                }

                case CLOSED: {
                    GripperState_current = GRIPPER_STATES.CLOSING;
                    GripperState_desired = GRIPPER_STATES.CLOSED;
                    GripperTimer.reset();

                    break;
                }


                case CLOSING: {
                    gripperSvo.setPosition(gripperSvoPos_close);
                    if (GripperTimer.milliseconds() > GripperMoveTime) {
                        GripperState_current = GRIPPER_STATES.CLOSING;
                    }
                    break;
                }
                case OPENING: {
                    gripperSvo.setPosition(gripperSvoPos_open);
                    if (GripperTimer.milliseconds() > GripperMoveTime) {
                        GripperState_current = GRIPPER_STATES.OPEN;
                    }
                    break;
                }

                default: {
                    break;
                }
            }
        }
    }

    public boolean atDestination(GRIPPER_STATES test_state) {
        return (GripperState_current == test_state);
    }


    public void cmd_open() {
        GripperState_desired = GRIPPER_STATES.OPENING;
    }


    public void cmd_close() {
        GripperState_desired = GRIPPER_STATES.CLOSING;
    }


    public boolean getIsOpen() {
        return GripperState_current == GRIPPER_STATES.OPEN;
    }

    public boolean getIsClosed() {
        return GripperState_current == GRIPPER_STATES.CLOSED;
    }


    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {

    }

    public static enum GRIPPER_STATES {
        OPEN,
        CLOSING,
        CLOSED,
        OPENING,
        UNKNOWN
    }
}
