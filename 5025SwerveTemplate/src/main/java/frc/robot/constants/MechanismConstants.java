package frc.robot.constants;

import static edu.wpi.first.units.Units.Amps;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.configs.VoltageConfigs;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

public class MechanismConstants {
    
    public static class OIConstants {
        public static final int controllerOnePort = 0;
        public static final int controllerTwoPort = 1;

        public static final CommandXboxController controllerOne = new CommandXboxController(controllerOnePort);
        public static final CommandXboxController controllerTwo = new CommandXboxController(controllerTwoPort);
    }

    public static class HardwareConstants {
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

        public static final TalonFX krkExampleMotor = new TalonFX(tfxPort50); //Kraken X60 : Left Shooter
    }

    public static class Settings {
        public static final double EX_MOTOR_SPEED = 0.5;
        public static final double FORWARD = 1.0;
        public static final double REVERSE = -1.0;

        public static final double MAX_VOLTAGE = 12.0;
        public static final double MAX_CURRENT_SUPPLY = 60.0;
        public static final double MAX_CURRENT_STATOR = 60.0;
        public static final boolean SET_SAFETY_TRUE = true;
    }

    public static class Configs {
        public static final TalonFXConfiguration defaultConfig = new TalonFXConfiguration().withCurrentLimits(
            new CurrentLimitsConfigs()
                .withStatorCurrentLimit(Amps.of(Settings.MAX_CURRENT_STATOR))
                .withStatorCurrentLimitEnable(true)
                .withSupplyCurrentLimit(Amps.of(Settings.MAX_CURRENT_SUPPLY))
                .withSupplyCurrentLimitEnable(true)
        ).withMotorOutput(
            new MotorOutputConfigs()
                .withInverted(InvertedValue.Clockwise_Positive)
                .withNeutralMode(NeutralModeValue.Coast)
        ).withVoltage(
            new VoltageConfigs()
                .withPeakForwardVoltage(Settings.MAX_VOLTAGE)
        );

        public static final TalonFXConfiguration invertedDefaultConfig = new TalonFXConfiguration().withCurrentLimits(
            new CurrentLimitsConfigs()
                .withStatorCurrentLimit(Amps.of(Settings.MAX_CURRENT_STATOR))
                .withStatorCurrentLimitEnable(true)
                .withSupplyCurrentLimit(Amps.of(Settings.MAX_CURRENT_SUPPLY))
                .withSupplyCurrentLimitEnable(true)
        ).withMotorOutput(
            new MotorOutputConfigs()
                .withInverted(InvertedValue.CounterClockwise_Positive)
                .withNeutralMode(NeutralModeValue.Coast)
        ).withVoltage(
            new VoltageConfigs()
                .withPeakForwardVoltage(Settings.MAX_VOLTAGE)
        );

        public static final TalonFXConfiguration defaultPowerConfig = new TalonFXConfiguration().withCurrentLimits(
            new CurrentLimitsConfigs()
                .withStatorCurrentLimit(Amps.of(90))
                .withStatorCurrentLimitEnable(true)
                .withSupplyCurrentLimit(Amps.of(50))
                .withSupplyCurrentLimitEnable(true)
        ).withMotorOutput(
            new MotorOutputConfigs()
                .withInverted(InvertedValue.Clockwise_Positive)
                .withNeutralMode(NeutralModeValue.Coast)
        ).withVoltage(
            new VoltageConfigs()
                .withPeakForwardVoltage(Settings.MAX_VOLTAGE)
        );
    }
}
