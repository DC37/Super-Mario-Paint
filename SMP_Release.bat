@echo on
set version=1.4.3
set t=Super Mario Paint
title %t% %version% Release Script


set src_folder=%cd%
set dst_folder=%cd%\Versions\SMP%version%


if exist dst_folder echo DIR_EXISTS
else MKDIR dst_folder
robocopy "%src_folder%\sprites" "%dst_folder%\sprites" /MIR /s /e
robocopy "%src_folder%\jfx11" "%dst_folder%\jfx11" /MIR /s /e


for /f "tokens=*" %%i in (flist.txt) DO (
    xcopy "%src_folder%\%%i" "%dst_folder%" /Y
)

xcopy "%src_folder%\SMPv%version%.jar" "%dst_folder%" /Y
pause