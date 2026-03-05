// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.helpers;

import org.photonvision.PhotonUtils;

import edu.wpi.first.math.geometry.Pose2d;

/**
    * This helper class for calculating shooter speed based on distance using a quadratic formula. The coefficients (a, b, c) can be adjusted based on testing and requirements.
 */
public class ShooterSpeedHelper {
    // https://manerajona.medium.com/7-best-practices-for-java-api-documentation-dc6e7e87d33f#:~:text=3.%20Add%20Method%2DLevel%20Comments%20Including%20Inputs%20(%40param)%20and%20Outputs%20(%40return%20and%20%40throws)
    /**
    * A method to calculate the shooter speed based on distance using a quadratic formula. The coefficients (a, b, c) can be adjusted based on testing and requirements.
    *
    * @param a Quadratic coefficient
    * @param b Linear coefficient
    * @param c Constant term
    * @return The calculated shooter speed based on the quadratic formula.
    */
    public static double quadraticShooterSpeed(Pose2d robotPose, Pose2d targetPose, double a, double b, double c) {
        // from https://docs.photonvision.org/en/latest/docs/programming/photonlib/using-target-data.html
        double distance = PhotonUtils.getDistanceToPose(robotPose, targetPose);
        // Returns speed = a * distance^2 + b * distance + c
        // Adjust the coefficients based on testing and requirements
        return a * Math.pow(distance, 2) + b * distance + c;
    }
}
