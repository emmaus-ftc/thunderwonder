//add a function to change speeds
// do phase 2

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
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import org.firstinspires.ftc.robotcore.external.JavaUtil;


@Autonomous
public class MultipleSensors extends LinearOpMode {

  private DcMotor motorFrontRight;
  private DcMotor motorBackRight;
  private DcMotor motorFrontLeft;
  private DcMotor motorBackLeft;
  private DistanceSensor distance;
  private ColorSensor colorLeft;
  private ColorSensor colorRight;
  private IMU imu;
  
  private DcMotor Pols;
  private Servo FingerLeft;
  private Servo FingerRight;
  //private DcMotor ArmLeft;
  private DcMotor ArmRight;
    
    
    YawPitchRollAngles orientation;
    AngularVelocity angularVelocity;
    
    double leftopen = 0.8;
    double rightclosed = 0.98;
    double leftclosed = 0.0;
    double rightopen = 0.4;
    int accuracy = 20;
    double adjust = 0.75;

 
  public class Pos {
    // 125 = 25cm 
    // 1 mat = 60 cm
    // 1 mat = 300
    int fl = 0;
    int fr = 0;
    int bl = 0;
    int br = 0;
    int arm = 0;
    
    public void setPowerLR(double powerL, double powerR) {
      motorFrontLeft.setPower(powerL);
      motorFrontRight.setPower(powerR);
      motorBackLeft.setPower(powerL);
      motorBackRight.setPower(powerR);
      ArmRight.setPower(0.5);
    }
    
    public void forward(int afstand){
      afstand*= adjust;
      this.fl += afstand;
      this.fr += afstand;
      this.bl += afstand;
      this.br += afstand;
    }
    
    public void backward(int afstand){
      afstand*= adjust;
      this.fl -= afstand;
      this.fr -= afstand;
      this.bl -= afstand;
      this.br -= afstand;
    }
    
    public void left(int afstand){
      afstand*= adjust;
      this.fl -= afstand;
      this.fr += afstand;
      this.bl += afstand;
      this.br -= afstand;
    }
    
    public void right(int afstand){
      afstand*= adjust;
      this.fl += afstand;
      this.fr -= afstand;
      this.bl -= afstand;
      this.br += afstand;
    }
    
    public void turnLeft(int afstand){
      afstand*= adjust;
      this.fl -= afstand;
      this.fr += afstand;
      this.bl -= afstand;
      this.br += afstand;
    }
    
    public void turnRight(int afstand){
      afstand*= adjust;
      this.fl += afstand;
      this.fr -= afstand;
      this.bl += afstand;
      this.br -= afstand;
    }
    
    public void angleCheck(double angle) {
      orientation = imu.getRobotYawPitchRollAngles();
      if (Math.abs(Double.parseDouble(JavaUtil.formatNumber(orientation.getYaw(AngleUnit.DEGREES),2)) - angle) > 10){
          //if(!this.busy()){
          // telemetry.addData("?", Double.parseDouble(JavaUtil.formatNumber(orientation.getYaw(AngleUnit.DEGREES),2)));
          //telemetry.update();
        if(Double.parseDouble(JavaUtil.formatNumber(orientation.getYaw(AngleUnit.DEGREES),2)) > angle) {
          //this.turnRight(Math.round((float)Math.abs(Double.parseDouble(JavaUtil.formatNumber(orientation.getYaw(AngleUnit.DEGREES),2)) - angle))*3);
          this.setPowerLR(1, 1);
          this.turnRight(5);
          // telemetry.addData("Right!", this.fr);
        } else {
          //this.turnLeft(Math.round((float)Math.abs(Double.parseDouble(JavaUtil.formatNumber(orientation.getYaw(AngleUnit.DEGREES),2)) -  angle))*3);
          this.setPowerLR(1, 1);
          this.turnLeft(5);
      }
      this.drive();
      // telemetry.addData("Yaw (Z)", JavaUtil.formatNumber(orientation.getYaw(AngleUnit.DEGREES), 2));
      // telemetry.update();
        this.angleCheck(angle);

      }
      telemetry.addData("Done!", JavaUtil.formatNumber(orientation.getYaw(AngleUnit.DEGREES), 2));
      //telemetry.update();
      this.setPowerLR(0.08, 0.08);
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
    //telemetry.addData("Pols", Pols.getCurrentPosition());
    //telemetry.update();
    this.wrist();
    }
    
    public java.lang.String scan() {
      // if (distance.getDistance(DistanceUnit.CM) < 30) {
      //   telemetry.addData("Dist Froward: ", distance.getDistance(DistanceUnit.CM));
      //   return "f";
      // }
      if (((DistanceSensor)colorRight).getDistance(DistanceUnit.CM) < 5) {
        //telemetry.addData("Dist Froward: ", distance.getDistance(DistanceUnit.CM));
        return "f";
      }else 
      if (((DistanceSensor)colorLeft).getDistance(DistanceUnit.CM) < 8) {
        //telemetry.addData("Dist Left: ", ((DistanceSensor)colorLeft).getDistance(DistanceUnit.CM));
        return "l";
      } else 
      if (distance.getDistance(DistanceUnit.CM) < 30) {
        //telemetry.addData("Dist Right: ", ((DistanceSensor)colorRight).getDistance(DistanceUnit.CM));
        return "r";
      } else {
        return "not found";
      }
        
    }
    
    public boolean scanRight() {
      if (distance.getDistance(DistanceUnit.CM) < 30) {
        telemetry.addData("Dist Right: ", distance.getDistance(DistanceUnit.CM));
        return true;
      } else {
        return false;
      }
    }
    
    public boolean scanLeft() {
      if (((DistanceSensor)colorLeft).getDistance(DistanceUnit.CM) < 10) {
        telemetry.addData("Dist Left: ", ((DistanceSensor)colorLeft).getDistance(DistanceUnit.CM));
        return true;
      } else {
        return false;
      }
    }
    
    public boolean scanForward() {
      if (((DistanceSensor)colorRight).getDistance(DistanceUnit.CM) < 10) {
        telemetry.addData("Dist Forward: ", ((DistanceSensor)colorRight).getDistance(DistanceUnit.CM));
        return true;
      } else {
        return false;
      }
    }
    
    public boolean busy() {
      //reset the targets.
      //if(ArmRight.isBusy() || motorFrontLeft.isBusy() || motorFrontRight.isBusy() || motorBackLeft.isBusy() || motorBackRight.isBusy()){
       if(Math.abs(ArmRight.getCurrentPosition() - ArmRight.getTargetPosition()) > accuracy || Math.abs(motorFrontRight.getCurrentPosition()-motorFrontRight.getTargetPosition()) > accuracy || Math.abs(motorFrontLeft.getCurrentPosition()-motorFrontLeft.getTargetPosition()) > accuracy || Math.abs(motorBackRight.getCurrentPosition()-motorBackRight.getTargetPosition()) > accuracy || Math.abs(motorBackLeft.getCurrentPosition()-motorBackLeft.getTargetPosition()) > accuracy) {
        return true;
      } else {
        this.fl = motorFrontLeft.getCurrentPosition();
        this.fr = motorFrontRight.getCurrentPosition();
        this.br = motorBackRight.getCurrentPosition();
        this.bl = motorBackLeft.getCurrentPosition();
        return false;
      }
    }
    
    public void drive() {
      //telemetry.addData("Target: ", motorFrontLeft.getTargetPosition());
      //telemetry.update();
      motorFrontLeft.setTargetPosition(this.fl);
      motorFrontRight.setTargetPosition(this.fr);
      motorBackLeft.setTargetPosition(this.bl);
      motorBackRight.setTargetPosition(this.br);
      ArmRight.setTargetPosition(this.arm);
      
      
      
      
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
    colorLeft = hardwareMap.get(ColorSensor.class, "ColorSensor");
    colorRight = hardwareMap.get(ColorSensor.class, "ColorSensor2");
    //imu_IMU = hardwareMap.get(IMU.class, "imu");
    Pols = hardwareMap.get(DcMotor.class, "Pols");
    // Pols = hardwareMap.crservo.get("Pols");
    FingerLeft = hardwareMap.get(Servo.class, "FingerLeft");
    FingerRight = hardwareMap.get(Servo.class, "FingerRight");
    //ArmLeft = hardwareMap.get(DcMotor.class, "ArmLeft");
    ArmRight = hardwareMap.get(DcMotor.class, "ArmRight");
     YawPitchRollAngles orientation;
    AngularVelocity angularVelocity;

    imu = hardwareMap.get(IMU.class, "imu");

    // This op mode demonstrates the color and distance features of the REV sensor.
    motorFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    motorBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    motorBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    motorFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    //ArmLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    ArmRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    Pols.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    
    motorFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    motorFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    motorBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    motorBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    //ArmLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    ArmRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    Pols.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    
    FingerRight.setDirection(Servo.Direction.REVERSE);
    //Pols.setDirection(Servo.Direction.REVERSE);
    motorFrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
    motorBackLeft.setDirection(DcMotorSimple.Direction.REVERSE);
    
    imu.initialize(new IMU.Parameters(new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.FORWARD, RevHubOrientationOnRobot.UsbFacingDirection.RIGHT)));
    imu.resetYaw();
    
    
    final int DRIVETOCONE = 0;
    final int SCAN = 1;
    final int DONE = 7;
    final int CAM = 8;
    final int PLACEONBACKBOARD = 9;
    final int RETURN = 10;
    final int TURNTOLINE = 14;
    final int DRIVETOBACKBOARD = 15;
    final int GIVEUP = 16;
    final int ATCORRECTION = 17;
    final int CORRECTIONFROM1 = 18;
    final int AWAY = 19;
    final int PARK = 20;
    final int MOVETOZONE1 = 21;
    final int MOVETOZONE3 = 22;
    final int CORRZONE3 = 23;
    final int CORRECTION = 24;
    final int PLACEONLINE = 25;
    
    int state = DRIVETOCONE;
    
    int zone = 0;
    int tried = 2;
    int cameraFail = 10;
    
    
    Pos pos = new Pos();
    
    boolean armd = true;
    
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
    pos.forward(550);
    ////pos.turnLeft(223);
    ////pos.drive();
    // pos.turnLeft(10);
    // pos.drive();
    // pos.angleCheck(90);
    // telemetry.addData("Pols", Pols.getCurrentPosition());
    // telemetry.update();
    // pos.wrist();
    
    pos.setPowerLR(0.08, 0.08);
    
    waitForStart();
    if (opModeIsActive()) {
      // Put run blocks here.
      while (opModeIsActive()) {
        orientation = imu.getRobotYawPitchRollAngles();
        angularVelocity = imu.getRobotAngularVelocity(AngleUnit.DEGREES);
        telemetry.addData("Yaw (Z)", JavaUtil.formatNumber(orientation.getYaw(AngleUnit.DEGREES), 2));
        
        // telemetry.addData("Yaw (Z)", JavaUtil.formatNumber(orientation.getYaw(AngleUnit.DEGREES), 2));
        // telemetry.addData("Pitch (X)", JavaUtil.formatNumber(orientation.getPitch(AngleUnit.DEGREES), 2));
        // telemetry.addData("Roll (Y)", JavaUtil.formatNumber(orientation.getRoll(AngleUnit.DEGREES), 2));
        // telemetry.update();
        switch(state) {
          case DRIVETOCONE:
            pos.drive();
            if(!pos.busy()) {
              state = SCAN;
            }
            break;
          
          // move forward at zone 3 before he places
          // zone2 after placing, move back further
          // zone1 weird
          
          case SCAN:
              //pos.setPowerLR(0.75, 0.75);
            if(pos.scan() == "f") {
              zone = 2;
              state = PLACEONLINE;
            } else if (pos.scan() == "r"){
              zone = 3;
              pos.forward(65);
              // //pos.drive();
              // state = MOVETOZONE3;
              //pos.angleCheck(-90);
              state = MOVETOZONE3;
            } else {
              zone = 1;
              pos.angleCheck(98);
              //pos.drive();
              state = CORRECTION;
            }
            break;
            
          case CORRECTION:
            pos.drive();
            if(!pos.busy()) {
            if(zone == 1){
              pos.forward(80);
            }
            state = PLACEONLINE;
            }
            
          case MOVETOZONE3:
            pos.drive();
            if(!pos.busy()) {
            pos.angleCheck(-80);
              //pos.drive();
              state = CORRZONE3;
            }
            break;
          
          case CORRZONE3:
            pos.drive();
            if(!pos.busy()) {
              pos.forward(75);
              state = PLACEONLINE;
            }
            
          // case MOVETOZONE3:
          //   pos.drive();
          //   if(pos.scan() == "f") {
          //     zone = 2;
          //     state = TURNTOLINE;
          //   } else if (pos.scan() == "l") {
          //     zone = 1;
          //     pos.angleCheck(90);
          //     //pos.drive();
          //     state = TURNTOLINE;
          //   } else if (pos.scan() == "r"){
          //     zone = 3;
          //     pos.angleCheck(-90);
          //     //pos.drive();
          //     state = TURNTOLINE;
          //   } 
          //   break;
            
          
          case PLACEONLINE:
            pos.drive();
            if(!pos.busy()) {
              pos.open(false, true);
              state = TURNTOLINE;
            }
            break;
            
            
          case TURNTOLINE:
            pos.drive();
            if(!pos.busy() && FingerRight.getPosition() < 0.45){
              // sleep(1000);
              if(zone == 1) {
                //accuracy = 50;
              pos.backward(100);
              } else if(zone == 2) {
                pos.backward(150);
              }
              pos.drive();
              state = RETURN;
            }
            break;
            
          
          
          case RETURN:
            if(!pos.busy()){
              //accuracy = 10;
              switch(zone){
                case 1:
                  pos.angleCheck(-90);
                  pos.drive();
                  state = DRIVETOBACKBOARD;
                  break;
                case 2:
                  pos.angleCheck(-90);
                  pos.drive();
                  state = DRIVETOBACKBOARD;
                  break;
                case 3:
                  //pos.right(100);
                  pos.drive();
                  state = DRIVETOBACKBOARD;
                  break;
              } 
            }
            break;
            
          case DRIVETOBACKBOARD:
            if(!pos.busy()) {
              boolean wrongTag = true;
            if (tagProcessor.getDetections().size() > 0) {
              for (int i = 0; i < tagProcessor.getDetections().size(); i++) {
                telemetry.addData("tag", tagProcessor.getDetections().get(i).id);
                  if (tagProcessor.getDetections().get(i).id == zone || tagProcessor.getDetections().get(i).id - 3 == zone) {
                    state = CAM;
                    wrongTag = false;
                  }
              }
            } 
            for (int i = 0; i < tagProcessor.getDetections().size(); i++) {
            if (wrongTag && (tagProcessor.getDetections().get(i).id == 3 || tagProcessor.getDetections().get(i).id - 3 == 3)) {
              pos.left(50);
            }
            }
            // if (wrongTag) {
            //   pos.forward(100);
            //   cameraFail--;
            // }
            }
            pos.drive();
            if (cameraFail == 0) {
              pos.right(1800/4);
              pos.forward(1800/4);
              state = GIVEUP;
            }
            break;
            
          case CAM:
          pos.setPowerLR(0.15, 0.148);
          if(armd) {
            pos.armDown(500);
            armd = false;
          }
          telemetry.addData("CAM", state);
          telemetry.addData("Amount: ", tagProcessor.getDetections().size());
          if (tagProcessor.getDetections().size() > 0) {
              for (int i = 0; i < tagProcessor.getDetections().size(); i++) {
                if (tagProcessor.getDetections().get(i).id == zone || tagProcessor.getDetections().get(i).id - 3 == zone) {
                AprilTagDetection tag = tagProcessor.getDetections().get(i);
                
                telemetry.addData("x", tag.ftcPose.x);
                telemetry.addData("y", tag.ftcPose.y);
                telemetry.addData("z", tag.ftcPose.z);
                telemetry.addData("roll", tag.ftcPose.roll);
                telemetry.addData("pitch", tag.ftcPose.pitch);
                telemetry.addData("yaw", tag.ftcPose.yaw);
                //telemetry.addData("size", tag.ftcPose.size);
                telemetry.addData("Dist:", distance.getDistance(DistanceUnit.CM));
                //telemetry.update();
                // double myTagPoseX = tag.ftcPose.x;
                // double myTagPoseY = tag.ftcPose.y;
                // double myTagPoseZ = tag.ftcPose.z;
                // double myTagPosePitch = tag.ftcPose.pitch;
                // double myTagPoseRoll = tag.ftcPose.roll;
                // double myTagPoseYaw = tag.ftcPose.yaw;
                if (tag.ftcPose.x > 3) {
                  pos.right(5);
                }
                if (tag.ftcPose.x < -3) {
                  pos.left(5);
                }
                // if (tag.ftcPose.yaw >3) {
                //   pos.turnLeft(3);
                // }
                // if (tag.ftcPose.yaw >-3) {
                //   pos.turnRight(3);
                // }
                pos.forward(20);
                pos.drive();
                // if(distance.getDistance(DistanceUnit.CM) < 20) {
                //   pos.right(140);
                //   pos.wrist();
                //   pos.forward(30);
                //   state = ATCORRECTION;
                // }
                }
            }
            
          }else {
            pos.right(140);
            state = ATCORRECTION;
          }
          
        break;
        
        case ATCORRECTION:
          pos.drive();
          if(!pos.busy()){
            //pos.armDown(800);
            state = PLACEONBACKBOARD;
          }
          break;
        
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
            pos.right(300);
            pos.drive();
            state = PARK;
          }
          break;
          
          case PARK:
            //pos.drive();
            break;
            
          
        case GIVEUP:
          pos.drive();
          break;
      }
      telemetry.addData("State: ", state);
      telemetry.addData("Servo: ", FingerRight.getPosition());
      telemetry.addData("BR current ", motorBackRight.getCurrentPosition());
      telemetry.addData("BL current ", motorBackLeft.getCurrentPosition());
      telemetry.addData("Dist Front: ", distance.getDistance(DistanceUnit.CM));
      telemetry.addData("Dist Left: ", ((DistanceSensor)colorLeft).getDistance(DistanceUnit.CM));
      telemetry.addData("Dist Right: ", ((DistanceSensor)colorRight).getDistance(DistanceUnit.CM));
      telemetry.update();

      }
    }
}
}
