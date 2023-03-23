package org.firstinspires.ftc.teamcode;

import android.graphics.Color;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import org.firstinspires.ftc.robotcore.external.JavaUtil;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous
public class FullColorSensor extends LinearOpMode {

  private DcMotor motorFrontRight;
  private DcMotor motorBackRight;
  private ColorSensor Colorsensor_REV_ColorRangeSensor;
  private DistanceSensor Colorsensor_DistanceSensor;
  private DcMotor motorFrontLeft;
  private DcMotor motorBackLeft;
  private Servo Servo_Right;
  private Servo Servo_Left;
  private DcMotor Arm_Left;
  private DcMotor Arm_Right;

  /**
   * This function is executed when this Op Mode is selected from the Driver Station.
   */
  @Override
  public void runOpMode() {
    float hue;
    boolean moving;
    int scanned;
    int timer = 0;
    boolean scanning;
    int output;
    int gain;
    NormalizedRGBA normalizedColors;
    int color;
    float saturation;
    float value;
    boolean armone = true;
    
    double servoPosition = 0.5;
    double servoSpeed = 0.3;

    motorFrontRight = hardwareMap.get(DcMotor.class, "motorFrontRight");
    motorBackRight = hardwareMap.get(DcMotor.class, "motorBackRight");
    Colorsensor_REV_ColorRangeSensor = hardwareMap.get(ColorSensor.class, "Colorsensor");
    Colorsensor_DistanceSensor = hardwareMap.get(DistanceSensor.class, "Colorsensor");
    motorFrontLeft = hardwareMap.get(DcMotor.class, "motorFrontLeft");
    motorBackLeft = hardwareMap.get(DcMotor.class, "motorBackLeft");
    Servo_Right = hardwareMap.get(Servo.class, "Servo_Right");
    Servo_Left = hardwareMap.get(Servo.class, "Servo_Left");
    Arm_Left = hardwareMap.get(DcMotor.class, "Arm_Left");
    Arm_Right = hardwareMap.get(DcMotor.class, "Arm_Right");

    // This op mode demonstrates the color and distance features of the REV sensor.
    motorFrontRight.setDirection(DcMotorSimple.Direction.REVERSE);
    motorBackRight.setDirection(DcMotorSimple.Direction.REVERSE);
    Arm_Left.setDirection(DcMotorSimple.Direction.REVERSE);
    
    moving = true;
    scanned = 0;
    scanning = false;
    output = 0;
    gain = 2;
    telemetry.addData("Color Distance Example", "Press start to continue...");
    telemetry.update();
    waitForStart();
    if (opModeIsActive()) {
      // Put run blocks here.
      while (opModeIsActive()) {
        servoPosition = Math.min(Math.max(servoPosition, 0), 1);
        Servo_Right.setDirection(Servo.Direction.REVERSE);
        //for(int i = 0; i < 500; i++){
        if(armone){
        servoPosition += servoSpeed;
        Servo_Right.setPosition(servoPosition);
        Servo_Left.setPosition(servoPosition);
        sleep(900);
        Arm_Left.setPower(-1);
        Arm_Right.setPower(-1);
        sleep(200);
        Arm_Left.setPower(0);
        Arm_Right.setPower(0);
        sleep(200);
        armone = false;
        }
        // Display distance info.
        telemetry.addData("Dist to tgt (cm)", ((DistanceSensor) Colorsensor_REV_ColorRangeSensor).getDistance(DistanceUnit.CM));
        // Display reflected light.
        telemetry.addData("Light detected", ((OpticalDistanceSensor) Colorsensor_REV_ColorRangeSensor).getLightDetected());
        // Adjust the gain.
        ((NormalizedColorSensor) Colorsensor_REV_ColorRangeSensor).setGain(gain);
        telemetry.addData("Gain", ((NormalizedColorSensor) Colorsensor_REV_ColorRangeSensor).getGain());
        // Read color from the sensor.
        normalizedColors = ((NormalizedColorSensor) Colorsensor_REV_ColorRangeSensor).getNormalizedColors();
        telemetry.addData("Red", Double.parseDouble(JavaUtil.formatNumber(normalizedColors.red, 3)));
        telemetry.addData("Green", Double.parseDouble(JavaUtil.formatNumber(normalizedColors.green, 3)));
        telemetry.addData("Blue", Double.parseDouble(JavaUtil.formatNumber(normalizedColors.blue, 3)));
        // Convert RGB values to Hue, Saturation, and Value.
        // See https://en.wikipedia.org/wiki/HSL_and_HSV for details on HSV color model.
        color = normalizedColors.toColor();
        hue = JavaUtil.colorToHue(color);
        saturation = JavaUtil.colorToSaturation(color);
        value = JavaUtil.colorToValue(color);
        telemetry.addData("Hue", Double.parseDouble(JavaUtil.formatNumber(hue, 0)));
        telemetry.addData("Saturation", Double.parseDouble(JavaUtil.formatNumber(saturation, 3)));
        telemetry.addData("Value", Double.parseDouble(JavaUtil.formatNumber(value, 3)));
        telemetry.addData("scanned value", scanned);
        telemetry.addData("Alpha", Double.parseDouble(JavaUtil.formatNumber(normalizedColors.alpha, 3)));
        // Show the color on the Robot Controller screen.
        JavaUtil.showColor(hardwareMap.appContext, color);
        // Use hue to determine if it's red, green, blue, etc..
        if(scanning&&scanned==1){
        if (hue < 1) {
          telemetry.addData("Color", "unknown");
         } else if (hue < 120) {
          telemetry.addData("Color", "oranje");
          output = 3;
          scanned ++;
          sleep(200);
         } else if (hue < 151) {
          telemetry.addData("Color", "Green");
          output = 1;
          scanned ++;
          sleep(200);
         } else if (hue > 179 && hue < 300) {
          telemetry.addData("Color", "purple");
          output = 2;
          scanned ++;
          sleep(200);
          } 
          else {
          telemetry.addData("Color", "unknown");
        }
        scanning = false;
        }
        
        
        // Check to see if it might be black or white.
        if (saturation < 0.2) {
          telemetry.addData("Check Sat", "Is surface white?");
        }
        telemetry.update();
        if (value < 0.16) {
          telemetry.addData("Check Val", "Is surface black?");
        }
        
        
        if (Colorsensor_DistanceSensor.getDistance(DistanceUnit.CM) < 2 &&scanned == 0) {
          moving = false;
          scanning = true;
          scanned++;
          sleep(200);
          //SCAN();
        }
        if(scanned == 2 && !moving){ 
          if(output == 1){
            motorFrontLeft.setPower(-0.5);
            motorFrontRight.setPower(0.5);
            motorBackLeft.setPower(0.5);
            motorBackRight.setPower(-0.5);
            sleep(490);
            motorFrontLeft.setPower(0);
            motorFrontRight.setPower(0);
            motorBackLeft.setPower(0);
            motorBackRight.setPower(0);
            sleep(200);
            motorFrontLeft.setPower(0.5); 
            motorFrontRight.setPower(-0.5);
            motorBackLeft.setPower(-0.5);
            motorBackRight.setPower(0.5);
            sleep(210);
            motorFrontLeft.setPower(0);
            motorFrontRight.setPower(0);
            motorBackLeft.setPower(0);
            motorBackRight.setPower(0);
            sleep(750);
            motorFrontLeft.setPower(-0.5);
            motorFrontRight.setPower(-0.5);
            motorBackLeft.setPower(-0.5);
            motorBackRight.setPower(-0.5);
            sleep(650);
            motorFrontLeft.setPower(0);
            motorFrontRight.setPower(0);
            motorBackLeft.setPower(0);
            motorBackRight.setPower(0);
            sleep(750);
            // /*motorFrontLeft.setPower(-0.5);
            // motorFrontRight.setPower(0.5);
            // motorBackLeft.setPower(0.5);
            // motorBackRight.setPower(-0.5);
            // sleep(1100);*/
            // motorFrontLeft.setPower(0);
            // motorFrontRight.setPower(0);
            // motorBackLeft.setPower(0);
            // motorBackRight.setPower(0);
            scanned ++;
          }
          if(output == 2){
            motorFrontLeft.setPower(-0.5);
            motorFrontRight.setPower(0.5);
            motorBackLeft.setPower(0.5);
            motorBackRight.setPower(-0.5);
            sleep(900);
            motorFrontLeft.setPower(0);
            motorFrontRight.setPower(0);
            motorBackLeft.setPower(0);
            motorBackRight.setPower(0);
            sleep(200);
            motorFrontLeft.setPower(-0.3);
            motorFrontRight.setPower(-0.3);
            motorBackLeft.setPower(-0.3);
            motorBackRight.setPower(-0.3);
            sleep(100);
            motorFrontLeft.setPower(0);
            motorFrontRight.setPower(0);
            motorBackLeft.setPower(0);
            motorBackRight.setPower(0);
            scanned ++;
          }
          if(output == 3){
            motorFrontLeft.setPower(-0.5);
            motorFrontRight.setPower(0.5);
            motorBackLeft.setPower(0.5);
            motorBackRight.setPower(-0.5);
            sleep(450);
            motorFrontLeft.setPower(0);
            motorFrontRight.setPower(0);
            motorBackLeft.setPower(0);
            motorBackRight.setPower(0);
            sleep(200);
            motorFrontLeft.setPower(0.5);
            motorFrontRight.setPower(-0.5);
            motorBackLeft.setPower(-0.5);
            motorBackRight.setPower(0.5);
            sleep(150);
            motorFrontLeft.setPower(0);
            motorFrontRight.setPower(0);
            motorBackLeft.setPower(0);
            motorBackRight.setPower(0);
            sleep(750);
            motorFrontLeft.setPower(0.5);
            motorFrontRight.setPower(0.5);
            motorBackLeft.setPower(0.5);
            motorBackRight.setPower(0.5);
            sleep(480);
            motorFrontLeft.setPower(0);
            motorFrontRight.setPower(0);
            motorBackLeft.setPower(0);
            motorBackRight.setPower(0);
            sleep(750);
            // /*motorFrontLeft.setPower(-0.5);
            // motorFrontRight.setPower(0.5);
            // motorBackLeft.setPower(0.5);
            // motorBackRight.setPower(-0.5);
            // sleep(1100);*/
            // motorFrontLeft.setPower(0);
            // motorFrontRight.setPower(0);
            // motorBackLeft.setPower(0);
            // motorBackRight.setPower(0);
            scanned ++;
          }
        }
        if (moving && timer < 200) {
          motorFrontRight.setPower(0.3);
          motorBackRight.setPower(-0.3);
          motorFrontLeft.setPower(-0.3);
          motorBackLeft.setPower(0.3);
          timer ++;
        } else {
          motorFrontRight.setPower(0);
          motorBackRight.setPower(0);
          motorFrontLeft.setPower(0);
          motorBackLeft.setPower(0);
        }
    }
    }
      // Show white on the Robot Controller screen.
      JavaUtil.showColor(hardwareMap.appContext, Color.parseColor("white"));
    }
    
    




// void SCAN() {
//       if (hue < 1) {
//           telemetry.addData("Color", "unknown");
//         } else if (hue < 100) {
//           telemetry.addData("Color", "Rood");
//           output = 3;
//           scanned = true;
//         } else if (hue < 200) {
//           telemetry.addData("Color", "Green");
//           output = 1;
//           scanned = true;
//         } else if (hue < 300) {
//           telemetry.addData("Color", "purple");
//           output = 2;
//           scanned = true;
//         } else {
//           telemetry.addData("Color", "unknown");
//         }
//     }

}
