## Bordercontrol
Internal service that handles the authentication process.  
It is focues to be only used internally so by default there is an IPv6 whitelisted.

### Features
- [ ] Permission System
- [ ] User Object
- [ ] Metrics
- [ ] REST API

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
If there is an `Permissions` header, it will check all permissions that are given. Those are splitted by using a semicolon (`;`).  
Returns:  
**200** - If exists (and Permissions exists)  
**401** - If exists, but the Permissions don't match  
**404** - If not exists


