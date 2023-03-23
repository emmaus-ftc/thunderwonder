package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.JavaUtil;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp

public class ENIGE_DIE_WERKT_COPY extends LinearOpMode {
    private Blinker control_Hub;
    private Gyroscope imu;
    private DcMotor motorFrontLeft;
    private DcMotor motorBackLeft;
    private DcMotor motorFrontRight;
    private DcMotor motorBackRight;
    private Servo Servo_Left;
    private Servo Servo_Right;
    private DcMotor Arm_Left;
    private DcMotor Arm_Right;
    
    
    // the order is frontleft. frontright, backleft, backright
    double[] currentSpeed = {0, 0, 0, 0};
    double[] maxSpeed = {0, 0, 0, 0};
    int[] stopped = {100, 100, 100, 100};
    double speedIncrease = 0.05; // i don't know if this is to much or to little
    
    double gears = 0.75;
    int cooldown = 10;
    

    
    @Override
    public void runOpMode() throws InterruptedException {
        // Declare our motors
        // Make sure your ID's match your configuration
    double ServoPosition;
    double ServoSpeed;
        DcMotor motorFrontLeft= hardwareMap.dcMotor.get("motorFrontLeft");
        DcMotor motorBackLeft = hardwareMap.dcMotor.get("motorBackLeft");
        DcMotor motorFrontRight = hardwareMap.dcMotor.get("motorFrontRight");
        DcMotor motorBackRight = hardwareMap.dcMotor.get("motorBackRight");
        Servo_Left = hardwareMap.get(Servo.class, "Servo_Left");
        Servo_Right = hardwareMap.get(Servo.class, "Servo_Right");
        Arm_Left = hardwareMap.get(DcMotor.class, "Arm_Left");
        Arm_Right = hardwareMap.get(DcMotor.class, "Arm_Right");

        // Reverse the right side motors
        // Reverse left motors if you are using NeveRests
        motorFrontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBackRight.setDirection(DcMotorSimple.Direction.REVERSE);
        Arm_Left.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBackLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorBackRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorFrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorFrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        // Arm_Left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        // Arm_Right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        ServoPosition = 0.5;
        ServoSpeed = 0.3;
        
        waitForStart();
            
        while (opModeIsActive()) {
            double y = gamepad2.left_stick_y; // Remember, this is reversed!
            double x = -gamepad2.left_stick_x * 1.1; // Counteract imperfect strafing
            double rx = -gamepad2.right_stick_x;
            
            
            Arm_Left.setPower((gamepad1.left_stick_y + gamepad1.left_stick_y));
            Arm_Right.setPower((gamepad1.left_stick_y + gamepad1.left_stick_y));
            
            
            if (gamepad1.b) {
            ServoPosition += ServoSpeed;
            }
            if (gamepad1.x) {
            ServoPosition += -ServoSpeed;
            }
      // Keep Servo position in valid range
      ServoPosition = Math.min(Math.max(ServoPosition, 0), 1);
      Servo_Left.setPosition(ServoPosition);
      Servo_Right.setDirection(Servo.Direction.REVERSE);
      Servo_Right.setPosition(ServoPosition);
      sleep(20);
            
            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio, but only when
            // at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;
            
            maxSpeed[0] = frontLeftPower;
            maxSpeed[1] = frontRightPower;
            maxSpeed[2] = backLeftPower;
            maxSpeed[3] = backRightPower;
            
            if (gamepad2.a|| gamepad2.b || gamepad2.x || gamepad2.y){
                gears = 0.75;
            }
            
            
            // there might be an issue where the wheelspeeds don't update at the same time, which causes issues
            for(int i = 0; i < currentSpeed.length; i++){
                if(gamepad2.left_bumper){
                if(rx == 0){
                if(maxSpeed[i] == 0){
                    // if (stopped[i] > 0){
                    // if (stopped [i] == 100){
                    // currentSpeed[i] *= -1;
                    
                    // }
                    // stopped[i] --;
                    // } else {
                        currentSpeed[i] = 0;
                    // }
                } else
                if(maxSpeed[i] > 0){
                    if(currentSpeed[i] < maxSpeed[i]){
                        currentSpeed[i] += speedIncrease;
                    } 
                    if(currentSpeed[i] > maxSpeed[i]){
                        currentSpeed[i] = maxSpeed[i];
                    }
                }else if (maxSpeed[i] < 0){
                    if(currentSpeed[i] > maxSpeed[i]){
                      currentSpeed[i] -= speedIncrease;
                    } 
                    if(currentSpeed[i] < maxSpeed[i]){
                      currentSpeed[i] = maxSpeed[i];    
                    }
                }
                } else {
                    currentSpeed[i] = maxSpeed[i];
                }
                } else {
                    currentSpeed[i] = maxSpeed[i];
                }
            }
            
            
            if (cooldown == 0){
            if(gamepad2.right_trigger > 0.25 && gears <= 1){
                gears += 0.1;
                cooldown = 10;
            }
            if(gamepad2.left_trigger > 0.25 && gears - 0.05 > 0){
                gears -= 0.1;
                cooldown = 10;
            }
            
            } else {
                cooldown --;
            }
            
            motorFrontLeft.setPower(currentSpeed[0]*gears);
            motorBackLeft.setPower(currentSpeed[2]*gears);
            motorFrontRight.setPower(currentSpeed[1]*gears);
            motorBackRight.setPower(currentSpeed[3]*gears);
            // }
        // eerst 695
        //  if(gamepad1.dpad_up){
        //  Arm_Left.setTargetPosition(1885);
        //  Arm_Right.setTargetPosition(1885);
        //  Arm_Left.setPower(0.5);
        //  Arm_Right.setPower(0.5);
        //  Arm_Left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //  Arm_Right.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        
        //  }
        //  if(gamepad1.dpad_down){
        //  Arm_Left.setTargetPosition(0);
        //  Arm_Right.setTargetPosition(0);
        //  Arm_Left.setPower(0.1);
        //  Arm_Right.setPower(0.1);
        //  Arm_Left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //  Arm_Right.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //  }
        //  //eerst 500
        //  if(gamepad1.dpad_left){
        //  Arm_Left.setTargetPosition(1400);
        //  Arm_Right.setTargetPosition(1400);
        //  Arm_Left.setPower(0.5);
        //  Arm_Right.setPower(0.5);
        //  Arm_Left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        // Arm_Right.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        
        //  }
        //  // eerst 300
        //  if(gamepad1.dpad_right){
        //  Arm_Left.setTargetPosition(900);
        //  Arm_Right.setTargetPosition(900);
        //  Arm_Left.setPower(0.5);
        //  Arm_Right.setPower(0.5);
        //  Arm_Left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //  Arm_Right.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        
        //  }
        
        
        // if(Arm_Right.getCurrentPosition() != Arm_Right.getTargetPosition()){
          
        // }
        
        // the problem might be that the arm is never busy
        // or that the servos are VERY hot
        // or something else
        /*
            if(Arm_Right.getCurrentPosition() > Arm_Right.getTargetPosition()){
            Arm_Right.setPower(-0.8);
            Arm_Left.setPower(-0.8);
          }
          if(Arm_Right.getCurrentPosition() < Arm_Right.getTargetPosition()){
            Arm_Right.setPower(1);
            Arm_Left.setPower(1);
          }
          */
          
        telemetry.addData("Servo", ServoPosition);
        telemetry.addData("Right Trigger", gamepad2.right_trigger);
        telemetry.addData("Left Trigger", gamepad2.left_trigger);
        telemetry.addData("target position", Arm_Right.getTargetPosition());
        telemetry.addData("current position", Arm_Right.getCurrentPosition());
        telemetry.addData("current power", Arm_Right.getPower());
        telemetry.addData("Is the servo busy?", Arm_Right.isBusy());
        telemetry.addData("speed", currentSpeed[0]);
        telemetry.update();
      
    }
  }
}
