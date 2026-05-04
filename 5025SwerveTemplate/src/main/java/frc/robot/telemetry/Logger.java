package frc.robot.telemetry;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * A centralized logging utility designed to mimic the AdvantageKit recordOutput API.
 * This ensures all subsystems log their states consistently and efficiently.
 * If the team upgrades to full AdvantageKit in the future, only this file needs to change.
 */
public class Logger {
    
    public static void recordOutput(String key, double value) {
        SmartDashboard.putNumber(key, value);
    }

    public static void recordOutput(String key, boolean value) {
        SmartDashboard.putBoolean(key, value);
    }

    public static void recordOutput(String key, String value) {
        SmartDashboard.putString(key, value);
    }

    public static void recordOutput(String key, Pose2d pose) {
        NetworkTableInstance.getDefault().getStructTopic(key, Pose2d.struct).publish().set(pose);
    }

    public static void recordOutput(String key, Pose2d... poses) {
        NetworkTableInstance.getDefault().getStructArrayTopic(key, Pose2d.struct).publish().set(poses);
    }

    public static void recordOutput(String key, Pose3d pose) {
        NetworkTableInstance.getDefault().getStructTopic(key, Pose3d.struct).publish().set(pose);
    }

    public static void recordOutput(String key, SwerveModuleState... states) {
        NetworkTableInstance.getDefault().getStructArrayTopic(key, SwerveModuleState.struct).publish().set(states);
    }

    public static void recordOutput(String key, SwerveModulePosition... positions) {
        NetworkTableInstance.getDefault().getStructArrayTopic(key, SwerveModulePosition.struct).publish().set(positions);
    }
}
