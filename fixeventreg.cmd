echo HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\services\eventlog  [7] > tmp.ini
regini tmp.ini
echo HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Services\EventLog\Security  [7] > tmp.ini
regini tmp.ini
echo HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Services\EventLog\Security\Security  [7] > tmp.ini
regini tmp.ini

reg add /f  HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Services\EventLog\Application  /d  "CustomSD O:BAG:SYD:(A;;0x3;;;AU)"
echo  HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Services\EventLog\Application [7] > tmp.ini
regini tmp.ini
