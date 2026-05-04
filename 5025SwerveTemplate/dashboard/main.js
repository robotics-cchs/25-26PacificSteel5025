import { NetworkTables, NetworkTablesTypeInfos } from 'ntcore-ts-client';

// 1. Initialize NT4 Client
// For offline testing, we point to localhost. 
// When ready for competition, change this to your team number (e.g., '10.50.25.2' or '5025')
const ROBOT_URI = '10.50.25.2'; // Team 5025 Robot IP
const nt4 = NetworkTables.getInstanceByURI(ROBOT_URI, 5810);

// 2. DOM Elements
const connectionDot = document.getElementById('connection-dot');
const connectionText = document.getElementById('connection-text');

const poseX = document.getElementById('pose-x');
const poseY = document.getElementById('pose-y');
const poseRot = document.getElementById('pose-rot');

const mechanismState = document.getElementById('mechanism-state');

const visionTags = document.getElementById('vision-tags');
const visionLatency = document.getElementById('vision-latency');
const visionLatencyBar = document.getElementById('vision-latency-bar');

const autoSelector = document.getElementById('auto-selector');

// Canvas and Control Elements
const canvas = document.getElementById('robot-map');
const ctx = canvas ? canvas.getContext('2d') : null;

const btnShootLeft = document.getElementById('btn-shoot-left');
const btnShootRight = document.getElementById('btn-shoot-right');
const btnShootMid = document.getElementById('btn-shoot-mid');
const btnIdle = document.getElementById('btn-idle');

// 3. Connect Event Listeners
nt4.addRobotConnectionListener((connected) => {
  if (connected) {
    connectionDot.classList.remove('disconnected');
    connectionDot.classList.add('connected');
    connectionText.textContent = 'Connected';
  } else {
    connectionDot.classList.remove('connected');
    connectionDot.classList.add('disconnected');
    connectionText.textContent = 'Disconnected';
  }
}, true);

// 4. Data Subscriptions

// Subscribe to Robot Pose (Assuming an array [x, y, rot])
// Subscribing to the DoubleArray that Telemetry.java publishes for Field2d
const poseSub = nt4.createTopic('/Pose/robotPose', NetworkTablesTypeInfos.kDoubleArray).subscribe((poseData) => {
  if (poseData && poseData.length >= 3) {
    const x = poseData[0];
    const y = poseData[1];
    const rot = poseData[2];
    
    poseX.textContent = x.toFixed(2);
    poseY.textContent = y.toFixed(2);
    poseRot.textContent = rot.toFixed(2);
    
    // Draw 2D Robot Map
    if (ctx && canvas) {
      ctx.clearRect(0, 0, canvas.width, canvas.height);
      
      // Field dimensions: 16.54m x 8.07m
      const scaleX = canvas.width / 16.54;
      const scaleY = canvas.height / 8.07;
      
      const pixelX = x * scaleX;
      const pixelY = canvas.height - (y * scaleY);
      
      ctx.save();
      ctx.translate(pixelX, pixelY);
      // Field rot is CCW, canvas rot is CW, so negative rot
      ctx.rotate(-rot * Math.PI / 180);
      
      // Draw Robot Bumper (e.g., 0.9m x 0.9m robot = ~35x35 pixels)
      const robotSize = 0.9 * scaleX; 
      ctx.fillStyle = "rgba(0, 240, 255, 0.5)";
      ctx.strokeStyle = "#00f0ff";
      ctx.lineWidth = 2;
      ctx.fillRect(-robotSize/2, -robotSize/2, robotSize, robotSize);
      ctx.strokeRect(-robotSize/2, -robotSize/2, robotSize, robotSize);
      
      // Draw Front indicator
      ctx.fillStyle = "#ff3366";
      ctx.fillRect(robotSize/2 - 5, -5, 5, 10);
      
      ctx.restore();
    }
  }
}, true);

// Subscribe to Mechanism State
const stateSub = nt4.createTopic('/SmartDashboard/ExampleMechanism/State', NetworkTablesTypeInfos.kString).subscribe((stateStr) => {
  if (stateStr) {
    mechanismState.textContent = stateStr;
    
    // Dynamic styling based on state
    if (stateStr === 'FORWARD' || stateStr === 'SCORING') {
      mechanismState.style.color = 'var(--success-color)';
    } else if (stateStr === 'REVERSE' || stateStr === 'INTAKING') {
      mechanismState.style.color = 'var(--accent-color)';
    } else {
      mechanismState.style.color = 'var(--text-primary)';
    }
  }
}, true);

// Subscribe to Vision Data
// SmartDashboard.putNumber always publishes as Double
const tagsSub = nt4.createTopic('/SmartDashboard/Vision/TagsVisible', NetworkTablesTypeInfos.kDouble).subscribe((tagsCount) => {
  visionTags.textContent = tagsCount !== null ? Math.round(tagsCount) : 0;
}, true);

const latencySub = nt4.createTopic('/SmartDashboard/Vision/LatencyMs', NetworkTablesTypeInfos.kDouble).subscribe((latencyValue) => {
  const latency = latencyValue !== null ? latencyValue : 0;
  visionLatency.textContent = latency.toFixed(1);
  
  // Update progress bar visually
  // Assuming 200ms is "bad" latency
  let barWidth = (latency / 200) * 100;
  if (barWidth > 100) barWidth = 100;
  visionLatencyBar.style.width = `${barWidth}%`;
  
  if (latency < 50) {
    visionLatencyBar.style.backgroundColor = 'var(--success-color)';
  } else if (latency < 120) {
    visionLatencyBar.style.backgroundColor = 'var(--warning-color)';
  } else {
    visionLatencyBar.style.backgroundColor = 'var(--danger-color)';
  }
}, true);

// Subscribe to Auto Chooser Options
const autoOptionsSub = nt4.createTopic('/SmartDashboard/Auto Mode/options', NetworkTablesTypeInfos.kStringArray).subscribe((options) => {
  if (options && Array.isArray(options)) {
    // Save current selection if any
    const currentSelection = autoSelector.value;
    
    // Clear and repopulate
    autoSelector.innerHTML = '';
    options.forEach(opt => {
      const el = document.createElement('option');
      el.value = opt;
      el.textContent = opt;
      autoSelector.appendChild(el);
    });
    
    // Restore selection if it still exists
    if (options.includes(currentSelection)) {
      autoSelector.value = currentSelection;
    }
  }
}, true);

// Listen for selection changes from UI
autoSelector.addEventListener('change', (e) => {
  const selectedAuto = e.target.value;
  nt4.publishTopic('/SmartDashboard/Auto Mode/active', NetworkTablesTypeInfos.kString);
  nt4.addSample('/SmartDashboard/Auto Mode/active', selectedAuto);
});

// Subscribe to active auto to keep UI in sync
const autoActiveSub = nt4.createTopic('/SmartDashboard/Auto Mode/active', NetworkTablesTypeInfos.kString).subscribe((activeAuto) => {
  if (activeAuto && autoSelector.querySelector(`option[value="${activeAuto}"]`)) {
    autoSelector.value = activeAuto;
  }
}, true);

// Wire up control buttons
const publishState = (state) => {
  nt4.publishTopic('/SmartDashboard/DashboardControl/DesiredState', NetworkTablesTypeInfos.kString);
  nt4.addSample('/SmartDashboard/DashboardControl/DesiredState', state);
};

if (btnShootLeft) btnShootLeft.addEventListener('click', () => publishState('SHOOT_LEFT'));
if (btnShootRight) btnShootRight.addEventListener('click', () => publishState('SHOOT_RIGHT'));
if (btnShootMid) btnShootMid.addEventListener('click', () => publishState('SHOOT_MID'));
if (btnIdle) btnIdle.addEventListener('click', () => publishState('IDLE'));

// Start the client
console.log('Starting NT4 Connection...');
nt4.connect();
