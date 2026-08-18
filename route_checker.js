const fs = require('fs');

const portalConfig = fs.readFileSync('frontend/src/config/portalConfig.js', 'utf8');
const appJsx = fs.readFileSync('frontend/src/App.jsx', 'utf8');

const advertisedPaths = [];
const configMatches = portalConfig.matchAll(/path:\s*'([^']+)'/g);
for (const match of configMatches) {
    advertisedPaths.push(match[1]);
}

const appPaths = [];
const appMatches = appJsx.matchAll(/path="([^"]+)"/g);
const basePaths = new Set();
for (const match of appMatches) {
    const p = match[1];
    if (p.startsWith('/')) {
        appPaths.push(p);
        basePaths.add(p);
    }
}

// Find nested routes inside RoleRoutes
let currentBase = "";
const lines = appJsx.split('\n');
for (const line of lines) {
    const baseMatch = line.match(/path="(\/[^"]+)"/);
    if (baseMatch) {
        currentBase = baseMatch[1];
    }
    const relativeMatch = line.match(/<Route path="([^"\/]+)"/);
    if (relativeMatch && currentBase) {
        appPaths.push(currentBase + '/' + relativeMatch[1]);
    }
}

const missing = advertisedPaths.filter(p => !appPaths.includes(p) && !p.startsWith('http'));
console.log("Paths advertised but missing in App.jsx:");
console.log(missing.join('\n'));
