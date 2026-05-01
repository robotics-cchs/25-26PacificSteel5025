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
            public static final double EX_MOTOR_SPEED = .5;
        }
        public static final double FORWARD = 1;
        public static final double REVERSE = -1;

        public static final double MAX_VOLTAGE = 12;
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
        public static final int tfxPort58 = 58;
        public static final int tfxPort59 = 59;

        // Initialize Controllers/Joysticks
        public static final CommandXboxController controllerOne = new CommandXboxController(controllerOnePort);
        public static final CommandXboxController controllerTwo = new CommandXboxController(controllerTwoPort);

        // Initialize TalonFX Non-Swerve MotorControllers
        public static final TalonFX krkExampleMotor = new TalonFX(tfxPort50); //Kraken X60 : Left Shooter

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
        public static final TalonFXConfiguration defaultPowerConfig = new TalonFXConfiguration().withCurrentLimits(
            new CurrentLimitsConfigs()
                .withStatorCurrentLimit(Amps.of(90)) // Makes stator current limits
                .withStatorCurrentLimitEnable(true) // Enables the current limits
                .withSupplyCurrentLimit(Amps.of(50)) // Makes supply current limits
                .withSupplyCurrentLimitEnable(true) // Enables the current limits
        ).withMotorOutput(
            new MotorOutputConfigs()
                .withInverted(InvertedValue.Clockwise_Positive) // Says to not inverse the motor
                .withNeutralMode(NeutralModeValue.Coast)
        ).withVoltage(
            new VoltageConfigs()
                .withPeakForwardVoltage(MAX_VOLTAGE)
        );
        public static final TalonFXConfiguration invertedDefaultPowerConfig = new TalonFXConfiguration().withCurrentLimits(
            new CurrentLimitsConfigs()
                .withStatorCurrentLimit(Amps.of(90)) // Makes stator current limits
                .withStatorCurrentLimitEnable(true) // Enables the current limits
                .withSupplyCurrentLimit(Amps.of(50)) // Makes supply current limits
                .withSupplyCurrentLimitEnable(true) // Enables the current limits
        ).withMotorOutput(
            new MotorOutputConfigs()
                .withInverted(InvertedValue.Clockwise_Positive) // Says to not inverse the motor
                .withNeutralMode(NeutralModeValue.Coast)
        ).withVoltage(
            new VoltageConfigs()
                .withPeakForwardVoltage(MAX_VOLTAGE)
        );
    }
}
