import groovy.json.JsonSlurper

d = new File('run1.out').text
// println d
js = new JsonSlurper()
p  = js.parseText(d)
ii = p.Instances[0].InstanceId
println "1> " + ii
