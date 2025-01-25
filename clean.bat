@echo off

if exist "out" (
    rmdir /s /q "out"
)

if exist "target" (
    rmdir /s /q "target"
)