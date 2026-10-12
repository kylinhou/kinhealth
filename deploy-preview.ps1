# 安康记 Demo 快速发布到 Vercel 脚本
param(
  [string]$Token = $env:VERCEL_TOKEN
)

if (-not $Token -and (Test-Path ".env.local")) {
    $envContent = Get-Content ".env.local"
    foreach ($line in $envContent) {
        if ($line -match "VERCEL_TOKEN=(.*)") {
            $Token = $matches[1].Trim()
        }
    }
}

if (-not $Token) {
    Write-Host "❌ 未检测到 VERCEL_TOKEN，请设置环境变量或在 .env.local 中配置 VERCEL_TOKEN=xxx" -ForegroundColor Red
    exit 1
}

# 调用跨平台 node 脚本
node deploy.js
