d:
cd d:\share
echo 1 >> d:\mylog.txt
set __COMPAT_LAYER=RunAsInvoker
echo 2 >> d:\mylog.txt
unzip winnt_12102_client32.zip
echo 3 >> d:\mylog.txt
unzip winx64_12102_client.zip
echo 4 >> d:\mylog.txt
client32\setup.exe -silent -nowelcome -nowait -waitforcompletion -responseFile d:\share\client32.rsp
echo 5 >> d:\mylog.txt
client\setup.exe   -silent -nowelcome -nowait -waitforcompletion -responseFile d:\share\client64.rsp
echo 6 >> d:\mylog.txt
exit 0

