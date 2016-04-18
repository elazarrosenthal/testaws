import groovy.json.JsonSlurper


// conver forward slash to backslash to avoid excess escaping
def dospath(path1) 
{ 
	retpath =  path1.replace("/", "\\")
	return retpath
}


def escapeand(path1) 
{ 
	esc =  path1.replace("&",  "^&")
	esc =  esc.replace("%",  "%%")
	return esc
}


// put quotes around string (with spaces)
def quote(s)
{
	rets = "\"" + s.toString() + "\""
	return rets
}



def extractiid(xml)
{
   def jsonSlurper = new JsonSlurper()
   def p = jsonSlurper.parseText(xml)
   ret = new String(p.Instances[0].InstanceId)
   return ret
}

def getimagedata(r)
{
	js = new JsonSlurper()
	p  = js.parseText(r)
	js = null
	iid  = new String(p.Instances[0].InstanceId)
	pip  = new String(p.Instances[0].PrivateIpAddress)
	println iid
	println pip
	js = null
	ret = ["id": iid, "ip":  pip]
	return ret
}

def getamidata(r)
{
        js = new JsonSlurper()
        p  = js.parseText(r)
        amiid  = new String(p.ImageId)
        println amiid;
	js = null
	p = null
        return amiid
}

def getpass(r)
{
        js = new JsonSlurper()
        p  = js.parseText(r)
        amiid  = new String(p.PasswordData)
        println amiid;
	js = null
	p = null
        return amiid
}


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


def aws(envprefix ,args){
	awsexe="c:/Program Files/Amazon/AWSCLI/aws.exe"
	awspath=envprefix + quote(dospath(awsexe))  + " > awstmp.out " 
	awscmd = awspath +" " +  ltos(args)
	echo "Running AWS...."
	echo awscmd
	bat awscmd
	d= readFile 'awstmp.out'
	echo "aws Generated ..."
	echo d
	echo "aws  done"
	return d
}


def makesetenv(raw)
{
echo "m ... 1"
    x0 = raw.replaceAll("\n", "")
echo "m ... 2"
    x1 = x0.replaceAll(";", "\n")
echo "m ... 3"
    x2 = x1.replaceAll("export", "set")
echo "m ... 4"
echo x2
    return x2
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


def wmic(args)
{
	wmicpath="C:/Windows/System32/wbem/WMIC.exe"
	wmiccmd = wmicpath +" " +  ltos(args)
	echo "Running Wmic...."
	echo wmiccmd
	bat wmiccmd
	echo "wmic  done"
}

def wmicexec(ip,user,pass,cmd)
{
	node="/node:" + ip
	u = "/user:" +  user
	p = "/password:\'" +  escapeand(pass) + "\'"
        c1 = "process call create "
        c2 = quote(cmd)
echo c2
        wmic([node,u,p,c1,c2])
}

def adduser(ip, auser, apass, uname, upass)
{
	cmd = "net user /add " + uname + " " + upass 
	wmicexec(ip,auser,apass,cmd)
}

def mkadmin(ip, auser, apass, uname)
{
	cmd = "net localgroup /add  administrators " + uname 
	wmicexec(ip,auser,apass,cmd)
	
}
def mkrdu(ip, auser, apass, uname)
{
	cmd = "net localgroup /add  \'Remote Desktop Users\' " + uname 
	wmicexec(ip,auser,apass,cmd)
	
}

def copyfiles(ip)
{
 dest = "\\\\" + ip + "\\share"
 mount = "net use  " + dest + "  mountit1! /user:share"
 unmount = "net use " +dest + "  /delete"
 echo dest
 echo mount 
 echo unmount
 copy = "copy d:\\InstallPkgs\\*.* " + dest
 echo copy

 bat mount

 bat copy

 bat unmount

}



node{
    stage "1"
    nows =nowstring()
   x =input message: 'enter vars:', parameters: [[$class: 'TextParameterDefinition', defaultValue: '', description: '', name: 'AWSENV']]
    senv = makesetenv(x)
    echo "senv =  " +senv

   // this was a test --- now its anoying 
   // aws1 =  aws(senv, ["ec2", "describe-instances"])
   // echo "aws 1 = " + aws1

// create new instace and get id


    bdev=' --block-device-mappings [ { \"DeviceName\": \"/dev/sda1\", \"Ebs\": { \"DeleteOnTermination\": true, \"VolumeSize\": 40 } }, { \"DeviceName\": \"xvdb\", \"Ebs\": { \"DeleteOnTermination\": true, \"VolumeSize\": 30 } } ]  '
   writeFile file: 'input.json', text: '[ { \"DeviceName\": \"/dev/sda1\", \"Ebs\": { \"DeleteOnTermination\": true, \"VolumeSize\": 40 } }, { \"DeviceName\": \"xvdb\", \"Ebs\": { \"DeleteOnTermination\": true, \"VolumeSize\": 30 } } ]  '

   // aws2 = aws(senv, [ " ec2 run-instances   --image-id ami-3d787d57 --count 1 --instance-type t2.micro --key-name ", "elazartest1",  " --security-group-ids sg-27f9af42 --subnet-id subnet-96d526e1  --block-device-mappings", "file://input.json"])
   // aws2 = aws(senv, [ " ec2 run-instances   --image-id ami-3d787d57 --count 1 --instance-type m3.medium --key-name ", "elazartest1",  " --security-group-ids sg-27f9af42 --subnet-id subnet-96d526e1  --block-device-mappings", "file://input.json"])
   aws2 = aws(senv, [ " ec2 run-instances   --image-id ami-3d787d57 --count 1 --instance-type m4.large --key-name ", "elazartest1",  " --security-group-ids sg-27f9af42 --subnet-id subnet-96d526e1  --block-device-mappings", "file://input.json"])
  // ii = extractiid(aws2)
    idat =    getimagedata(aws2)
    echo "idat = " + idat.toString()
    ii =  idat.id
    ip = idat.ip




// tag instace
   echo "tagging"
   aws3 = aws(senv, [" ec2 create-tags --resources  " ,  ii , "  --tags Key=Name,Value=ElazarTestMachine-"+nows  ])
   echo "tag done"

   echo nowstring()
// wait for instace to start 
   echo "Waiting for start ...."
   aws4 = aws(senv, [" ec2 wait instance-status-ok  --instance-ids  " ,  ii ])
   echo "....start Done."
   echo nowstring()


   echo nowstring()
// get windows password
   echo "Getting Password"
   aws5 = aws(senv,[  " ec2 get-password-data --priv-launch-key D:\\elazar\\elazartest1.pem --instance-id " ,  ii] )
   echo aws5
   adminpass = getpass(aws5)
   echo adminpass
   echo "Got it"
   echo nowstring()


   echo nowstring()
   adduser(ip, "Administrator", adminpass, "installer", "mrsetup1!")
   echo nowstring()
   mkadmin(ip, "Administrator", adminpass, "installer")
   echo nowstring()
   mkrdu(ip, "Administrator", adminpass, "installer")
   echo nowstring()
   adduser(ip, "Administrator", adminpass, "oracle", "mrdb1!")
   echo nowstring()
   adduser(ip, "Administrator", adminpass, "share", "mountit1!")
   echo nowstring()
   wmicexec(ip,"Administrator",adminpass, "cmd /c mkdir d:\\share")
   echo nowstring()

   pscmd="powershell -Command \$netcmd='net share share=d:\\share /grant:share'+[char]44+'full'; cmd /c \$netcmd "
   echo nowstring()
   echo "pscmd"
   echo pscmd
   echo nowstring()
   wmicexec(ip,"Administrator",adminpass, pscmd)
   echo nowstring()
   sleep 30
   echo nowstring()
   copyfiles(ip)
   echo nowstring()


//   instoracmd="cmd /c d:\\share\\instora.cmd"
//  wmicexec(ip,"Administrator",adminpass, instoracmd)
//C:\Users\erose>Downloads\PSTools\psexec  \\10.252.142.93  -u installer -p mrsetup1! -h cmd /c d:\share\instora
   echo nowstring()
    instora2cmd = "D:\\Installs\\PSTools\\psexec -accepteula  \\\\" + ip + "  -u installer -p mrsetup1!  -h cmd /c d:\\share\\instora"
    echo instora2cmd
    bat instora2cmd
   echo nowstring()
  
// compytnsnames
   echo nowstring()
    tnsnamecp  = "D:\\Installs\\PSTools\\psexec -accepteula  \\\\" + ip + "  -u installer -p mrsetup1!  -h cmd /c d:\\share\\copytnsnames.cmd"
    echo tnsnamecp 
    bat tnsnamecp 
   echo nowstring()

// install and start iis 
   echo nowstring()
    startiiscmd = "D:\\Installs\\PSTools\\psexec -accepteula  \\\\" + ip + "  -u installer -p mrsetup1!  -h cmd /c d:\\share\\startiis"
    echo startiiscmd
    bat startiiscmd
   echo nowstring()


// install webdeploy
//webpicmd /install /products:wdeploy
   echo nowstring()
    wdcmd = "D:\\Installs\\PSTools\\psexec -accepteula  \\\\" + ip + "  -u installer -p mrsetup1!  -h cmd /c D:\\share\\instwebdep"
    echo wdcmd
    bat  wdcmd
   echo nowstring()



// d:\Jenkins\workspace\carrier-rn-admin-builder-git-qa-deploy\Rn.Admin\Rn.Admin.WebApi\publish\QA\Rn.Admin.WebApi.deploy.cmd /t /m:10.252.142.221 /U:installer /P:mrsetup1! 

   echo nowstring()
   depcmd = "d:\\Jenkins\\workspace\\carrier-rn-admin-builder-git-qa-deploy\\Rn.Admin\\Rn.Admin.WebApi\\publish\\QA\\Rn.Admin.WebApi.deploy.cmd /y /m:" + ip + " /U:installer /P:mrsetup1!"
  echo depcmd
  bat depcmd
   echo nowstring()

   echo nowstring()
   depcmd2 = "d:\\Jenkins\\workspace\\carrier-rn-admin-builder-git-qa-deploy\\Rn.Admin\\Rn.Admin.WebConsole\\publish\\QA\\Rn.Admin.WebConsole.deploy.cmd /y /m:" + ip + " /U:installer /P:mrsetup1!"
  echo depcmd2
  bat depcmd2
   echo nowstring()





   echo "stopping.. "
   aws7 = aws(senv,[ "ec2  stop-instances --instance-ids"  ,  ii] )





   echo nowstring()
   echo "wait for stopped"
   aws4 = aws(senv, [" ec2 wait   instance-stopped --instance-ids  " ,  ii ])
   echo nowstring()


   echo nowstring()
   echo "imaging "
   aws6 = aws(senv,[ "ec2 ", "create-image --instance-id ", ii, "--name", "elazartest1name-"+nows, "--description",  quote("elazar test server description-"+nows) ])
   amiid = getamidata(aws6)
   echo "amiid: " + amiid

   echo nowstring()
   echo "wait for ami to be ready"
   // aws ec2 wait image-available --image-ids ami-947063fe

   awsami1 = aws(senv, [" ec2 wait image-available --image-ids " ,  amiid ])

   echo nowstring()

   echo "restarting "
   aws7 = aws(senv,[ "ec2  start-instances --instance-ids"  ,  ii] )


//   echo "terminating "
//   aws7 = aws(senv,[ "ec2  terminate-instances --instance-ids"  ,  ii] )

   echo nowstring()

   echo "End "

}
