package org.firstinspires.ftc.utilities;

import com.qualcomm.hardware.bosch.BNO055IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import static org.firstinspires.ftc.utilities.Utils.hardwareMap;

public class IMU {
    private BNO055IMU imu;
    private Double previousAngle;
    private double deltaAngle;

    public IMU(String deviceName) {
        
        imu = hardwareMap().get(BNO055IMU.class, deviceName);
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        imu.initialize(parameters);

        previousAngle = null;
        deltaAngle = 0;
    }

    /**
     * @return the wrapped angle
     */
    public double getAngle(){

        // Get the current angle
        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double currentAngle = angles.firstAngle;

        // Update how many times we have wrapped
        deltaAngle = updateWraps(previousAngle, currentAngle, deltaAngle);

        // Update the previous angle
        previousAngle = currentAngle;
        return currentAngle + deltaAngle;
    }

    /**
     * @param {Double} previousAngle
     * @param {double} currentAngle
     * @param {double} deltaAngle
     * @return {double} the value to add to the currentAngle to get the currentWrappedAngle
     */
    static double updateWraps(Double previousAngle, double currentAngle, double deltaAngle){
        if (previousAngle != null){
            if (currentAngle - previousAngle >= 180){
                deltaAngle -= 360;
            }
            else if (currentAngle - previousAngle <= -180){
                deltaAngle += 360;
            }
        }
        return deltaAngle;
    }
}
