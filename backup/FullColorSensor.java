package org.firstinspires.ftc.teamcode;

import android.graphics.Color;
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

  /**
   * This function is executed when this Op Mode is selected from the Driver Station.
   */
  @Override
  public void runOpMode() {
    float hue;
    boolean moving;
    boolean scanned;
    int output;
    int gain;
    NormalizedRGBA normalizedColors;
    int color;
    float saturation;
    float value;

    motorFrontRight = hardwareMap.get(DcMotor.class, "motorFrontRight");
    motorBackRight = hardwareMap.get(DcMotor.class, "motorBackRight");
    Colorsensor_REV_ColorRangeSensor = hardwareMap.get(ColorSensor.class, "Colorsensor");
    Colorsensor_DistanceSensor = hardwareMap.get(DistanceSensor.class, "Colorsensor");
    motorFrontLeft = hardwareMap.get(DcMotor.class, "motorFrontLeft");
    motorBackLeft = hardwareMap.get(DcMotor.class, "motorBackLeft");

    // This op mode demonstrates the color and distance features of the REV sensor.
    motorFrontRight.setDirection(DcMotorSimple.Direction.REVERSE);
    motorBackRight.setDirection(DcMotorSimple.Direction.REVERSE);
    moving = true;
    scanned = false;
    output = 0;
    gain = 2;
    telemetry.addData("Color Distance Example", "Press start to continue...");
    telemetry.update();
    waitForStart();
    if (opModeIsActive()) {
      // Put run blocks here.
      while (opModeIsActive()) {
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
        telemetry.addData("Alpha", Double.parseDouble(JavaUtil.formatNumber(normalizedColors.alpha, 3)));
        // Show the color on the Robot Controller screen.
        JavaUtil.showColor(hardwareMap.appContext, color);
        // Use hue to determine if it's red, green, blue, etc..
        if(!scanned){
        if (hue < 1) {
          telemetry.addData("Color", "unknown");
        } else if (hue < 100) {
          telemetry.addData("Color", "Oranje");
          output = 3;
          scanned = true;
        } else if (hue < 200) {
          telemetry.addData("Color", "Green");
          output = 1;
          scanned = true;
        } else if (hue < 300) {
          telemetry.addData("Color", "purple");
          output = 2;
          scanned = true;
        } else {
          telemetry.addData("Color", "unknown");
        }
        }
        
        // Check to see if it might be black or white.
        if (saturation < 0.2) {
          telemetry.addData("Check Sat", "Is surface white?");
        }
        telemetry.update();
        if (value < 0.16) {
          telemetry.addData("Check Val", "Is surface black?");
        }
        if (Colorsensor_DistanceSensor.getDistance(DistanceUnit.CM) < 4) {
          moving = false;
          //SCAN();
        }
        if(scanned && !moving){ 
          if(output == 1){
            motorFrontLeft.setPower(-0.5);
            motorFrontRight.setPower(0.5);
            motorBackLeft.setPower(0.5);
            motorBackRight.setPower(-0.5);
            sleep(300);
            motorFrontLeft.setPower(0);
            motorFrontRight.setPower(0);
            motorBackLeft.setPower(0);
            motorBackRight.setPower(0);
            sleep(750);
            motorFrontLeft.setPower(-0.5);
            motorFrontRight.setPower(-0.5);
            motorBackLeft.setPower(-0.5);
            motorBackRight.setPower(-0.5);
            sleep(700);
            motorFrontLeft.setPower(0);
            motorFrontRight.setPower(0);
            motorBackLeft.setPower(0);
            motorBackRight.setPower(0);
            sleep(750);
            /*motorFrontLeft.setPower(-0.5);
            motorFrontRight.setPower(0.5);
            motorBackLeft.setPower(0.5);
            motorBackRight.setPower(-0.5);
            sleep(1100);*/
            motorFrontLeft.setPower(0);
            motorFrontRight.setPower(0);
            motorBackLeft.setPower(0);
            motorBackRight.setPower(0);
            scanned = false;
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
            scanned = false;
          }
          if(output == 3){
            motorFrontLeft.setPower(-0.5);
            motorFrontRight.setPower(0.5);
            motorBackLeft.setPower(0.5);
            motorBackRight.setPower(-0.5);
            sleep(300);
            motorFrontLeft.setPower(0);
            motorFrontRight.setPower(0);
            motorBackLeft.setPower(0);
            motorBackRight.setPower(0);
            sleep(750);
            motorFrontLeft.setPower(0.5);
            motorFrontRight.setPower(0.5);
            motorBackLeft.setPower(0.5);
            motorBackRight.setPower(0.5);
            sleep(700);
            motorFrontLeft.setPower(0);
            motorFrontRight.setPower(0);
            motorBackLeft.setPower(0);
            motorBackRight.setPower(0);
            sleep(750);
            /*motorFrontLeft.setPower(-0.5);
            motorFrontRight.setPower(0.5);
            motorBackLeft.setPower(0.5);
            motorBackRight.setPower(-0.5);
            sleep(1100);*/
            motorFrontLeft.setPower(0);
            motorFrontRight.setPower(0);
            motorBackLeft.setPower(0);
            motorBackRight.setPower(0);
            scanned = false;
          }
        }
        
        if (moving) {
          motorFrontRight.setPower(0.3);
          motorBackRight.setPower(-0.3);
          motorFrontLeft.setPower(-0.3);
          motorBackLeft.setPower(0.3);
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
//           telemetry.addData("Color", "Oranje");
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
