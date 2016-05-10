rem From Eytan:
rem This is wat I have in my notes.... let me knoiw if it works
rem added a new registry key to handle righting to eventvwr
rem HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\services\eventlog\Application
rem String Value CustomSD O:BAG:SYD:(A;;0×3;;;AU)
rem Set Permissions to allow Everyone full access

rem example REG ADD HKLM\Software\MyCo /v Path /t REG_EXPAND_SZ /d ^%systemroot^%


reg add /f  HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\services\eventlog\Application  /d  "CustomSD O:BAG:SYD:(A;;0x3;;;AU)"
echo HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\services\eventlog\Application [7] > tmp.ini
regini tmp.ini


rem ----------------------------------------------------------
rem HKLM\SYSTEM\CurrentControlSet\services\eventlog

rem HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Services\EventLog\Securit


rem echo HKLM\SYSTEM\CurrentControlSet\services\eventlog [8 8 8 8] > tmp.ini
rem regini tmp.ini

rem echo HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\services\eventlog  [7 7 7 7 ] > tmp.ini
rem regini tmp.ini
rem echo HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Services\EventLog\Security  [7 7 7 7 ] > tmp.ini
rem regini tmp.ini
rem echo HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Services\EventLog\Security\Security  [7 7 7 7 ] > tmp.ini
rem regini tmp.ini

