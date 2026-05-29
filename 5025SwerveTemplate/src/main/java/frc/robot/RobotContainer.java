// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.net.PortForwarder;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.FollowPathCommand;
import com.pathplanner.lib.commands.PathfindingCommand;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.RobotModeTriggers;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.constants.PoseConstants;
import frc.robot.constants.SwerveConstants;
import frc.robot.constants.MechanismConstants.OIConstants;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.VisionSubsystem;
import frc.robot.subsystems.mechanisms.ExampleMechanismSubsystem;
import frc.robot.subsystems.mechanisms.ExampleMechanismSubsystem.State;
import frc.robot.telemetry.Telemetry;

public class RobotContainer {
    double speed = 1.00;
    
    // Drive Speed Located Here
    private double MaxSpeed = speed * SwerveConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top speed
    private double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond); // 3/4 of a rotation per second max angular velocity

    // Setting up bindings for necessary control of the swerve drive platform
    private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
            .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
            .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors
    private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();

    private final Telemetry logger = new Telemetry(MaxSpeed);

    public final CommandSwerveDrivetrain drivetrain = SwerveConstants.createDrivetrain();

    private final VisionSubsystem vision = new VisionSubsystem(drivetrain);
    private final ExampleMechanismSubsystem mechanism = new ExampleMechanismSubsystem();

    private final CommandXboxController controllerOne = new CommandXboxController(OIConstants.controllerOnePort);
    private final CommandXboxController controllerTwo = new CommandXboxController(OIConstants.controllerTwoPort);

    /* Path follower */
    private final SendableChooser<Command> autoChooser;
    
    public RobotContainer() {
        configureAutoBindings();
        configureBindings();
        
        autoChooser = AutoBuilder.buildAutoChooser("Tests");
        SmartDashboard.putData("Auto Mode", autoChooser);
        // Warmup PathPlanner to avoid Java pauses
        FollowPathCommand.warmupCommand().schedule();
        PathfindingCommand.warmupCommand().schedule();
        PortForwarder.add(5800, "photonvision.local", 5800);
    }
    // Path target and commands are handled by StateHandler now.
    
    private void configureAutoBindings() {
        // Register Commands
        NamedCommands.registerCommand("ExampleToggle", mechanism.runStateCommand(State.FORWARD));
    }
    private void configureBindings() {
        // Note that X is defined as forward according to WPILib convention,
        // and Y is defined as to the left according to WPILib convention.
        drivetrain.setDefaultCommand(
            drivetrain.applyTeleopDrive(
                drive,
                () -> controllerOne.getLeftY(),
                () -> controllerOne.getLeftX(),
                () -> controllerOne.getRightX(),
                MaxSpeed,
                MaxAngularRate
            )
        );

        // Idle while the robot is disabled. This ensures the configured
        // neutral mode is applied to the drive motors while disabled.
        final var idle = new SwerveRequest.Idle();
        RobotModeTriggers.disabled().whileTrue(
            drivetrain.applyRequest(() -> idle).ignoringDisable(true)
        );

        // Drivetrain & Mechanism Bindings
        controllerOne.x().whileTrue(drivetrain.applyRequest(() -> brake));
        controllerOne.back().onTrue(drivetrain.runOnce(drivetrain::seedFieldCentric));
        controllerOne.y().whileTrue(mechanism.runVelocityCommand(50.0));

        controllerOne.povLeft().onTrue(
            Commands.parallel(
                drivetrain.pathfindToPoseCommand(PoseConstants.SHOOT_LEFT),
                mechanism.runStateCommand(State.FORWARD)
            )
        );
        controllerOne.povRight().onTrue(
            Commands.parallel(
                drivetrain.pathfindToPoseCommand(PoseConstants.SHOOT_RIGHT),
                mechanism.runStateCommand(State.FORWARD)
            )
        );
        controllerOne.povDown().onTrue(
            Commands.parallel(
                drivetrain.pathfindToPoseCommand(PoseConstants.SHOOT_MID),
                mechanism.runStateCommand(State.FORWARD)
            )
        );

        drivetrain.registerTelemetry(logger::telemeterize);
    }

    public Command getAutonomousCommand() {
        /* Run the path selected from the auto chooser */
        return autoChooser.getSelected();
    }
}