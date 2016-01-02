A new generation RA for EJBCA

# Requirements

* Java (OpenJDK) at least version 8
* Tomcat 8
* ejbca server
* client certificate from EJBCA
* Enough rights to to its job on EJBCA

To set up a deployment environment you have to do following commands:

* web/js
* git clone https://github.com/GlobalSign/ASN1.js.git
* git clone https://github.com/GlobalSign/PKI.js.git

# Production

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
for details, see file license.txt

This repository uses following third party repositories:

* https://jquery.com
* https://jqueryui.com/
* https://github.com/digitalbazaar/forge

not included in that repository, see *requirements*:

* https://github.com/GlobalSign/PKI.js
* https://github.com/GlobalSign/ASN1.js

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
        <entry key="wsdlLocationUrl">file:/home/ejbca/ejbcaws.wsdl</entry>

        <entry key="trustStoreType">jks</entry>
        <entry key="trustStoreFile">/etc/secrets/truststore.jks</entry>
        <entry key="trustStorePassword">changeit</entry>

        <entry key="proxyHost">192.168.0.1</entry>
        <entry key="proxyPort">3128</entry>

        <entry key="keyStoreType">PKCS12</entry>
        <entry key="keyStoreFile">/etc/secrets/ejbca-myca-admin.p12</entry>
        <entry key="keyStorePassword">changeit</entry>

        <entry key="schemaFileName">/media/cf/Development/ejbca-new-ra.schema.json</entry>

        <entry key="caName">DemoCA</entry>
        <entry key="CertificateProfileName">DemoCertProfile</entry>
        <entry key="EndEntityProfileName">DemoEntityProfile</entry>
        <entry key="ou">Demo Certificates</entry>
        <entry key="o">example.com</entry>

        <entry key="title">Demo RA for EJBCA</entry>

    </properties>

wsdlLocationUrl contains EJBCA wsdl file, you may consider to provide it as file.

## ejbca-new-ra.schema.json

Schema file contains attributes needed by EJBCA entity configuration.
key represents attribute and value is text shown to user in form.

    {
      "e": "E-Mail Address:",
      "cn": "Common Name:",
      "c": "Country:"
    }

May be improved...

# Typical Mistakes

* truststore contains CA certificate used for signing EJBCA server certificate.
* keystore must contain client certificate for EJBCA which is used to authenticate Webservice client.
* EJBCA User (keystore) needs administrative rights at EJBCA.
