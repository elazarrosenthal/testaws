
def escapeand(path1) 
{ 
	esc =  path1.replace("&", "^&")
	return esc
}

pscmd="1&2"

println pscmd
println escapeand(pscmd)

