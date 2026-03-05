package frc.robot.constants;
// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

import static edu.wpi.first.units.Units.Amps;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.configs.VoltageConfigs;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

/** Add your docs here. */
public class MechanismConstants {
    public static class OperatorConstants {
        public static class MotorSettings {
            public static final double INTAKE_SPEED = 0.8;
            public static final double INTAKE_LIFTER_SPEED = 0.5;
            public static final double KICKER_SPEED_BASE = 0.4;
            public static final double SHOOTER_SPEED_BASE = 0.4;
            public static final double CONVEYOR_SPEED = 0.2;

            // Lifter PID constants
            public static final double LIFTER_kP = 0.5;
            public static final double LIFTER_kI = 0.0;
            public static final double LIFTER_kD = 0.0;
            public static final double LIFTER_kF = 0.0;

            // Lifter reference points
            public static final double LIFTER_UP_SETPOINT = 0.0;
            public static final double LIFTER_MIDDLE_SETPOINT = -12.5;
            public static final double LIFTER_DOWN_SETPOINT = -25.0;
            
            // Tolerance
            public static final double LIFTER_POSITION_TOLERANCE_ROTATIONS = 0.05; // adjust based on testing and requirements
        }
        public static final double FORWARD = 1;
        public static final double REVERSE = -1;

        public static final double MAX_VOLTAGE = 12.2;
        public static final double MAX_CURRENT_SUPPLY = 60;
        public static final double MAX_CURRENT_STATOR = 60;
        public static final boolean SET_SAFETY_TRUE = true;
        
        // Controller/Joystick Ports
        public static final int controllerOnePort = 0;
        public static final int controllerTwoPort = 1;

        // Kraken Ports 50 ... 59
        public static final int tfxPort50 = 50;
        public static final int tfxPort51 = 51;
        public static final int tfxPort52 = 52; 
        public static final int tfxPort53 = 53; 
        public static final int tfxPort54 = 54; 
        public static final int tfxPort55 = 55; 
        public static final int tfxPort56 = 56; 
        public static final int tfxPort57 = 57;

        // Initialize Controllers/Joysticks
        public static final CommandXboxController controllerOne = new CommandXboxController(controllerOnePort);
        public static final CommandXboxController controllerTwo = new CommandXboxController(controllerTwoPort);

        // Initialize TalonFX Non-Swerve MotorControllers
        public static final TalonFX krkLeftShooterMotor = new TalonFX(tfxPort50); //Kraken X60 : Left Shooter
        public static final TalonFX krkRightShooterMotor = new TalonFX(tfxPort51); // Kraken X60 : Right Shooter

        public static final TalonFX krkIntakeMotor = new TalonFX(tfxPort52); // Kraken X44 : Intake (right)
        public static final TalonFX krkIntakeLifterMotor = new TalonFX(tfxPort53); // Kraken X60 : Intake Lifter

        public static final TalonFX krkConveyorMotor = new TalonFX(tfxPort54); // Kraken X44 : Conveyor

        public static final TalonFX krkLeftKickerMotor = new TalonFX(tfxPort55); // Kraken X44 : Left Kicker
        public static final TalonFX krkRightKickerMotor = new TalonFX(tfxPort56); // Kraken X44 : Right Kicker
        
        public static final TalonFXConfiguration defaultConfig = new TalonFXConfiguration().withCurrentLimits(
            new CurrentLimitsConfigs()
                .withStatorCurrentLimit(Amps.of(MAX_CURRENT_STATOR)) // Makes stator current limits
                .withStatorCurrentLimitEnable(true) // Enables the current limits
                .withSupplyCurrentLimit(Amps.of(MAX_CURRENT_SUPPLY)) // Makes supply current limits
                .withSupplyCurrentLimitEnable(true) // Enables the current limits
        ).withMotorOutput(
            new MotorOutputConfigs()
                .withInverted(InvertedValue.Clockwise_Positive) // Says to not inverse the motor
                .withNeutralMode(NeutralModeValue.Coast)
        ).withVoltage(
            new VoltageConfigs()
                .withPeakForwardVoltage(MAX_VOLTAGE)
        );
        public static final TalonFXConfiguration invertedDefaultConfig = new TalonFXConfiguration().withCurrentLimits(
            new CurrentLimitsConfigs()
                .withStatorCurrentLimit(Amps.of(MAX_CURRENT_STATOR)) // Makes stator current limits
                .withStatorCurrentLimitEnable(true) // Enables the current limits
                .withSupplyCurrentLimit(Amps.of(MAX_CURRENT_SUPPLY)) // Makes supply current limits
                .withSupplyCurrentLimitEnable(true) // Enables the current limits
        ).withMotorOutput(
            new MotorOutputConfigs()
                .withInverted(InvertedValue.CounterClockwise_Positive) // Says to inverse the motor
                .withNeutralMode(NeutralModeValue.Coast)
        ).withVoltage(
            new VoltageConfigs()
                .withPeakForwardVoltage(MAX_VOLTAGE)
        );
    }
}
