$ErrorActionPreference = 'Stop'
Set-Location $PSScriptRoot

if (-not $env:JAVA_HOME -or -not (Test-Path "$env:JAVA_HOME\release") -or
    (Get-Content "$env:JAVA_HOME\release" -Raw) -notmatch 'JAVA_VERSION="(17|21)\.') {
    $jdk = Get-ChildItem "$env:USERPROFILE\.jdks" -Directory -ErrorAction SilentlyContinue |
        Where-Object { (Test-Path "$($_.FullName)\release") -and
            (Get-Content "$($_.FullName)\release" -Raw) -match 'JAVA_VERSION="(17|21)\.' } |
        Select-Object -First 1
    if (-not $jdk) { throw 'Configura JAVA_HOME con un JDK 17 o 21.' }
    $env:JAVA_HOME = $jdk.FullName
}

if (-not $env:JWT_SECRET) {
    $bytes = New-Object byte[] 48
    $generator = [System.Security.Cryptography.RandomNumberGenerator]::Create()
    $generator.GetBytes($bytes)
    $generator.Dispose()
    $env:JWT_SECRET = [Convert]::ToBase64String($bytes)
}
$env:SPRING_PROFILES_ACTIVE = 'local'
& "$PSScriptRoot\mvnw.cmd" spring-boot:run
exit $LASTEXITCODE
