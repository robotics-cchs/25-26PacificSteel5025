Here’s your content cleaned up into proper **Markdown with LaTeX formatting**:

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

$$
\text{error} = \text{target RPM} - \text{current RPM}
$$

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

$$
\text{Output} = \text{Feedforward} + \text{PID}
$$

---

## Feedforward (does most of the work)

This predicts how much power you need:

$$
V = k_S + k_V \cdot \omega + k_A \cdot \alpha
$$

For a shooter, acceleration is small, so:

$$
V \approx k_S + k_V \cdot \omega
$$

Where:

* $k_S$: static friction
* $k_V$: volts per RPM

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

**Goal:**

* Motor reaches near target RPM **without PID**

---

### Step 2: Add small P

* Increase P until:

  * Fast response
  * No oscillation

---

### Step 3: Usually

* $I = 0$ (or very tiny)
* $D = 0$ (rarely needed for shooters)

---

# 6. What happens when you shoot

When a note enters:

* RPM drops suddenly
* PID kicks in to recover

Good tuning = fast recovery

---

# 7. When to allow shooting

Only fire when:

$$
|\text{error}| < \text{tolerance}
$$

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

* Update setpoint every cycle (20 ms)

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

### Objects

```java
PIDController pid = new PIDController(kP, kI, kD);
SimpleMotorFeedforward ff = new SimpleMotorFeedforward(kS, kV, kA);
```

### Sensor

* Encoder that gives **velocity (RPM or rad/s)**

---

# 2. Your Control Loop (runs every 20 ms)

```java
double targetRPM = shooterMap.get(distance);
double currentRPM = encoder.getVelocity();

// Convert RPM → rad/s
double targetRadPerSec = targetRPM * 2 * Math.PI / 60;
double currentRadPerSec = currentRPM * 2 * Math.PI / 60;

// Feedforward
double ffVolts = ff.calculate(targetRadPerSec);

// PID
double pidOutput = pid.calculate(currentRadPerSec, targetRadPerSec);

// Combine
double outputVolts = ffVolts + pidOutput;

// Apply
motor.setVoltage(outputVolts);
```

---

# 3. Units (this trips people up a lot)

WPILib expects:

* Velocity → **rad/s**
* Voltage → **volts**

So ALWAYS convert:

$$
\text{RPM} \rightarrow \text{rad/s} = \text{RPM} \cdot \frac{2\pi}{60}
$$

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

```java
InterpolatingDoubleTreeMap shooterMap = new InterpolatingDoubleTreeMap();

shooterMap.put(2.0, 2500.0);
shooterMap.put(3.0, 3200.0);
shooterMap.put(4.0, 4000.0);
```

```java
double targetRPM = shooterMap.get(distance);
```

---

# 6. Tuning Order (super important)

### Step 1: Feedforward first

```java
motor.setVoltage(ff.calculate(targetRadPerSec));
```

Tune:

* $k_S$
* $k_V$

Goal:

* Shooter reaches close to target RPM on its own

---

### Step 2: Add P

$$
k_P \approx \text{small (e.g., 0.001–0.01)}
$$

Increase until:

* Fast response
* No oscillation

---

### Step 3

* $k_I = 0$ (almost always)
* $k_D = 0$ (rare for shooters)

---

# 7. Shot Readiness Check

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

```java
targetRPM = slewRateLimiter.calculate(targetRPM);
```

Helps stability when distance changes quickly.

---

# 10. Common Pitfalls

### ❌ Forgetting unit conversion

→ causes totally broken tuning

### ❌ PID too high

→ oscillation, inconsistent shots

### ❌ Bad encoder scaling

→ wrong RPM = everything wrong

---

# 11. Clean Structure

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

If you want, I can also convert this into a **clean PDF**, **Notion doc**, or **commented WPILib class file**.


Tuning **kS (static friction)** and **kV (velocity gain)** in FRC is all about characterizing how your mechanism responds to voltage. These are part of the feedforward model used in WPILib (`SimpleMotorFeedforward`), and getting them right makes your control much smoother and more predictable.

---

## 🔧 What kS and kV mean

* **kS (static gain)**: Voltage needed to *just start moving* (overcome friction)
* **kV (velocity gain)**: Voltage needed to maintain a given velocity

The basic model is:

```
Voltage = kS * sign(velocity) + kV * velocity (+ kA * acceleration)
```

---

## 🚀 Best Method: Use SysId (Recommended)

The easiest and most accurate way is using **WPILib SysId**.

### Steps:

1. Install SysId (comes with WPILib tools)
2. Run quasistatic and dynamic tests on your robot
3. Export the data
4. Analyze → it gives you kS, kV (and kA)

This is what most competitive teams use.

---

## 🧪 Manual Method (if you want to do it yourself)

### 1. Find kS (static friction)

* Slowly increase voltage until the mechanism *just starts moving*
* Record that voltage

👉 Do this in both directions and average:

```
kS ≈ (forward_start + reverse_start) / 2
```

---

### 2. Find kV (velocity gain)

* Run motor at several steady speeds
* Measure:

  * Applied voltage
  * Resulting velocity (encoder)

Then compute:

```
kV ≈ (Voltage - kS) / velocity
```

Do this for multiple speeds and average the results.

---

## 📊 Practical Tips

* Use **real units** (meters/sec, radians/sec) — WPILib expects this
* Make sure battery is reasonably charged
* Disable PID when measuring (just apply raw voltage)
* Log data for better accuracy (Shuffleboard / AdvantageScope)

---

## ⚠️ Common Mistakes

* ❌ Not accounting for kS → leads to sluggish starts
* ❌ Using percent output instead of volts
* ❌ Noisy velocity readings → ruins kV
* ❌ Not testing both directions

---

## 💡 When to care about kA

* For arms, elevators, or fast acceleration → include kA
* For simple drivetrains → kS + kV is often enough

---

## 🧠 Rule of Thumb

If you’re short on time:

* Use SysId once
* Plug values into `SimpleMotorFeedforward`
* Tune PID *after*

---

If you tell me what mechanism you’re tuning (drivetrain, shooter, arm, etc.), I can give more specific target ranges and tuning tricks.
