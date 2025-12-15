@echo off
echo Starting RevCart Backend...
cd /d "%~dp0"

echo Cleaning target directory...
if exist target rmdir /s /q target
mkdir target\classes

echo Compiling Java files...
"C:\Program Files\Java\jdk-23\bin\javac" -cp "src\main\resources;lib\*" -d "target\classes" src\main\java\com\revcart\RevCartApplication.java

if %ERRORLEVEL% NEQ 0 (
    echo Compilation failed!
    pause
    exit /b 1
)

echo Running application...
"C:\Program Files\Java\jdk-23\bin\java" -cp "target\classes;lib\*" com.revcart.RevCartApplication

pause