@echo off
setlocal enabledelayedexpansion

echo Installing Maven...

REM Download Maven
echo Downloading Maven 3.9.6...
powershell -Command "(New-Object System.Net.ServicePointManager).SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; (New-Object System.Net.WebClient).DownloadFile('https://archive.apache.org/dist/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.zip', 'C:\maven.zip')"

REM Extract Maven
echo Extracting Maven...
powershell -Command "Expand-Archive -Path 'C:\maven.zip' -DestinationPath 'C:\' -Force"
ren "C:\apache-maven-3.9.6" "maven"

REM Add to PATH
echo Adding Maven to PATH...
setx MAVEN_HOME "C:\maven"
setx PATH "%PATH%;C:\maven\bin"

echo Maven installed successfully!
echo Please restart PowerShell and run: mvn -version
pause
