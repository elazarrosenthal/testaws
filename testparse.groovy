import groovy.json.JsonSlurper

def getdata(r)
{
	js = new JsonSlurper()
	p  = js.parseText(r)
	iid  = new String(p.Instances[0].InstanceId)
	pip  = new String(p.Instances[0].PrivateIpAddress)
	println iid
	println pip
	return ret = ["id": iid, "ip":  pip]
}

d = new File('run1.out').text
ii = getdata(d)
println "1> " + ii["id"]
println "2> " + ii["ip"]

