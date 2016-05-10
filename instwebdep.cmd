d:
cd d:\share
rem msiexec /i WebPlatformInstaller_x86_en-US.msi /passive /quiet /l*  logfile1.txt
rem type logfile1.txt
msiexec /i WebPlatformInstaller_amd64_en-US.msi /passive /quiet /l* logfile2.txt
type logfile2.txt
"C:\Program Files\Microsoft\Web Platform Installer\webpicmd" /install /products:wdeploy /accepteula

