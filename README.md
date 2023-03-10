<a href="https://drone.n1.mommde.xyz/whitehole-dev/bordercontrol">
  <img src="https://drone.n1.mommde.xyz/api/badges/whitehole-dev/bordercontrol/status.svg?ref=refs/heads/main" />
</a>

## Bordercontrol
Internal service that handles the authentication process.  
It is focused to be only used internally so by default there is an IPv6 whitelisted.

### Features
- [ ] Permission System
- [ ] User Object
- [ ] Metrics
- [ ] REST API

### Test
The Test EcoSystem is running a Memory Based MongoDB on your local machine.  
Using the [mongo-java-server](https://github.com/bwaldvogel/mongo-java-server) it can test real db interactions using the MongoDB protocol

### Permission System
Each token has a list of permissions represented as a string.  
They are formatted in lower case and trailed by a dot.  
It should start with organization and go in depth.

**Keywords:**  
**`*`** Is beeing used as an wildcart.


### REST API
The service runs on port 80 on basic HTTP.

#### Routes
**`/verify`**  
**GET**  
Checks if the Bearer Token in the `Authorization` header exist.  
This header must be formatted the following: PublicId.HourlyGeneratedTokenAsUS_ASCIIEncodeed  
If there is an `Permissions` header, it will check all permissions that are given. Those will be split by using a semicolon (`;`).  
Returns:  
**200** - If exists (and Permissions exists)  
**401** - If exists, but the Permissions don't match  
**404** - If not exists


**`/about`**  
**GET**  
Return data of token provided in `Authorization` header.
