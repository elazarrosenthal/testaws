import groovy.json.JsonSlurper

def puts3()
{
       
def jsonSlurper = new JsonSlurper()
def object = jsonSlurper.parseText('{ "name": "John Doe" }')
echo object.name
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
	iid  = new String(p.Instances[0].InstanceId)
	pip  = new String(p.Instances[0].PrivateIpAddress)
	println iid
	println pip
	return ret = ["id": iid, "ip":  pip]
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
	awscmd = msdeploypath +" " +  ltos(args)
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





node{
    stage "1"
   x =input message: 'enter vars:', parameters: [[$class: 'TextParameterDefinition', defaultValue: '', description: '', name: 'AWSENV']]
    x0 = x.replaceAll("\n", "")
    x1 = x0.replaceAll(";", "\n")
    x2 = x1.replaceAll("export", "set")
    echo "\n\n"
    echo x2
    bat x2
    echo "getting env"
    senv = makesetenv(x)
    echo "senv =  " +senv


/*
    aws='"c:\\Program Files\\Amazon\\AWSCLI\\aws"'
    echo aws
    cmd = x2 +" " +  aws + " ec2 describe-instances"
    echo cmd
    bat cmd
*/
   aws1 =  aws(env, ["ec2", "describe-instances"])
   echo "aws 1 = " + aws1

// create new instace and get id
    cmd2 = x2 + aws + " ec2 run-instances   --image-id ami-3d787d57 --count 1 --instance-type t2.micro --key-name  elazartest1 --security-group-ids sg-27f9af42 --subnet-id subnet-96d526e1  > run2.out"
   echo cmd2
   bat cmd2
   d= readFile 'run2.out'
   echo d
   ii = extractiid(d)
   echo ii

// tag instace
   echo "tagging"
   cmd3 = x2 + aws + " ec2 create-tags --resources  " + ii + "  --tags Key=Name,Value=ElazarTestMachine " 
   echo cmd3
   bat  cmd3
   
   echo "tag done"
// wait for instace to start 
   echo "Waiting for start ...."
   cmd4 =  x2 + aws + " ec2 wait instance-status-ok  --instance-ids  " + ii 
   echo cmd4
   bat cmd4
   echo "....start Done."

// get windows password
   echo "Getting Password"
   cmd5 =  x2 + aws + " ec2 get-password-data --priv-launch-key D:\\elazar\\elazartest1.pem --instance-id " + ii
   echo cmd5
   echo "Got it"
   bat cmd5

   echo "End "

}