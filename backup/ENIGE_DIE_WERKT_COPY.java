package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.robotcore.external.JavaUtil;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp

public class ENIGE_DIE_WERKT_COPY_Copy extends LinearOpMode {
    private Blinker control_Hub;
    private Gyroscope imu;
    private DcMotor motorFrontLeft;
    private DcMotor motorBackLeft;
    private DcMotor motorFrontRight;
    private DcMotor motorBackRight;
    private Servo Pols;
    private Servo FingerLeft;
    private Servo FingerRight;
    //private DcMotor ArmLeft;
    private DcMotor ArmRight;
    private Servo Plane;
    
    
    // the order is frontleft. frontright, backleft, backright
    double[] currentSpeed = {0, 0, 0, 0};
    double[] maxSpeed = {0, 0, 0, 0};
    int[] stopped = {100, 100, 100, 100};
    double speedIncrease = 0.05; // i don't know if this is to much or to little
    
    double gears = 0.5;
    int cooldown = 10;
    
    //0.45 en 1
    double polsPos = 0.75;
    double polsSpeed = 0.001;
    
    boolean hanging = false;
    int timer = 1000;
    
    double leftopen = 0.4;
    double rightopen = 0.97;
    double leftclosed = 0.15;
    double rightclosed = 0.65;
    

    
    @Override
    public void runOpMode() throws InterruptedException {
        // Declare our motors
        // Make sure your ID's match your configuration
    // double ServoPosition;
    // double ServoSpeed;
        DcMotor motorFrontLeft= hardwareMap.dcMotor.get("motorFrontLeft");
        DcMotor motorBackLeft = hardwareMap.dcMotor.get("motorBackLeft");
        DcMotor motorFrontRight = hardwareMap.dcMotor.get("motorFrontRight");
        DcMotor motorBackRight = hardwareMap.dcMotor.get("motorBackRight");
        Pols = hardwareMap.get(Servo.class, "Pols");
        FingerLeft = hardwareMap.get(Servo.class, "FingerLeft");
        FingerRight = hardwareMap.get(Servo.class, "FingerRight");
        //ArmLeft = hardwareMap.get(DcMotor.class, "ArmLeft");
        ArmRight = hardwareMap.get(DcMotor.class, "ArmRight");
        Plane = hardwareMap.get(Servo.class, "Plane");

        // Reverse the right side motors
        // Reverse left motors if you are using NeveRests
        FingerRight.setDirection(Servo.Direction.REVERSE);
        Pols.setDirection(Servo.Direction.REVERSE);
        motorBackRight.setDirection(DcMotorSimple.Direction.REVERSE);
        motorFrontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        //ArmLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        //Arm.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBackLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorBackRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorFrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorFrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //ArmLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        ArmRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        // Arm_Left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        // Arm_Right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        

        waitForStart();
            
        while (opModeIsActive()) {
            double y = gamepad2.left_stick_y; // Remember, this is reversed!
            double x = -gamepad2.left_stick_x * 1; // Counteract imperfect strafing 
            double rx = -gamepad2.right_stick_x;
            //double mod = 0.9;
           
            
            // og ArmRight.setPower(-(gamepad1.left_stick_y + gamepad1.left_stick_y)*0.5*0.6);
            //ArmLeft.setPower(-(gamepad1.left_stick_y + gamepad1.left_stick_y)*mod);
            
            //Arm_Right.setPower((gamepad1.left_stick_y + gamepad1.left_stick_y));
            
            
            if (gamepad1.a || gamepad1.x ||  gamepad1.b) {
                FingerLeft.setPosition(leftopen);
                FingerRight.setPosition(rightclosed);
            } else if (gamepad1.y) {
                FingerRight.setPosition(rightopen);
            }else { 
                FingerLeft.setPosition(leftclosed);
                FingerRight.setPosition(rightclosed);
            }
            
            
            
            if (gamepad1.right_trigger > 0.5) {
              polsPos = Range.clip(polsPos + polsSpeed,0 , 1);
              //polsPos = polsPos + polsSpeed;
               
               
            }
            if (gamepad1.left_trigger > 0.5) {
                polsPos = Range.clip(polsPos - polsSpeed, 0, 1);
                if(polsPos > 1) {
                    polsPos = 1;
                }
                if (polsPos < 0.45) {
                    polsPos = 0.45;
                }
            }
            Pols.setPosition(polsPos);
            
            if (gamepad1.right_stick_button) {
                Plane.setPosition(1);
            }
            
            if (gamepad1.dpad_down) {
                polsPos = 0.642;
            }
            
            if (gamepad1.dpad_up) {
                if (timer <= 0) {
                if(hanging) {
                    hanging = false;
                } else {
                    hanging = true;
                }
                timer = 1000;
                }
            }
            timer --;
            if (hanging) {
                ArmRight.setPower(-0.6);
            } else {
                ArmRight.setPower(-(gamepad1.left_stick_y + gamepad1.left_stick_y)*0.5*0.6);

            }
            
            // if(gamepad1.right_trigger > 0.5) {
            //     Pols.setPower(0.25);
            // }
            // if(gamepad1.left_trigger > 0.5) {
            //     Pols.setPower(-0.25); 
            // } 
            
            
    //   // Keep Servo position in valid range
    //   ServoPosition = Math.min(Math.max(ServoPosition, 0), 1);
    //   FingerLeft.setPosition(ServoPosition);
    //   FingerRight.setDirection(Servo.Direction.REVERSE);
    //   FingerRight.setPosition(ServoPosition);
    //   sleep(20);
            
            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio, but only when
            // at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;
            
            maxSpeed[0] = frontLeftPower*gears;
            maxSpeed[1] = frontRightPower*gears;
            maxSpeed[2] = backLeftPower*gears;
            maxSpeed[3] = backRightPower*gears;
            
            if (gamepad2.a|| gamepad2.x){
                gears = 0.5;
            }
            if (gamepad2.y) {
                gears = 0.5 - 0.5/3*2;
            }
            if (gamepad2.b) {
                gears = 0.5 + 0.5/3;
            }
        
            //0.642
            
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
                gears += 0.5/3;
                cooldown = 1000;
            }
            if(gamepad2.left_trigger > 0.25 && gears - 0.05 > 0.5/3){
                gears -= 0.5/3;
                cooldown = 1000;
            }
            
            } else {
                cooldown --;
            }
            if(gears < 0.5/3) {
                gears = 0.5/3;
            }
            
            motorFrontLeft.setPower(currentSpeed[0]); //*gears);
            motorBackLeft.setPower(currentSpeed[2]);//*gears);
            motorFrontRight.setPower(currentSpeed[1]);//*gears);
            motorBackRight.setPower(currentSpeed[3]);//*gears);
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
          
        // telemetry.addData("Pols", Pols.getPosition());
        // telemetry.addData("Servo", Finger.getPosition());
        telemetry.addData("Right Trigger", gamepad2.right_trigger);
        telemetry.addData("Left Trigger", gamepad2.left_trigger);
        // telemetry.addData("target position", Arm.getTargetPosition());
        // telemetry.addData("current position", Arm.getCurrentPosition());
        telemetry.addData("current power", ArmRight.getPower());
        // telemetry.addData("Is the servo busy?", Arm.isBusy());
        telemetry.addData("speed", currentSpeed[0]);
        telemetry.addData("Pols: ", Pols.getPosition());
        telemetry.addData("Servo Links: ", FingerLeft.getPosition());
        telemetry.addData("Servo Rechts: ", FingerRight.getPosition());
        telemetry.update();
      
    }
  }
}
