//finish the PLACEONLINE state

package org.firstinspires.ftc.teamcode;

import android.graphics.Color;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import android.app.Activity;
import com.qualcomm.robotcore.hardware.DcMotor;
import android.graphics.Color;
import android.view.View;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.SwitchableLight;
import com.qualcomm.robotcore.hardware.IMU;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import android.util.Size;

@Autonomous
public class AutonoomBlauw extends LinearOpMode {

  private DcMotor motorFrontRight;
  private DcMotor motorBackRight;
  private DcMotor motorFrontLeft;
  private DcMotor motorBackLeft;
  private DistanceSensor distance;
  private Servo Pols;
  private Servo FingerLeft;
  private Servo FingerRight;
  //private DcMotor ArmLeft;
  private DcMotor ArmRight;
    
    
    YawPitchRollAngles orientation;
    AngularVelocity angularVelocity;
    
    double leftopen = 0.8;
    double rightclosed = 0.93;
    double leftclosed = 0.;
    double rightopen = 0.4;
    int accuracy = 10;

 
  public class Pos {
    // 125 = 25cm 
    // 1 mat = 60 cm
    // 1 mat = 300
    int fl = 0;
    int fr = 0;
    int bl = 0;
    int br = 0;
    int arm = 0;
    
    public void forward(int afstand){
      this.fl += afstand;
      this.fr += afstand;
      this.bl += afstand;
      this.br += afstand;
    }
    
    public void backward(int afstand){
      this.fl -= afstand;
      this.fr -= afstand;
      this.bl -= afstand;
      this.br -= afstand;
    }
    
    public void left(int afstand){
      this.fl -= afstand;
      this.fr += afstand;
      this.bl += afstand;
      this.br -= afstand;
    }
    
    public void right(int afstand){
      this.fl += afstand;
      this.fr -= afstand;
      this.bl -= afstand;
      this.br += afstand;
    }
    
    public void turnLeft(int afstand){
      this.fl -= afstand;
      this.fr += afstand;
      this.bl -= afstand;
      this.br += afstand;
    }
    
    public void turnRight(int afstand){
      this.fl += afstand;
      this.fr -= afstand;
      this.bl += afstand;
      this.br -= afstand;
    }
    
    public void close(boolean left, boolean right) {
      if(left){
      FingerLeft.setPosition(leftclosed);
      }
      if (right) {
      FingerRight.setPosition(rightclosed);
      }
    }
    
    public void open(boolean left, boolean right) {
      if(left){
      FingerLeft.setPosition(leftopen);
      }
      if (right) {
      FingerRight.setPosition(rightopen);
      }
    }
    
    public void armUp(int afstand) {
      this.arm += afstand;
    }
    public void armDown(int afstand) {
      this.arm -= afstand;
    }
    
    public void wrist () {
      Pols.setPosition(0.45);
    }
    
    public boolean scan() {
      if (distance.getDistance(DistanceUnit.CM) < 30) {
        telemetry.addData("Dist: ", distance.getDistance(DistanceUnit.CM));
        return true;
      } else {
        return false;
      }
    }
    
    public boolean busy() {
      //if(ArmRight.isBusy() || motorFrontLeft.isBusy() || motorFrontRight.isBusy() || motorBackLeft.isBusy() || motorBackRight.isBusy()){
       if(Math.abs(ArmRight.getCurrentPosition() - ArmRight.getTargetPosition()) > accuracy || Math.abs(motorFrontRight.getCurrentPosition()-motorFrontRight.getTargetPosition()) > accuracy || Math.abs(motorFrontLeft.getCurrentPosition()-motorFrontLeft.getTargetPosition()) > accuracy || Math.abs(motorBackRight.getCurrentPosition()-motorBackRight.getTargetPosition()) > accuracy || Math.abs(motorBackLeft.getCurrentPosition()-motorBackLeft.getTargetPosition()) > accuracy) {
        return true;
      } else {
        return false;
      }
    }
    
    public void drive() {
      telemetry.addData("Target: ", motorFrontLeft.getTargetPosition());
      telemetry.update();
      motorFrontLeft.setTargetPosition(this.fl);
      motorFrontRight.setTargetPosition(this.fr);
      motorBackLeft.setTargetPosition(this.bl);
      motorBackRight.setTargetPosition(this.br);
      ArmRight.setTargetPosition(this.arm);
      
      motorFrontLeft.setPower(0.15);
      motorFrontRight.setPower(0.15);
      motorBackLeft.setPower(0.15);
      motorBackRight.setPower(0.15);
      ArmRight.setPower(0.5);
      
      
      motorFrontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
      motorFrontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
      motorBackLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
      motorBackRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
      ArmRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
  }

  /**
   * This function is executed when this Op Mode is selected from the Driver Station.
   */
  @Override
  public void runOpMode() {
    // AprilTagProcessor tagProcessor = new AprilTagProcessor.Builder()
    //         .setDrawAxes(true)
    //         .setDrawCubeProjection(true)
    //         .setDrawTagID(true)
    //         .setDrawTagOutline(true)
    //         .build();
            
    //     VisionPortal visionPortal = new VisionPortal.Builder()
    //         .addProcessor(tagProcessor)
    //         .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
    //         .setCameraResolution(new Size(640, 480))
    //         .build();
    
    motorFrontRight = hardwareMap.get(DcMotor.class, "motorFrontRight");
    motorBackRight = hardwareMap.get(DcMotor.class, "motorBackRight");
    motorFrontLeft = hardwareMap.get(DcMotor.class, "motorFrontLeft");
    motorBackLeft = hardwareMap.get(DcMotor.class, "motorBackLeft");
    distance = hardwareMap.get(DistanceSensor.class, "distance");
    //imu_IMU = hardwareMap.get(IMU.class, "imu");
    Pols = hardwareMap.get(Servo.class, "Pols");
    // Pols = hardwareMap.crservo.get("Pols");
    FingerLeft = hardwareMap.get(Servo.class, "FingerLeft");
    FingerRight = hardwareMap.get(Servo.class, "FingerRight");
    //ArmLeft = hardwareMap.get(DcMotor.class, "ArmLeft");
    ArmRight = hardwareMap.get(DcMotor.class, "ArmRight");

    // This op mode demonstrates the color and distance features of the REV sensor.
    motorFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    motorBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    motorBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    motorFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    //ArmLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    ArmRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    
    motorFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    motorFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    motorBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    motorBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    //ArmLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    ArmRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    
    FingerRight.setDirection(Servo.Direction.REVERSE);
    Pols.setDirection(Servo.Direction.REVERSE);
    motorFrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
    motorBackLeft.setDirection(DcMotorSimple.Direction.REVERSE);
    
    final int DRIVETOCONE = 0;
    final int SCANFOR2 = 1;
    final int SCANFOR1 = 2;
    final int SCANFOR3 = 3;
    final int MOVETOZONE1 = 4;
    final int MOVETOZONE3 = 5;
    final int MOVETOZONE2 = 6;
    final int DONE = 7;
    final int CAM = 8;
    final int PLACEONBACKBOARD = 9;
    final int PLACEONLINE = 10;
    final int RETURNFROM1 = 11;
    final int RETURNFROM2 = 12;
    final int RETURNFROM3 = 13;
    final int TURNTOLINE = 14;
    final int DRIVETOBACKBOARD = 15;
    final int GIVEUP = 16;
    final int ATCORRECTION = 17;
    final int CORRECTIONFROM1 = 18;
    final int AWAY = 19;
    final int PARK = 20;
    
    int state = DRIVETOCONE;
    
    int zone = 0;
    int tried = 2;
    int cameraFail = 10;
    

    Pos pos = new Pos();
    
    AprilTagProcessor tagProcessor = new AprilTagProcessor.Builder()
            .setDrawAxes(true)
            .setDrawCubeProjection(true)
            .setDrawTagID(true)
            .setDrawTagOutline(true)
            .build();
            
        VisionPortal visionPortal = new VisionPortal.Builder()
            .addProcessor(tagProcessor)
            .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
            .setCameraResolution(new Size(640, 480))
            .build();
    
    //DRIVE DOES NOT NEED TO BE CALLED EVERY LOOP!!!
    pos.close(true, true);
    pos.armUp(1300);
    pos.forward(450);
    //pos.turnLeft(223);
    //pos.drive();

    waitForStart();
    if (opModeIsActive()) {
      // Put run blocks here.
      while (opModeIsActive()) {
        
        // telemetry.addData("Yaw (Z)", JavaUtil.formatNumber(orientation.getYaw(AngleUnit.DEGREES), 2));
        // telemetry.addData("Pitch (X)", JavaUtil.formatNumber(orientation.getPitch(AngleUnit.DEGREES), 2));
        // telemetry.addData("Roll (Y)", JavaUtil.formatNumber(orientation.getRoll(AngleUnit.DEGREES), 2));
        // telemetry.update();
        switch(state) {
          case DRIVETOCONE:
            // if(!pos.busy()){
            //   pos.forward(1800);
            // }
            
            pos.drive();
            
            if(!pos.busy()){
              state = SCANFOR2;
            }
          break;
        
        case SCANFOR3:
          if(pos.scan()) {
            zone = 3;
            //pos.turnRight(223);
            state = TURNTOLINE;
          } else {
            //state = DONE;
            telemetry.addData("FAILED", state);
            telemetry.addData("Afstand: ", distance.getDistance(DistanceUnit.CM));
            telemetry.update();
            pos.turnLeft(225);
            //backward(20);
            state = MOVETOZONE2;
          }
        break;
        
        case MOVETOZONE2:
          // if(!pos.busy()){
          //   pos.turnLeft(900);
          // }
          
          pos.drive();
          
          if(!pos.busy()){
            // pos.backward(25);
            // pos.drive();
            state = SCANFOR2;
          }
        break;
        
        case SCANFOR2: // or tried = 0
          if(pos.scan()) {
            zone = 2;
            //pos.turnRight(223);
            state = TURNTOLINE;
          } else {
            telemetry.addData("FAILED", state);
            telemetry.addData("Afstand: ", distance.getDistance(DistanceUnit.CM));
            telemetry.update();
            pos.turnLeft(233);
            pos.right(80);
            state = MOVETOZONE1;
          }
        break;
        
        case MOVETOZONE1:
          // if(!pos.busy()){
          //   pos.turnLeft(900);
          // }
          pos.drive();
          
          if(!pos.busy()){
            //pos.right(50);
            pos.forward(100);
            pos.left(20);
            pos.drive();
            state = SCANFOR1;
          }
        break;
        
        case SCANFOR1:
          if(!pos.busy()){
          if(pos.scan()) {
            zone = 1;
            //pos.turnRight(223);
            state = TURNTOLINE;
          } else {
            telemetry.addData("FAILED", state);
            telemetry.addData("Afstand: ", distance.getDistance(DistanceUnit.CM));
            telemetry.update();
            //state = DONE;
            pos.turnLeft(223*2+50);
            state = MOVETOZONE3;
            tried --;
          }
          }
        break;
        
        case MOVETOZONE3:
          // if(!pos.busy()){
          //   pos.turnLeft(1750);
          
          // }
          
          pos.drive();
          
          if(!pos.busy()){
            pos.left(80);
            zone = 3;
            state = TURNTOLINE;
          }
        break;
        
        case TURNTOLINE:
          // if(!pos.busy()) {
          //   pos.turnRight(900);
          // }
          pos.drive();
          if (!pos.busy()) {
            //pos.armDown(700);
            //pos.wrist();
            //pos.backward(75);
            switch (zone) {
              case 1: 
                pos.forward(50);
                pos.left(50);
                break;
              case 2: 
                pos.forward(45);
                break;
              case 3: 
                //pos.forward(100);
                break;
            }
            //pos.drive();
            state = PLACEONLINE;
          }
          break;
          
        case PLACEONLINE:
          // if(!pos.busy()) {
          //   pos.armDown(800);
          // }
          pos.drive();
          //sleep(10);
          if(!pos.busy()){
            pos.open(false, true);
            //pos.armUp(800);
            pos.backward(100);
            switch (zone){
                case 1:
                  state = RETURNFROM1;
                  break;
                case 2:
                  state = RETURNFROM2;
                  break;
                case 3:
                  state = RETURNFROM3;
                  break;
              }
          }
          break;
          
        case RETURNFROM3:
          pos.drive();
          if(!pos.busy()) {
            pos.turnRight(223*2+50);
            state = DRIVETOBACKBOARD;
          }
          break;
          
        case RETURNFROM1:
          // if(!pos.busy()) {
          //   pos.turnRight(1800);
          // }
          pos.drive();
          if(!pos.busy()) {
            
            pos.left(200);
            state = CORRECTIONFROM1;
          }
          break;
          
        case CORRECTIONFROM1:
          pos.drive();
          if(!pos.busy()) {
            pos.left(75);
            state = DRIVETOBACKBOARD;
          }
          
        case RETURNFROM2:
            // if(!pos.busy()) {
            //   pos.turnRight(900);
            // }
            pos.drive();
            if(!pos.busy()) {
              pos.turnLeft(223);
              state = DRIVETOBACKBOARD;
            }
            break;
            
        case DRIVETOBACKBOARD:
          if(!pos.busy()) {
            boolean wrongTag = true;
          if (tagProcessor.getDetections().size() > 0) {
            for (int i = 0; i < tagProcessor.getDetections().size(); i++) {
              telemetry.addData("tag", tagProcessor.getDetections().get(i).id);
                if (tagProcessor.getDetections().get(i).id == zone) {
            state = CAM;
            wrongTag = false;
            }
            }
          } 
          if (wrongTag) {
            pos.forward(100);
            cameraFail--;
          }
          }
          pos.drive();
          if (cameraFail == 0) {
            pos.right(1800/4);
            pos.forward(1800/4);
            state = GIVEUP;
          }
          break;
        
        case DONE:
          telemetry.addData("Status: ", state);
          telemetry.addData("Afstand: ", distance.getDistance(DistanceUnit.CM));
          telemetry.update();
        break;
        
        case CAM:
          telemetry.addData("CAM", state);
          telemetry.addData("Amount: ", tagProcessor.getDetections().size());
          if (tagProcessor.getDetections().size() > 0) {
              for (int i = 0; i < tagProcessor.getDetections().size(); i++) {
                if (tagProcessor.getDetections().get(i).id == zone) {
                AprilTagDetection tag = tagProcessor.getDetections().get(i);
                
                telemetry.addData("x", tag.ftcPose.x);
                telemetry.addData("y", tag.ftcPose.y);
                telemetry.addData("z", tag.ftcPose.z);
                telemetry.addData("roll", tag.ftcPose.roll);
                telemetry.addData("pitch", tag.ftcPose.pitch);
                telemetry.addData("yaw", tag.ftcPose.yaw);
                telemetry.addData("Dist:", distance.getDistance(DistanceUnit.CM));
                telemetry.update();
                // double myTagPoseX = tag.ftcPose.x;
                // double myTagPoseY = tag.ftcPose.y;
                // double myTagPoseZ = tag.ftcPose.z;
                // double myTagPosePitch = tag.ftcPose.pitch;
                // double myTagPoseRoll = tag.ftcPose.roll;
                // double myTagPoseYaw = tag.ftcPose.yaw;
                if (tag.ftcPose.x > 2.5) {
                  pos.right(5);
                }
                if (tag.ftcPose.x < -2.5) {
                  pos.left(5);
                }
                if (tag.ftcPose.yaw >3) {
                  pos.turnLeft(3);
                }
                if (tag.ftcPose.yaw >-3) {
                  pos.turnRight(3);
                }
                pos.forward(20);
                pos.drive();
                
                }
            }
          }
          if(distance.getDistance(DistanceUnit.CM) < 15) {
                  pos.right(140);
                  pos.wrist();
                  pos.forward(30);
                  state = ATCORRECTION;
                }
        break;
        
        case ATCORRECTION:
          pos.drive();
          if(!pos.busy()){
            pos.armDown(1300);
            state = PLACEONBACKBOARD;
          }
        
        case PLACEONBACKBOARD:
          pos.drive();
          if(!pos.busy()) {
          //pos.wrist();
          pos.open(true, true);
          pos.backward(10);
          state = AWAY;
          }
          break;
          
        case AWAY:
          pos.drive();
          if(!pos.busy()) {
            pos.left(300);
            state = PARK;
          }
          break;
          
          case PARK:
            pos.drive();
            break;
            
          
        case GIVEUP:
          pos.drive();
          break;
        
      }
      telemetry.addData("State: ", state);
      telemetry.addData("BR current ", motorBackRight.getCurrentPosition());
      telemetry.addData("BL current ", motorBackLeft.getCurrentPosition());
              telemetry.addData("Dist: ", distance.getDistance(DistanceUnit.CM));

      }
    }
}
}
