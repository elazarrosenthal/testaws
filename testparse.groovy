import groovy.json.JsonSlurper

def getdata(r)
{
	js = new JsonSlurper()
	p  = js.parseText(r)
	ret = new String(p.Instances[0].InstanceId)
	return ret
}

d = new File('run1.out').text
ii = getdata(d)
println "1> " + ii
sleep 5000
println "1> " + ii

i2 = getdata(d)
println "1> " + i2
