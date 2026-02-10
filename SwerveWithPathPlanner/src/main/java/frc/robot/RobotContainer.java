// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.cameraserver.CameraServer;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.commands.FollowPathCommand;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.RobotModeTriggers;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
import frc.robot.constants.SwerveConstants;
import frc.robot.constants.MechanismConstants.OperatorConstants;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.telemetry.Telemetry;

public class RobotContainer {
    double sped = 0;
    double slowSpeed = 0.25;
    double fullSpeed = 1.00;
    
    // CHANGE TO EITHER: slowSpeed or fullSpeed AND DEPLOY SS
    private double MaxSpeed = slowSpeed * SwerveConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top speed
    private double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond); // 3/4 of a rotation per second max angular velocity

    /* Setting up bindings for necessary control of the swerve drive platform */
    private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
            .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
            .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors
    private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();
    private final SwerveRequest.PointWheelsAt point = new SwerveRequest.PointWheelsAt();
    private final SwerveRequest.RobotCentric forwardStraight = new SwerveRequest.RobotCentric()
            .withDriveRequestType(DriveRequestType.OpenLoopVoltage);

    private final Telemetry logger = new Telemetry(MaxSpeed);

    public final CommandSwerveDrivetrain drivetrain = SwerveConstants.createDrivetrain();
    public final ShooterSubsystem shooter = new ShooterSubsystem();

    /* Path follower */
    private final SendableChooser<Command> autoChooser;

    public RobotContainer() {
        autoChooser = AutoBuilder.buildAutoChooser("Tests");
        SmartDashboard.putData("Auto Mode", autoChooser);

        configureBindings();
        CameraServer.startAutomaticCapture(0);
        // Warmup PathPlanner to avoid Java pauses
        FollowPathCommand.warmupCommand().schedule();
    }

    private void configureBindings() {
        // Note that X is defined as forward according to WPILib convention,
        // and Y is defined as to the left according to WPILib convention.
        drivetrain.setDefaultCommand(
            // Drivetrain will execute this command periodically
            drivetrain.applyRequest(() ->
                drive.withVelocityX(OperatorConstants.controllerOne.getLeftY() * -MaxSpeed / 1) // Drive forward with negative Y (forward)
                    .withVelocityY(OperatorConstants.controllerOne.getLeftX() * -MaxSpeed / 1) // Drive left with negative X (left)
                    .withRotationalRate(-OperatorConstants.controllerOne.getRightX() * MaxAngularRate) // Drive counterclockwise with negative X (left)
                // drive.withVelocityX(joystick.getLeftY() * MaxSpeed * (1-sped)) // Drive forward with negative Y (forward)
                //     .withVelocityY(joystick.getLeftX() * MaxSpeed * (1-sped)) // Drive left with negative X (left)
                //     .withRotationalRate(-joystick.getRightX() * MaxAngularRate) // Drive counterclockwise with negative X (left)

            )
        );

        // Idle while the robot is disabled. This ensures the configured
        // neutral mode is applied to the drive motors while disabled.
        final var idle = new SwerveRequest.Idle();
        RobotModeTriggers.disabled().whileTrue(
            drivetrain.applyRequest(() -> idle).ignoringDisable(true)
        );

        OperatorConstants.controllerOne.a().whileTrue(drivetrain.applyRequest(() -> brake));
        OperatorConstants.controllerOne.b().whileTrue(drivetrain.applyRequest(() ->
            point.withModuleDirection(new Rotation2d(-OperatorConstants.controllerOne.getLeftY(), -OperatorConstants.controllerOne.getLeftX()))
        ));

        OperatorConstants.controllerOne.povUp().whileTrue(drivetrain.applyRequest(() ->
            forwardStraight.withVelocityX(0.5).withVelocityY(0))
        );
        OperatorConstants.controllerOne.povDown().whileTrue(drivetrain.applyRequest(() ->
            forwardStraight.withVelocityX(-0.5).withVelocityY(0))
        );
        if (OperatorConstants.controllerOne.povLeft().getAsBoolean()) {
            sped = .6666;
        } else {
            if (OperatorConstants.controllerOne.povRight().getAsBoolean()) {
                sped = .3333;
            } else {
                sped = 0;
            }
        }

        // Run SysId routines when holding back/start and X/Y.
        // Note that each routine should be run exactly once in a single log.
        OperatorConstants.controllerOne.back().and(OperatorConstants.controllerOne.y()).whileTrue(drivetrain.sysIdDynamic(Direction.kForward));
        OperatorConstants.controllerOne.back().and(OperatorConstants.controllerOne.x()).whileTrue(drivetrain.sysIdDynamic(Direction.kReverse));
        OperatorConstants.controllerOne.start().and(OperatorConstants.controllerOne.y()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kForward));
        OperatorConstants.controllerOne.start().and(OperatorConstants.controllerOne.x()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kReverse));
        
        // Reset the field-centric heading on left bumper press.
        // joystick.leftBumper().onTrue(drivetrain.runOnce(drivetrain::seedFieldCentric));

        drivetrain.registerTelemetry(logger::telemeterize);
    }

    public Command getAutonomousCommand() {
        /* Run the path selected from the auto chooser */
        return autoChooser.getSelected();
    }
}
