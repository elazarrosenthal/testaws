import groovy.json.JsonSlurper

def puts3()
{
       
def jsonSlurper = new JsonSlurper()
def object = jsonSlurper.parseText('{ "name": "John Doe" }')
echo object.name
}




node{
    stage "1"
   x =input message: 'enter vars:', parameters: [[$class: 'TextParameterDefinition', defaultValue: '', description: '', name: 'AWSENV']]
   echo x
   echo x.inspect()
    x0 = x.replaceAll("\n", "")
    x1 = x0.replaceAll(";", "\n")
    x2 = x1.replaceAll("export", "set")
    echo "\n\n"
    echo x2
    bat x2
    aws='"c:\\Program Files\\Amazon\\AWSCLI\\aws"'
    echo aws
    cmd = x2 +" " +  aws + " ec2 describe-instances"
    echo cmd
    bat cmd
    cmd2 = x2 + aws + " ec2 run-instances   --image-id ami-3d787d57 --count 1 --instance-type t2.micro --key-name  elazartest1 --security-group-ids sg-27f9af42 --subnet-id subnet-96d526e1  > run2.out"
   echo cmd2
   bat cmd2
   d= readFile 'run2.out'
   echo d
   def jsonSlurper = new JsonSlurper()
   def p = jsonSlurper.parseText(d)
   ii = p.Instances[0].InstanceId
   echo ii
   cmd3 = x2 + aws + " ec2 create-tags --resources  " + ii + "  --tags Key=Name,Value=ElazarTestMAchine " 
   echo cmd3
   bat  cmd3

  

}
