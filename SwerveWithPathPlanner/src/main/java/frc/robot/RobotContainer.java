// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.*;

import java.util.Set;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.net.PortForwarder;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.FollowPathCommand;
import com.pathplanner.lib.commands.PathfindingCommand;
import com.pathplanner.lib.path.PathConstraints;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.RobotModeTriggers;
import frc.robot.constants.PoseConstants;
// import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
import frc.robot.constants.SwerveConstants;
import frc.robot.constants.MechanismConstants.OperatorConstants;
import frc.robot.helpers.AutoAlign;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.VisionSubsystem;
import frc.robot.subsystems.mechanisms.ConveyorSubsystem;
import frc.robot.subsystems.mechanisms.IntakeSubsystem;
import frc.robot.subsystems.mechanisms.KickerSubsystem;
import frc.robot.subsystems.mechanisms.ShooterSubsystem;
// import frc.robot.subsystems.vision.VisionSubsystem;
import frc.robot.telemetry.Telemetry;

public class RobotContainer {
    double speed = 1.00;
    
    // Drive Speed Located Here
    private double MaxSpeed = speed * SwerveConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top speed
    private double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond); // 3/4 of a rotation per second max angular velocity

    // Initialization of Subsystems
    private final ConveyorSubsystem m_conveyorSubsystem = new ConveyorSubsystem();
    private final IntakeSubsystem m_intakeSubsystem = new IntakeSubsystem();
    // private final IntakeLifterSubsystem m_intakeLifterSubsystem = new IntakeLifterSubsystem();
    private final KickerSubsystem m_kickerSubsystem = new KickerSubsystem();
    private final ShooterSubsystem m_shooterSubsystem = new ShooterSubsystem();
    private final AutoAlign m_autoAlign = new AutoAlign();
    /* PATHFINDING VARIABLES */

    PathConstraints constraints = new PathConstraints(
        3.0, 3.0,
        Units.degreesToRadians(540), Units.degreesToRadians(720)
    );

    private Pose2d currentTarget = PoseConstants.SHOOT_MID; // default target

    // All requests as variables, and now instead of command files, with a more modern WPILib style

    // Pathfind
    Command pathfindToCurrentTarget() {
    return Commands.defer(
        () -> AutoBuilder.pathfindToPose(currentTarget, constraints, 0.0),
        Set.of(drivetrain)
    );
}
    // Shooter Subsystem
    Command toggleShootCommand = Commands.runOnce(() -> {
        m_shooterSubsystem.toggle();
    }, m_shooterSubsystem);

    Command shooterOffCommand = Commands.runOnce(() -> {
        m_shooterSubsystem.off();
    }, m_shooterSubsystem);

    Command shooterOnCommand = Commands.runOnce(() -> {
        m_shooterSubsystem.on();
    }, m_shooterSubsystem);

    Command shooterSpeedUpCommand = Commands.runOnce(() -> {
        m_shooterSubsystem.inc();
    }, m_shooterSubsystem);

    Command shooterSpeedDownCommand = Commands.runOnce(() -> {
        m_shooterSubsystem.dec();
    }, m_shooterSubsystem);

    Command teleopEnableCommand = Commands.runOnce(() -> {
        m_shooterSubsystem.teleopEnable();
    }, m_shooterSubsystem);

    // Kicker Subsystem
    Command toggleKickerCommand = Commands.runOnce(() -> {
        m_kickerSubsystem.toggle();
    }, m_kickerSubsystem);

    Command kickerOnCommand = Commands.runOnce(() -> {
        m_kickerSubsystem.on();
    }, m_kickerSubsystem);

    Command kickerOffCommand = Commands.runOnce(() -> {
        m_kickerSubsystem.off();
    }, m_kickerSubsystem);

    Command kickerSetForwardCommand = Commands.runOnce(() -> {
        m_kickerSubsystem.forward();
    }, m_kickerSubsystem);

    Command kickerSetReverseCommand = Commands.runOnce(() -> {
        m_kickerSubsystem.reverse();
    }, m_kickerSubsystem);

    Command kickerSpeedUpCommand = Commands.runOnce(() -> {
        m_kickerSubsystem.inc();
    }, m_kickerSubsystem);

    Command kickerSpeedDownCommand = Commands.runOnce(() -> {
        m_kickerSubsystem.dec();
    }, m_kickerSubsystem);

    // Intake Subsystem
    Command toggleIntakeCommand = Commands.runOnce(() -> {
        m_intakeSubsystem.toggle();
    }, m_intakeSubsystem);

    Command intakeOnCommand = Commands.runOnce(() -> {
        m_intakeSubsystem.on();
    }, m_intakeSubsystem);

    Command intakeOffCommand = Commands.runOnce(() -> {
        m_intakeSubsystem.off();
    }, m_intakeSubsystem);

    Command intakeForwardCommand = Commands.runOnce(() -> {
        m_intakeSubsystem.forward();
    }, m_intakeSubsystem);

    Command intakeReverseCommand = Commands.runOnce(() -> {
        m_intakeSubsystem.reverse();
    }, m_intakeSubsystem);

    Command intakeSpeedUpCommand = Commands.runOnce(() -> {
        m_intakeSubsystem.inc();
    }, m_intakeSubsystem);

    Command intakeSpeedDownCommand = Commands.runOnce(() -> {
        m_intakeSubsystem.dec();
    }, m_intakeSubsystem);

    // Command intakeUpCommand = Commands.runOnce(() -> {
    //     m_intakeLifterSubsystem.setLifterUp();
    // }, m_intakeLifterSubsystem);

    // Command intakeDownCommand = Commands.runOnce(() -> {
    //     m_intakeLifterSubsystem.setLifterDown();
    // }, m_intakeLifterSubsystem);

    Command intakeUpCommand = Commands.runOnce(() -> {
        m_intakeSubsystem.intakeLifterSpeed(OperatorConstants.MotorSettings.INTAKE_LIFTER_SPEED);
    }, m_intakeSubsystem);

    Command intakeDownCommand = Commands.runOnce(() -> {
        m_intakeSubsystem.intakeLifterSpeed(-OperatorConstants.MotorSettings.INTAKE_LIFTER_SPEED);
    }, m_intakeSubsystem);
    
    Command intakeZeroCommand = Commands.runOnce(() -> {
        m_intakeSubsystem.intakeLifterSpeed(0);
    }, m_intakeSubsystem);
    
    // Conveyor Subsystem
    Command toggleConveyorCommand = Commands.runOnce(() -> {
        m_conveyorSubsystem.toggle();
    }, m_conveyorSubsystem);

    Command conveyorOnCommand = Commands.runOnce(() -> {
        m_conveyorSubsystem.on();
    }, m_conveyorSubsystem);

    Command conveyorOffCommand = Commands.runOnce(() -> {
        m_conveyorSubsystem.off();
    }, m_conveyorSubsystem);

    Command conveyorForwardCommand = Commands.runOnce(() -> {
        m_conveyorSubsystem.forward();
    }, m_conveyorSubsystem);

    Command conveyorReverseCommand = Commands.runOnce(() -> {
        m_conveyorSubsystem.reverse();
    }, m_conveyorSubsystem);

    // Setting up bindings for necessary control of the swerve drive platform
    private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
            .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
            .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors
    private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();
    // private final SwerveRequest.PointWheelsAt point = new SwerveRequest.PointWheelsAt();
    // private final SwerveRequest.RobotCentric forwardStraight = new SwerveRequest.RobotCentric()
    //         .withDriveRequestType(DriveRequestType.OpenLoopVoltage);

    private final Telemetry logger = new Telemetry(MaxSpeed);

    public final CommandSwerveDrivetrain drivetrain = SwerveConstants.createDrivetrain();

    private final VisionSubsystem vision = new VisionSubsystem(drivetrain);

    /* Path follower */
    private final SendableChooser<Command> autoChooser;

    public RobotContainer() {
        m_shooterSubsystem.setPoseSupplier(() -> drivetrain.getState().Pose); // to provide location data to shooter
        // VisionSubsystem vision = new VisionSubsystem(drivetrain);
        // m_intakeLifterSubsystem.zeroLifterEncoder();
        configureAutoBindings();
        configureBindings();
        
        autoChooser = AutoBuilder.buildAutoChooser("Tests");
        SmartDashboard.putData("Auto Mode", autoChooser);
        // Warmup PathPlanner to avoid Java pauses
        FollowPathCommand.warmupCommand().schedule();
        PathfindingCommand.warmupCommand().schedule();
        PortForwarder.add(5800, "photonvision.local", 5800);
    }
    private void configureAutoBindings() {
        // Register Commands
        // Shooter Commands
        NamedCommands.registerCommand("ToggleShoot", toggleShootCommand);
        NamedCommands.registerCommand("TeleopEnable", teleopEnableCommand);
        NamedCommands.registerCommand("ShooterSpeedUp", shooterSpeedUpCommand);
        NamedCommands.registerCommand("ShooterSpeedDown", shooterSpeedDownCommand);
        NamedCommands.registerCommand("ShooterOn", shooterOnCommand);
        NamedCommands.registerCommand("ShooterOff", shooterOffCommand);

        // Kicker Commands
        NamedCommands.registerCommand("ToggleKicker", toggleKickerCommand);
        NamedCommands.registerCommand("KickerSpeedUp", kickerSpeedUpCommand);
        NamedCommands.registerCommand("KickerSpeedDown", kickerSpeedDownCommand);
        NamedCommands.registerCommand("KickerSetForward", kickerSetForwardCommand);
        NamedCommands.registerCommand("KickerSetReverse", kickerSetReverseCommand);

        // Intake Commands
        NamedCommands.registerCommand("ToggleIntake", toggleIntakeCommand);
        NamedCommands.registerCommand("IntakeForward", intakeForwardCommand);
        NamedCommands.registerCommand("IntakeReverse", intakeReverseCommand);
        NamedCommands.registerCommand("IntakeUp", intakeUpCommand);
        NamedCommands.registerCommand("IntakeDown", intakeDownCommand);
        NamedCommands.registerCommand("IntakeZero", intakeZeroCommand);

        // Conveyor Commands
        NamedCommands.registerCommand("ToggleConveyor", toggleConveyorCommand);
        NamedCommands.registerCommand("ConveyorForward", conveyorForwardCommand);
        NamedCommands.registerCommand("ConveyorReverse", conveyorReverseCommand);
    }
    private void configureBindings() {
        // Note that X is defined as forward according to WPILib convention,
        // and Y is defined as to the left according to WPILib convention.
        drivetrain.setDefaultCommand(
            // Drivetrain will execute this command periodically
            drivetrain.applyRequest(() ->
                drive.withVelocityX(OperatorConstants.controllerOne.getLeftY() * -MaxSpeed / 1) // Drive forward with negative Y (forward)
                    .withVelocityY(OperatorConstants.controllerOne.getLeftX() * -MaxSpeed / 1) // Drive left with negative X (left)
                    // .withRotationalRate((-OperatorConstants.controllerOne.getRightX() + m_autoAlign.robotRotationOffset(drivetrain, OperatorConstants.controllerOne.b().getAsBoolean())) * MaxAngularRate) // Drive counterclockwise with negative X (left)
                    .withRotationalRate((-OperatorConstants.controllerOne.getRightX())* MaxAngularRate) // Drive counterclockwise with negative X (left)
            )
        );

        // Idle while the robot is disabled. This ensures the configured
        // neutral mode is applied to the drive motors while disabled.
        final var idle = new SwerveRequest.Idle();
        RobotModeTriggers.disabled().whileTrue(
            drivetrain.applyRequest(() -> idle).ignoringDisable(true)
        );

        // Controller Bindings: https://drive.google.com/drive/folders/12yf_uMQr7M03QmZiKqnwpWyf11m-gW6I
        // Drivetrain Bindings
        OperatorConstants.controllerOne.x().whileTrue(drivetrain.applyRequest(() -> brake));
        OperatorConstants.controllerOne.back().onTrue(drivetrain.runOnce(drivetrain::seedFieldCentric));
        OperatorConstants.controllerOne.y().toggleOnTrue(pathfindToCurrentTarget()); // 
        
        // Shooter
        OperatorConstants.controllerTwo.a().onTrue(shooterOnCommand); // Activate Shooter
        OperatorConstants.controllerTwo.x().onTrue(shooterOffCommand); // Deactivate Shooter
        OperatorConstants.controllerOne.leftBumper().onTrue(shooterSpeedDownCommand); // Decrease Shooter Speed
        OperatorConstants.controllerOne.rightBumper().onTrue(shooterSpeedUpCommand); // Increase Shooter Speed

        // Intake Lifter
        OperatorConstants.controllerOne.leftTrigger().onTrue(intakeDownCommand); // Intake Down
        OperatorConstants.controllerOne.rightTrigger().onTrue(intakeUpCommand); // Intake Up
        OperatorConstants.controllerOne.leftTrigger().onFalse(intakeZeroCommand); // Intake Down
        OperatorConstants.controllerOne.rightTrigger().onFalse(intakeZeroCommand); // Intake Up

        // Intake
        OperatorConstants.controllerTwo.b().onTrue(intakeForwardCommand); // Intake In
        OperatorConstants.controllerTwo.b().whileTrue(intakeOnCommand); // Activate Intake
        OperatorConstants.controllerTwo.b().onFalse(intakeOffCommand); // Deactivate Intake
        OperatorConstants.controllerTwo.y().onTrue(intakeReverseCommand); // Intake Out
        OperatorConstants.controllerTwo.y().whileTrue(intakeOnCommand); // Activate Intake
        OperatorConstants.controllerTwo.y().onFalse(intakeOffCommand); // Deactivate Intake

        // Kicker
        OperatorConstants.controllerTwo.leftBumper().onTrue(kickerSetReverseCommand); // Kicker Out/Reverse/Away Shooter
        OperatorConstants.controllerTwo.leftBumper().whileTrue(kickerOnCommand); // Activate Kicker
        OperatorConstants.controllerTwo.leftBumper().onFalse(kickerOffCommand); // Deactivate Kicker
        OperatorConstants.controllerTwo.leftTrigger().onTrue(kickerSetForwardCommand); // Kicker In/Forward/Towards Shooter
        OperatorConstants.controllerTwo.leftTrigger().whileTrue(kickerOnCommand); // Activate Kicker
        OperatorConstants.controllerTwo.leftTrigger().onFalse(kickerOffCommand); // Deactivate Kicker

        // Conveyor
        OperatorConstants.controllerTwo.rightBumper().onTrue(conveyorReverseCommand); // Conveyor Out/Reverse/Away Kicker
        OperatorConstants.controllerTwo.rightBumper().whileTrue(conveyorOnCommand); // Activate Conveyor
        OperatorConstants.controllerTwo.rightBumper().onFalse(conveyorOffCommand); // Deactivate Conveyor
        OperatorConstants.controllerTwo.rightTrigger().onTrue(conveyorForwardCommand); // Conveyor In/Forward/Towards Kicker
        OperatorConstants.controllerTwo.rightTrigger().whileTrue(conveyorOnCommand); // Activate Conveyor
        OperatorConstants.controllerTwo.rightTrigger().onFalse(conveyorOffCommand); // Deactivate Conveyor

        drivetrain.registerTelemetry(logger::telemeterize);
    }

    public Command getAutonomousCommand() {
        /* Run the path selected from the auto chooser */
        return autoChooser.getSelected();
    }
}