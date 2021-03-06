A new generation RA for EJBCA

# Languages

* Javascript (NodeJS)
* Java

NodeJS is needed to build client side Javascript libraries.

# Requirements

* Java (OpenJDK) at least version 8
* Tomcat 8
* ejbca server
* client certificate from EJBCA
* Enough rights to to its job on EJBCA

## Prepare for Build

You must execute following step 

**npm install**

otherwise you will get a broken build and bad things will happen.

For building you need NodeJS/NPM. Consult https://nodejs.org/ for
installation, if your favourite distribution does not offer a
installation package.

# Status

**This software is in very early alpha status. Do not use for production!**

If you going to use in Internet do not forget to uncomment
<secure>true</secure>
to enforce https for session cookie.

# Theory of Operation

The Problem: There are many browsers with different ways to locally generate
a key pair. In case of Firefox *genkey* is depricated for many years and
should not longer used.

The solution: Let's Javascript do that work! W3C defined WebcryptoAPI for
that reason. Of course we wont't like a solution which sends privates keys
from or to server.

**All private material is generated client side and never leave client**

Even password for PKCS#12 file is generated on client side beacause nobody
else needs that password.

If key pair was generated by JavaScript on client certificate signing request
is sent to server. Server does webservice call to EJBCA (ACME protocol will
be also supported soon) to get a certificate.

In case of success server answers with users certificate. JavaScript client
builds a PKCS#12 file then with locally stored private key and provided
certificate and offers it to user for download. Password for PKCS#12 is
shown to user.

If user wants to use certificate he has to import it to application by himself.
It is not imported automatically to browser, because application cannot know
users intention for certificate usage.

# License

This code is released under conditions of GNU AFFERO GENERAL PUBLIC LICENSE,
for details, see file web/license.txt

This repository uses following third party repositories:

* https://jquery.com
* https://jqueryui.com/
* https://github.com/PeculiarVentures/PKI.js
* https://github.com/digitalbazaar/forge

Please refer to that URLs to get their license conditions. These files are
not enclosed to this repository.

## Java Libraries

see pom.xml

# How to use

## Properties file

PropertyLoader (see source code) looks on different locations for
a file named ejbca-new-ra.properties.xml and tells via log file which on used.

Following properties file should explain what needs to be configured.

    <?xml version="1.0" encoding="UTF-8" standalone="no"?>
    <!DOCTYPE properties SYSTEM "http://java.sun.com/dtd/properties.dtd">
    <properties>
    <entry key="shiroini">/etc/ejbca-ra-new-config/ejbca-new-ra.shiro.ini</entry>
    <entry key="ldap.truststore.file">/etc/ejbca-ra-new-config/ca.jks</entry>
    <entry key="ldap.truststore.password">changeit</entry>

    <entry key="wsdlLocationUrl">file:/etc/ejbca-ra-new-config/ejbcaws.wsdl</entry>

    <!-- If you need a proxy server -->
    <!--
    <entry key="proxyHost">192.168.0.1</entry>
    <entry key="proxyPort">3128</entry>
    -->
    
    <entry key="ejbca.keystore.type">PKCS12</entry>
    <entry key="ejbca.keystore.file">/etc/ejbca-ra-new-config/demo.ca.administrator.p12</entry>
    <entry key="ejbca.keystore.password">A*Very*Bad*Password</entry>
    <entry key="ejbca.truststore.type">jks</entry>
    <entry key="ejbca.truststore.file">/etc/ejbca-ra-new-config/ejbca-new-ra.truststore.jks</entry>
    <entry key="ejbca.truststore.password">changeit</entry>
    <entry key="ejbca.tls">TLSv1.2</entry>

    <entry key="schemaFileName">/etc/ejbca-ra-new-config/ejbca-new-ra.schema.json</entry>

    <entry key="caName">DemoCA</entry>
    <entry key="CertificateProfileName">democa-profile</entry>
    <entry key="EndEntityProfileName">democa-profile</entry>
    <entry key="o">Honest Achmeds CA</entry>
    <entry key="ou">Used cars and aircrafts</entry>

    <entry key="title">Demo RA for EJBCA</entry>

    <!-- Set if you want authentication --> 
    <entry key="auth.required"></entry>
    
    <!--
    Space separated list of allowed hashes.
    Possible values: SHA-256 SHA-384 SHA-512 SHA-1
    Do not use SHA-1, it is broken and you get insecure certificates.
    Do not define to use default value of SHA-256
    -->
    <entry key="hashes">SHA-256 SHA-384 SHA-512</entry>
    <!--
    Space separated list of allowed signatures.
    Possible values: RSASSA-PKCS1-v1_5 RSA-PSS ECDSA
    Do not use ECDSA, PKCS#12 creation does not work yet.
    Do not define to use default value of RSASSA-PKCS1-v1_5
    -->
    <entry key="sign">RSASSA-PKCS1-v1_5 RSA-PSS ECDSA</entry>
    
    </properties>

wsdlLocationUrl contains EJBCA wsdl file, you may consider to provide it as file.

## Environment Variables

CATALINA_OPTIONS="-DshiroConfigLocations=file:<path to your shiro.ini>"

If properties file named other than ejbca-new-ra.properties.xml or not located in
/etc or /usr/local/etc you have to provide its location by environment:

EJBCA_NEW_RA_PROPERTIES=/home/user/myproperties.xml     

It tries to load properties from /home/user/myproperties.xml then.

## ejbca-new-ra.schema.json

Schema file contains attributes needed by EJBCA entity configuration.
key represents attribute and value is text shown to user in form.

    {
      "e": "E-Mail Address:",
      "cn": "Common Name:",
      "c": "Country:"
    }

May be improved...

## shiro.ini

    [main]
    sessionManager = org.apache.shiro.web.session.mgt.DefaultWebSessionManager
    securityManager.sessionManager=$sessionManager
    sessionValidationScheduler = org.apache.shiro.session.mgt.ExecutorServiceSessionValidationScheduler
    # Default is 3,600,000 millis = 1 hour:
    sessionValidationScheduler.interval = 3600000
    securityManager.sessionManager.globalSessionTimeout = 7200000
    securityManager.sessionManager.sessionValidationScheduler = $sessionValidationScheduler
    
    # Only used if you need Active Directory
    contextFactory = org.apache.shiro.realm.ldap.JndiLdapContextFactory
    contextFactory.url = ldaps://dc1.samba4.example.com:636
    contextFactory.systemUsername = shiro@samba4.example.com
    contextFactory.systemPassword = A***very***b8d***Password
    contextFactory.environment[java.naming.security.protocol] = TLSv1.2
    contextFactory.environment[java.naming.ldap.factory.socket] = net.felsing.client_cert.utilities.RaSSLSocketFactory
    
    activeDirectoryRealm = org.apache.shiro.realm.activedirectory.ActiveDirectoryRealm
    activeDirectoryRealm.ldapContextFactory = $contextFactory
    activeDirectoryRealm.searchBase = "CN=Users,DC=samba4,DC=example,DC=com"
    activeDirectoryRealm.groupRolesMap = "CN=shirouser,CN=Users,DC=samba4,DC=example,DC=com":"shirouser"
    securityManager.realm = $activeDirectoryRealm
    
    # Simple auth: comment out all contextFactory and activeDirectoryRealm stuff  
    #[users]
    #joetest = Geheim1234
    
    [roles]
    shirouser = shirouser
    
    [urls]
    /req/** = user
    /login/** = anon
    /** = anon

# Typical Mistakes

* truststore must contain CA certificate used for signing EJBCA server certificate.
* keystore must contain client certificate for EJBCA which is used to authenticate Webservice client.
* EJBCA User (keystore) needs administrative rights at EJBCA.
* No properties file
* shiro.ini file not found, see shiroini property in properties file
* Changes on language files ignored: Did you started npm install?
* Does not compile, or tons of Javascript errors in Browser: Did you started npm install?
