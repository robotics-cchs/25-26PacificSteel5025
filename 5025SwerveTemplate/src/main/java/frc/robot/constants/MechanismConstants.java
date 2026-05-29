package frc.robot.constants;

import static edu.wpi.first.units.Units.Amps;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.configs.VoltageConfigs;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

public class MechanismConstants {
    
    public static class OIConstants {
        public static final int controllerOnePort = 0;
        public static final int controllerTwoPort = 1;
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
    }

    public static class Settings {
        public static final double EX_MOTOR_SPEED = 0.5;
        public static final double FORWARD = 1.0;
        public static final double REVERSE = -1.0;

        // Shooter Targets in Rotations Per Second (RPS)
        public static final double SHOOT_VELOCITY_RPS = 60.0; // Target shooting RPS
        public static final double INTAKE_VELOCITY_RPS = -20.0; // Target intake/reverse RPS

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
        ).withSlot0(
            // Slot 0 PID + Feedforward gains for velocity control (Theme 1 Option 1)
            new Slot0Configs()
                .withKP(0.12) // Proportional gain
                .withKI(0.0)  // Integral gain
                .withKD(0.0)  // Derivative gain
                .withKS(0.15) // Static friction feedforward (volts)
                .withKV(0.11) // Velocity feedforward (volts per RPS)
        );
    }
}
