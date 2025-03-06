# diag-tools

this repository contains the simple diagnostic tool to check connectivity through an appenv
optionnaly it can also check some DNS records and URLs related to an application if provided to the tool

It is recommended that the checkout of the repository and generate the config file each time you take your on-call duty
Those tools require that you have _knife_ correctly installed and configured and _jq_

In a future release the json file generated by _create-diag-config.sh_ will be available through the _infra-cli_ client to be usable by a wider range of people

## create-diag-config.sh

### Usage
simplest form is *__PATH_TO_SCRIPT__*_/create-diag-config.sh_ *__APPENV__*

where _PATH_TO_SCRIPT_ is the path to the script and _APPENV_ is the appenv you wish to generate the config file for

Please consult options below to adapt to your specific _knife_ client configuration and/or add specific app to test.

Be aware that generating the config file can take up to two minutes (it's about one minute for a typical appenv)

### Options
```
Usage: ./create-diag-config.sh -h | --help
       ./create-diag-config.sh [OPTION] APP_ENV [APP [APP]...]

Only one APP_ENV may be specified as the first argument, multiple apps can be provided

  -v, --verbose                         be more verbose,
  -q, --quiet                           be less verbose
  -d, --destination=                    set configuration destination (defaults to _PROVIDED_APP_ENV_.json)
  --knife_command="your knife command"  use this command instead of the default "knife"
  --knife_profile=your_knife_profile    use this knife profile instead of the default "--profile production"
  --no-knife_profile                    don't use any knife profile
  -h, --help                            show this help
```
- `-v` will provide you some more detail about the ongoing run. the "Constructing nodes array" step being especially long, adding -v will output each treated node, making it less frustrating to watch. It will also output the generated json to stdout in addition to the generated file
- `-q` is the opposite and will suppres about all output
- `-d` enables you to provide a custom destination file instead of the default *_APPENV__*_.json_
- `--knife_command="your knife command"` enables you to specify a custom _knife_ program to use instead of the default _knife_
- `--knife_profile=your_knife_profile"` enables you to specify a custom _knife_ profile to use instead of the default _production_ (ex. : `--knife_profile=prod`)
- `--no_knife_profile` enables you to disable using a knife profile, letting the default configuration for your _knife_ program take precedence

You may provide one or more application included in that appenv on the command-line to be able to test URLs and DNS-records for that(those) app(s)

### Output
two sample generated file are provided as a example, 
- _climbers-sb.json_ is the result of `diag-tools/create-diag-config.sh climbers-sb`
- _climbers-sb_test-certificate.json_ is the result of `diag-tools/create-diag-config.sh -d climber-sb_test-certificate.json climbers-sb test-certificate`

A typical output of the tool, when not using verbose nor quiet mode, should look like the following:
```
Diagnostic config file generator 

Generating for app_env: climbers-sb
Output configuration file: climbers-sb.json
Constructing nodes array
Constructing hypervisors array
Constructing extra-tests array
Constructing dns-servers array
Constructing dns-records array
Constructing extra-tests array
Constructing jump-servers array
Create final output
```

### Limitations & notes
This tool works better with appenvs and apps running using on _the platform_. 
Especially, the apps you may provide on the command-line are a bit picky in their naming. 
Also, specifying apps works only for "client" apps, not "global" apps such as _iam_. 
This limitation should be removed in a future release. 

You may want to add your own special extra tests in the extra-tests array of the json file. This array will typically look like:
```
  "extra-tests": [
    "curl -vk https://chef.priv.cloud.kbrwadventure.com/",
    "curl http://yp.priv.cloud.kbrwadventure.com/climbers-sb.json",
    "nc -u -w 1 asterisk.priv.cloud.kbrwadventure.com 5060",
    "nc -w 1 git.priv.cloud.kbrwadventure.com 22"
  ],
```
As you can see those are just plain commands to execute, which should have an exit code of 0. so you can just add lines like
```
"extra-tests": [
  "curl http://mycustomdoma.in",
  "ping -c 1 client-remote.targ.et",
...
  ],
```
and those will be executed in the "extra-test" step of the _monitoring.sh_ program presented below

## monitoring.sh

### Features

#### Host Checks
- Verifies that servers are reachable via hostname, IP addresses (ping)

#### Container Checks
- Tests IPv4 and IPv6 connectivity to containers and verifies that specified ports are open (ping + nc)

#### Access Container Checks
- Tests public IP addresses for access containers, including VRRPs if configured (Ping)

#### Gateway v2 Checks
- Verifies outbound connectivity through gateway servers and identifies which gateway handles outbound requests (Curl)

#### VPN Router Checks
- Connects to VPN routers and verifies IPsec tunnel status (SSH + ipsec status)

#### DNS Checks
- Tests DNS resolution for specified records against all configured DNS servers (Dig)

#### Riak Cluster Checks
- Verifies that all Riak nodes are properly connected in the cluster (Curl /stats)

#### Custom Tests
- Executes any additional commands specified in the "extra-tests" section

### Requirements
* Bash shell 
* Required utilities:
   * `jq` - For JSON parsing
   * `timeout` - For command timeouts
   * `ssh` - For remote connections
   * `curl` - For HTTP requests
   * `dig` - For DNS queries
   * `nc` - For TCP port checks
   * `ping` - For basic connectivity testing

### Usage
Basic usage:

* Activate your VPN

On Linux:
```bash
./monitoring.sh appenv.json
```

On Mac:
```bash
bash monitoring.sh appenv.json
```

