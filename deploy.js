const fs = require('fs');
const path = require('path');
const { execSync } = require('child_process');
const os = require('os');

// 优先读取环境变量，或本地 .env.local 文件
let TOKEN = process.env.VERCEL_TOKEN;
const envFile = path.resolve(__dirname, '.env.local');
if (!TOKEN && fs.existsSync(envFile)) {
  const content = fs.readFileSync(envFile, 'utf8');
  const m = content.match(/VERCEL_TOKEN=(.*)/);
  if (m) TOKEN = m[1].trim();
}

if (!TOKEN) {
  console.error('❌ 未检测到 VERCEL_TOKEN，请设置环境变量或在 .env.local 中配置 VERCEL_TOKEN=xxx');
  process.exit(1);
}

const sourceDir = path.resolve(__dirname, 'demos');
const tempDir = path.join(os.tmpdir(), 'ankangji_demos');

console.log('🚀 正在同步发布文件...');
if (fs.existsSync(tempDir)) {
  fs.rmSync(tempDir, { recursive: true, force: true });
}
fs.cpSync(sourceDir, tempDir, {
  recursive: true,
  filter: (src) => !src.includes('.vercel')
});

console.log('📦 正在上传至 Vercel (纯净模式)...');
const cmd = `npx vercel deploy "${tempDir}" --prod --yes --token "${TOKEN}" --no-wait --format json`;
let output = '';
try {
  output = execSync(cmd, { encoding: 'utf8', stdio: ['pipe', 'pipe', 'pipe'] });
} catch (e) {
  output = (e.stdout || '') + '\n' + (e.stderr || '');
}

let deployId = null;
const m = output.match(/"id":\s*"([^"]+)"/);
if (m) {
  deployId = m[1];
}

console.log('部署完成，ID:', deployId);

if (deployId) {
  fetch(`https://api.vercel.com/v2/deployments/${deployId}/aliases`, {
    method: 'POST',
    headers: {
      Authorization: `Bearer ${TOKEN}`,
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ alias: 'ankangji-preview.vercel.app' })
  })
    .then(r => r.json())
    .then(data => {
      console.log('🎉 部署成功！主域名已绑定');
      console.log('🌐 线上地址: https://ankangji-preview.vercel.app');
    })
    .catch(err => {
      console.error('绑定域名失败:', err);
    });
} else {
  console.log('原始输出:', output);
}
