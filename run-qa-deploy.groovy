// support routines


//convert list to space spereated string
def ltos(args) 
{
	ret = ""
	if (args.class == java.util.ArrayList)
	{
		sz = args.size()
		for(i = 0 ; i < sz; i++)
		{
			ret += args[i].toString();
			if ((i + 1) < sz)
			{
				ret +=" " 
			}
			
		}
	}
	else
	{
		ret = args.toString();
	}

	return ret;
}


// conver forward slash to backslash to avoid excess escaping
def dospath(path1) 
{ 
	retpath =  path1.replace("/", "\\")
	return retpath
}

// put quotes around string (with spaces)
def quote(s)
{
	rets = "\"" + s.toString() + "\""
	return rets
}

// run mstest on given dll
def mstest(testdll)
{
	exitcode = 0
	mstestpath=this.quote(this.dospath("C:/Program Files (x86)/Microsoft Visual Studio 12.0/Common7/IDE/MSTest.exe"))
	tdll =this.dospath(testdll.toString())
	mstestcmd=mstestpath + "/testcontainer:"+tdll 
	echo "Running mstest..."
	echo mstestcmd
	try
	{
		bat mstestcmd
	}
	catch(Exception e)
	{
	    echo "error in test suite " +  mstestcmd
	    exitcode = 1
	   // to force error on mstest uncomment below
	   // bat "exit " + exitcode.toString();
	}
	return exitcode // to allow dieing later
}

// run msbuild with given args
def msbuild(args){
	msbuildpath=quote(dospath("C:/Program Files (x86)/MSBuild/12.0/Bin/MSBuild.exe"))
	msbuildcmd = msbuildpath +" " +  ltos(args)
	echo "Running Msbuild...."
	echo msbuildcmd
	bat msbuildcmd
	echo "msbuild done"
}

def msdeploy(args){
	msdeploypath=quote(dospath("C:/Program Files/IIS/Microsoft Web Deploy V3/msdeploy.exe"))
	msdeploycmd = msdeploypath +" " +  ltos(args)
	echo "Running MsDeploy...."
	echo msdeploycmd
	bat msdeploycmd
	echo "mmsbuildc done"
}

// run nuget with args
def nuget(args) 
{
	nugetpath=dospath('D:/Installs/nuget.exe')
	nugetcmd = nugetpath + " " + ltos(args)
	echo "Running Nuget...."
	echo nugetcmd
	bat nugetcmd
	echo "nuget done"
}

def publish(proj, pub)
{
	publishparams=" /p:DeployOnBuild=true /p:PublishProfile="+pub
	prj = dospath(proj)
	msbuild([prj,  publishparams])
}

def trxlist(tr)
{
	echo "in trxlist"
	d = pwd().toString() +"\\"+ tr.toString()
	echo d
	def baseDir = new File(d);
	files = baseDir.listFiles();
	sz = files.size()
	ret = []
	for (i = 0 ; i < sz; i++)
	{
	    fn =  files[i].toString()
	    if (fn.endsWith(".trx"))
	    {
		echo "found " + fn
		ret << fn
            }
	}
	echo "returning  " + ret.toString()
	return ret
}

def xmlname(trxname)
{
	return trxname.replace("trx", "xml")
}

def trxtoxml(trxname)
{
// sample of what we need 
// ***** bat 'C:\\bin\\msxsl.exe TestResult.trx "C:\\Jenkins\\plugins\\mstest\\WEB-INF\\mstest-to-junit_withOutput.xsl" -o %cd%\\Build-%BUILD_NUMBER%\\JUnitLikeResultsOutputFile1.xml' 

	msxl = dospath("D:/Installs/msxsl.exe")
	xsl = dospath("D:/Installs/mstest-to-junit.xsl")
	trx = quote(trxname)
	xml = quote(xmlname(trxname))
	
	cmd = msxl + " " + trx + " " + xsl  +  " -o " + xml 
	echo cmd 
	bat cmd

}

def trxlstoxmls(trxl)
{
	for(i = 0 ; i < trxl.size() ; i++)
	{
		trxtoxml(trxl[i])
	}
}

def nowstring()
{
	now = new Date()
	year = (now.year + 1900 )
	month =  (now.month + 1 ).toString().padLeft(2,"0")
	date = now.date.toString().padLeft(2,"0")
	hour = now.getHours().toString().padLeft(2,"0")
	min = now.getMinutes().toString().padLeft(2,"0")
	sec = now.getSeconds().toString().padLeft(2,"0")
	ret = year + "-" + month + "-" + date + "-" + hour+ "-" + min  + "-" + sec
	return ret
}

def puts3(prefix,file)
{
	bucket="s3://idtq-carrier-dot-net-deploy"
	project="carrier-rn-admin"

	cmd=cmdprefix+ "s3 cp " + file + " " + bucket + "/" + project + "/" +  prefix +"/" + file + "\n"

	echo "copying to s3 ...."
	echo cmd
	bat cmd
}


def zipandstore(now, zipname, dir)
{
	bat 'zip -r ' + zipname + ' publish'
	archive zipname
	puts3("latest", zipname)
	puts3(now, zipname)

}


def deployapi()
{


	auth = " /U:deploy /P:headpizza1! "
	auth = " "
	cmdfile=dospath("Rn.Admin/Rn.Admin.WebApi/publish/QA/Rn.Admin.WebApi.deploy.cmd")
	host= "qa1web2012.am.idtcorp.net"

	cmd=cmdfile + auth + "/t /m:" + host
	echo cmd
	bat cmd
	
}

def deployweb()
{
	auth = " /U:deploy /P:headpizza1! "
	auth = " " 
	cmdfile=dospath("Rn.Admin/Rn.Admin.WebConsole/publish/QA/Rn.Admin.WebConsole.deploy.cmd")
	host= "qa1web2012.am.idtcorp.net"
	cmd=cmdfile + auth  + "/t /m:" + host
	echo cmd
	bat cmd
}


node {
	stage 'Cleanup'
	y=nowstring()
	echo y

	deleteDir()

	stage 'Check Out'

	stage 'Build'
	slnfile='Rn.Admin/Rn.Admin.sln'

	msbuild([slnfile, '/t:clean'])
	nuget(['restore', slnfile])
	msbuild([slnfile, "/p:Configuration=QA"])
	msbuild([slnfile, "/p:Configuration=Release"])
	msbuild([slnfile, "/p:Configuration=Debug"])

	stage 'Test'
	mstest("Rn.Admin/Rn.Admin.Tests/bin/QA/Rn.Admin.Tests.dll")
	echo "testing disabled for now"

	stage 'Publish Deploy Packages'
	echo "Running Publish"
	// publish Webapi
	publish('Rn.Admin/Rn.Admin.WebApi/Rn.Admin.WebApi.csproj', 'RnAdminApiProd.pubxml')
	publish('Rn.Admin/Rn.Admin.WebApi/Rn.Admin.WebApi.csproj', 'RnAdminApiQA.pubxml')
	publish('Rn.Admin/Rn.Admin.WebApi/Rn.Admin.WebApi.csproj', 'RnAdminApiDev.pubxml')


	// publish Webconsole
	publish('Rn.Admin/Rn.Admin.WebConsole/Rn.Admin.WebConsole.csproj', 'RnAdminWebDev.pubxml')
	publish('Rn.Admin/Rn.Admin.WebConsole/Rn.Admin.WebConsole.csproj', 'RnAdminProd.pubxml')
	publish('Rn.Admin/Rn.Admin.WebConsole/Rn.Admin.WebConsole.csproj', 'RnAdminWebQA.pubxml')



	stage 'Post Trx Results to Jenknis'
	echo "Trx files"
	trxl =  trxlist("TestResults")
	echo trxl.toString()
	trxlstoxmls(trxl)
	step([$class: 'JUnitResultArchiver', testResults: 'TestResults/*.xml'])

	stage 'Zip and Store'
	now=nowstring()
	// publish Webconsole

echo "elazar zip it 1"
	dir('Rn.Admin/Rn.Admin.WebApi')
	{
		zipandstore(now,'publish-webapi.zip','publish')
	}
echo "elazar zip it 2"
	dir('Rn.Admin/Rn.Admin.WebConsole')
	{
		zipandstore(now,'publish-webconsole.zip','publish')
	}
	stage 'Push to QA'
	echo "do msdeploy"
	deployweb()
	deployapi()
}


