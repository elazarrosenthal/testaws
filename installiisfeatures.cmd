rem dism /online /enable-feature /all 


dism /online /enable-feature /all /FeatureName:IIS-WebServerRole 
dism /online /enable-feature /all /FeatureName:IIS-WebServer 
dism /online /enable-feature /all /FeatureName:IIS-CommonHttpFeatures 
dism /online /enable-feature /all /FeatureName:IIS-Security 
dism /online /enable-feature /all /FeatureName:IIS-RequestFiltering 
dism /online /enable-feature /all /FeatureName:IIS-StaticContent 
dism /online /enable-feature /all /FeatureName:IIS-DefaultDocument 
dism /online /enable-feature /all /FeatureName:IIS-DirectoryBrowsing 
dism /online /enable-feature /all /FeatureName:IIS-HttpErrors 
dism /online /enable-feature /all /FeatureName:IIS-HttpRedirect 
dism /online /enable-feature /all /FeatureName:IIS-WebDAV 
dism /online /enable-feature /all /FeatureName:IIS-ApplicationDevelopment 
dism /online /enable-feature /all /FeatureName:IIS-WebSockets 
dism /online /enable-feature /all /FeatureName:IIS-ApplicationInit 
dism /online /enable-feature /all /FeatureName:IIS-NetFxExtensibility 
dism /online /enable-feature /all /FeatureName:IIS-NetFxExtensibility45 
dism /online /enable-feature /all /FeatureName:IIS-ISAPIExtensions 
dism /online /enable-feature /all /FeatureName:IIS-ISAPIFilter 
dism /online /enable-feature /all /FeatureName:IIS-ASPNET 
dism /online /enable-feature /all /FeatureName:IIS-ASPNET45 
dism /online /enable-feature /all /FeatureName:IIS-ASP 
dism /online /enable-feature /all /FeatureName:IIS-CGI 
dism /online /enable-feature /all /FeatureName:IIS-ServerSideIncludes 
dism /online /enable-feature /all /FeatureName:IIS-HealthAndDiagnostics 
dism /online /enable-feature /all /FeatureName:IIS-HttpLogging 
dism /online /enable-feature /all /FeatureName:IIS-LoggingLibraries 
dism /online /enable-feature /all /FeatureName:IIS-RequestMonitor 
dism /online /enable-feature /all /FeatureName:IIS-HttpTracing 
dism /online /enable-feature /all /FeatureName:IIS-CustomLogging 
dism /online /enable-feature /all /FeatureName:IIS-ODBCLogging 
dism /online /enable-feature /all /FeatureName:IIS-CertProvider 
dism /online /enable-feature /all /FeatureName:IIS-BasicAuthentication 
dism /online /enable-feature /all /FeatureName:IIS-WindowsAuthentication 
dism /online /enable-feature /all /FeatureName:IIS-DigestAuthentication 
dism /online /enable-feature /all /FeatureName:IIS-ClientCertificateMappingAuthentication 
dism /online /enable-feature /all /FeatureName:IIS-IISCertificateMappingAuthentication 
dism /online /enable-feature /all /FeatureName:IIS-URLAuthorization 
dism /online /enable-feature /all /FeatureName:IIS-IPSecurity 
dism /online /enable-feature /all /FeatureName:IIS-Performance 
dism /online /enable-feature /all /FeatureName:IIS-HttpCompressionStatic 
dism /online /enable-feature /all /FeatureName:IIS-HttpCompressionDynamic 
dism /online /enable-feature /all /FeatureName:IIS-WebServerManagementTools 
dism /online /enable-feature /all /FeatureName:IIS-ManagementConsole 
dism /online /enable-feature /all /FeatureName:IIS-LegacySnapIn 
dism /online /enable-feature /all /FeatureName:IIS-ManagementScriptingTools 
dism /online /enable-feature /all /FeatureName:IIS-ManagementService 
dism /online /enable-feature /all /FeatureName:IIS-IIS6ManagementCompatibility 
dism /online /enable-feature /all /FeatureName:IIS-Metabase 
dism /online /enable-feature /all /FeatureName:IIS-WMICompatibility 
dism /online /enable-feature /all /FeatureName:IIS-LegacyScripts 
dism /online /enable-feature /all /FeatureName:IIS-FTPServer 
dism /online /enable-feature /all /FeatureName:IIS-FTPSvc 
dism /online /enable-feature /all /FeatureName:IIS-FTPExtensibility 
dism /online /enable-feature /all /FeatureName:IIS-HostableWebCore 
dism /online /enable-feature /all /FeatureName:Microsoft-Windows-Web-Services-for-Management-IIS-Extension  



Start /w pkgmgr /iu:IIS-WebServerRole;IIS-WebServer;IIS-CommonHttpFeatures;IIS-StaticContent;IIS-DefaultDocument;IIS-DirectoryBrowsing;IIS-HttpErrors;IIS-ApplicationDevelopment;IIS-ASPNET;IIS-NetFxExtensibility;IIS-ISAPIExtensions;IIS-ISAPIFilter;IIS-HealthAndDiagnostics;IIS-HttpLogging;IIS-LoggingLibraries;IIS-RequestMonitor;IIS-Security;IIS-RequestFiltering;IIS-HttpCompressionStatic;IIS-WebServerManagementTools;IIS-ManagementConsole;WAS-WindowsActivationService;WAS-ProcessModel;WAS-NetFxEnvironment;WAS-ConfigurationAPI

exit 0
