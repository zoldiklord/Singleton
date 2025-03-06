# Diag-tool

## Overview
Diag-tool is a bash-based tool designed to verify the health and connectivity of various network components. It performs a series of tests on hosts, containers, VPN connections, DNS records, and more using data from a structured JSON configuration file.

## Features

### Host Checks
- Verifies that servers are reachable via hostname, IP addresses (ping)

### Container Checks
- Tests IPv4 and IPv6 connectivity to containers and verifies that specified ports are open (ping + nc)

### Access Container Checks
- Tests public IP addresses for access containers, including VRRPs if configured (Ping)

### Gateway Checks
- Verifies outbound connectivity through gateway servers and identifies which gateway handles outbound requests (Curl)

### VPN Router Checks
- Connects to VPN routers and verifies IPsec tunnel status (SSH + ipsec status)

### DNS Checks
- Tests DNS resolution for specified records against all configured DNS servers (Dig)

### Riak Cluster Checks
- Verifies that all Riak nodes are properly connected in the cluster (Curl /stats)

### Custom Tests
- Executes any additional commands specified in the "extra-tests" section

## Requirements
* Bash shell 
* Required utilities:
   * `jq` - For JSON parsing
   * `timeout` - For command timeouts
   * `ssh` - For remote connections
   * `curl` - For HTTP requests
   * `dig` - For DNS queries
   * `nc` - For TCP port checks
   * `ping` - For basic connectivity testing

## Usage
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

