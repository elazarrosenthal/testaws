

def makesetenv(raw)
{
    x0 = raw.replaceAll("\n", "")
    x1 = x0.replaceAll(";", "\n")
    x2 = x1.replaceAll("export", "set")
    return x2.toString()
}

s="""export AWS_DEFAULT_REGION=us-east-1;
export AWS_SESSION_TOKEN=FQoDYXdzEEkaDE7xCEhZ8DP9RGYIpiKeAvpf6qef20ICIT/4VB9CfUKTKAhfjbmmTPSNn8Si3gEAhpVKXcNI/X8/Eyxix/mMgt1OGIGY+kN0Qbc28feLPvY7cGpVwbbO3zTaVTaMB/drtDligXqvJg5ZSgHdGcQJWXOvdJoO9lFmgJ4m5oI1Qlq23pa6KEaSPIrzcUtFRPGQ6jg4nuHo/P/XwaAggEdpxd6y3abmAZQs93weVW5x5jOnLVxliM0sfkgNvnmceHV4cs33mGaMGMHUDJkcKvS3VZRBZiisxGHGxoKxjvTJBiNSRGa83EnTNWvgozabAq5RWG2n720ZI6HtBXuLlJ/05UNRCZlSXI2Nan/vzEHSmNMPa388RS6l6vcxURdWDsZyRrmrgbPBDWtZyZpb2HkonpOjuQU=;
export AWS_ACCESS_KEY_ID=ASIAJ2BIJBSZSOVV2CXA;
export AWS_SECRET_ACCESS_KEY=hmpNoPVb1lU77pakzX9WBYUDuxa9q2QfT620arRu;"""


println s
s2 = makesetenv(s)

println(s2)
println s2.getClass().toString()
