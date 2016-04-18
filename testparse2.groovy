import groovy.json.JsonSlurper

def getamidata(r)
{
	js = new JsonSlurper()
	p  = js.parseText(r)
	println p
	println p.getClass()
	amiid  = new String(p.ImageId)
	return amiid
}

d = new File('run2.out').text
println d
ii = getamidata(d)
println "1> " + ii

