
    bdev=' --block-device-mappings \'[ { \"DeviceName\": \"/dev/sda1\", \"Ebs\": { \"DeleteOnTermination\": true, \"VolumeSize\": 40 } }, { \"DeviceName\": \"xvdb\", \"Ebs\": { \"DeleteOnTermination\": true, \"VolumeSize\": 30 } } ] \' '

   println bdev
