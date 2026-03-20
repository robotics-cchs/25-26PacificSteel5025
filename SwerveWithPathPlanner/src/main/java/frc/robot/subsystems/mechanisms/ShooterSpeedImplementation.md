(from ChatGPT)

---

Good—this is where everything actually becomes usable on a real robot.

Your lookup table gives you a **target RPM**, but PID is what makes the shooter **actually hit and hold that RPM** despite battery sag, ball contact, etc.

---

# Big Picture

You’re layering things like this:

```
Pose → Distance → Lookup Table → Target RPM → PID → Motor Output
```

---

# 1. What PID is doing here

Your shooter PID loop is controlling:

[
\text{error} = \text{target RPM} - \text{current RPM}
]

And adjusting motor power to reduce that error.

---

# 2. You NEED a velocity PID (not position)

Make sure you’re using:

* **Velocity control**
* Encoder gives **RPM (or rad/s)**

If you’re using WPILib, this is typically:

* `PIDController` (software loop), OR
* Built-in motor controller PID (Spark MAX / TalonFX preferred)

---

# 3. The real answer: use Feedforward + PID

PID alone is not enough for shooters.

The best practice is:

[
\text{Output} = \text{Feedforward} + \text{PID}
]

---

## Feedforward (does most of the work)

This predicts how much power you need:

[
V = k_S + k_V \cdot \omega + k_A \cdot \alpha
]

For a shooter, acceleration is small, so:

[
V \approx k_S + k_V \cdot \omega
]

Where:

* (k_S): static friction
* (k_V): volts per RPM

WPILib has:

* `SimpleMotorFeedforward`

---

## PID (just corrects errors)

* Fixes small differences
* Handles disturbances (like shooting a note)

---

# 4. Implementation Flow

### Step 1: Get target RPM from your lookup

```java
double targetRPM = shooterMap.get(distance);
```

---

### Step 2: Measure current RPM

```java
double currentRPM = encoder.getVelocity();
```

---

### Step 3: Compute feedforward

```java
double ff = kS + kV * targetRPM;
```

(or WPILib class)

---

### Step 4: Compute PID correction

```java
double pidOutput = pid.calculate(currentRPM, targetRPM);
```

---

### Step 5: Combine them

```java
double output = ff + pidOutput;
motor.setVoltage(output);
```

---

# 5. Tuning Strategy (this matters a lot)

### Step 1: Tune feedforward FIRST

Use WPILib SysId if possible.

Goal:

* Motor reaches near target RPM **without PID**

---

### Step 2: Add small P

* Increase P until:

  * Fast response
  * No oscillation

---

### Step 3: Usually:

* I = 0 (or very tiny)
* D = 0 (rarely needed for shooters)

---

# 6. What happens when you shoot

When a note enters:

* RPM drops suddenly
* PID kicks in to recover

Good tuning = fast recovery

---

# 7. When to allow shooting

Super important:

Only fire when:

[
|\text{error}| < \text{tolerance}
]

Example:

```java
if (Math.abs(targetRPM - currentRPM) < 100) {
    shoot();
}
```

---

# 8. Common mistakes

### ❌ Using only PID

* Leads to slow spin-up and inconsistency

### ❌ No feedforward

* PID works way too hard

### ❌ Too much I gain

* Causes overshoot and weird behavior

### ❌ Not accounting for battery voltage

* Use voltage control if possible

---

# 9. Bonus: Dynamic RPM + PID

When your robot moves:

* Distance changes
* Target RPM changes continuously

Your PID loop should:

* Update setpoint every cycle (20ms)

---

# 10. Clean mental model

Think of it like this:

* **Lookup table** = “what speed should I aim for?”
* **Feedforward** = “how much power gets me close?”
* **PID** = “fix whatever’s off”

---

# What “pure WPILib” means here

You’re not using:

* Built-in PID on Spark MAX / TalonFX

Instead, you use:

* `PIDController`
* `SimpleMotorFeedforward`

And you control the motor with:

* `setVoltage()`

---

# 1. Core Components You Need

### Objects:

```java
PIDController pid = new PIDController(kP, kI, kD);
SimpleMotorFeedforward ff = new SimpleMotorFeedforward(kS, kV, kA);
```

### Sensor:

* Encoder that gives **velocity (RPM or rad/s)**

---

# 2. Your Control Loop (runs every 20ms)

This is the heart of everything:

```java
double targetRPM = shooterMap.get(distance);  // from your lookup
double currentRPM = encoder.getVelocity();

// Convert RPM → rad/s (WPILib feedforward uses SI units)
double targetRadPerSec = targetRPM * 2 * Math.PI / 60;
double currentRadPerSec = currentRPM * 2 * Math.PI / 60;

// Feedforward
double ffVolts = ff.calculate(targetRadPerSec);

// PID (note: using rad/s)
double pidOutput = pid.calculate(currentRadPerSec, targetRadPerSec);

// Combine
double outputVolts = ffVolts + pidOutput;

// Apply to motor
motor.setVoltage(outputVolts);
```

---

# 3. Units (this trips people up a lot)

WPILib expects:

* Velocity → **rad/s**
* Voltage → **volts**

So ALWAYS convert:

[
RPM \rightarrow rad/s = RPM \cdot \frac{2\pi}{60}
]

---

# 4. Where this code goes

Typically inside your **ShooterSubsystem**:

```java
@Override
public void periodic() {
    updateShooter();
}
```

---

# 5. Lookup Table (WPILib way)

Use:

```java
InterpolatingDoubleTreeMap shooterMap = new InterpolatingDoubleTreeMap();

shooterMap.put(2.0, 2500.0);
shooterMap.put(3.0, 3200.0);
shooterMap.put(4.0, 4000.0);
```

Then:

```java
double targetRPM = shooterMap.get(distance);
```

---

# 6. Tuning Order (super important)

### Step 1: Feedforward first

Temporarily disable PID:

```java
motor.setVoltage(ff.calculate(targetRadPerSec));
```

Tune:

* kS
* kV

Goal:

* Shooter reaches close to target RPM on its own

---

### Step 2: Add P

```java
kP ≈ small (start like 0.001–0.01 depending on units)
```

Increase until:

* Fast response
* No oscillation

---

### Step 3:

* kI = 0 (almost always)
* kD = 0 (rare for shooters)

---

# 7. Shot Readiness Check

Add this:

```java
boolean atSpeed = Math.abs(targetRPM - currentRPM) < 100;
```

Only shoot when true.

---

# 8. Handling Battery Voltage Drop

This is WHY `setVoltage()` matters.

If you used:

```java
motor.set(percentOutput);
```

Your shooter would slow down when battery drops.

Voltage control keeps it consistent.

---

# 9. Optional: Acceleration Limiting

To avoid aggressive jumps:

```java
targetRPM = slewRateLimiter.calculate(targetRPM);
```

Helps stability when distance changes quickly.

---

# 10. Common Pitfalls

### ❌ Forgetting unit conversion

→ causes totally broken tuning

---

### ❌ PID too high

→ oscillation, inconsistent shots

---

### ❌ Bad encoder scaling

→ RPM readings wrong = everything wrong

---

# 11. Clean Structure (what good code looks like)

```java
public void setTargetDistance(double distance) {
    targetRPM = shooterMap.get(distance);
}

public void updateShooter() {
    double currentRPM = encoder.getVelocity();

    double targetRad = rpmToRad(targetRPM);
    double currentRad = rpmToRad(currentRPM);

    double ffVolts = ff.calculate(targetRad);
    double pidVolts = pid.calculate(currentRad, targetRad);

    motor.setVoltage(ffVolts + pidVolts);
}
```

---

# If you want to push this further

I can help you:

* Tune real kS/kV from your robot data
* Add automatic shooting while driving
* Simulate shot trajectories with your exact geometry
* Build a full command-based shooter system

Just tell me what hardware you’re running (NEO? Falcon? encoder type), and I’ll dial it in with you.
