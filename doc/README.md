# LDAP Authentication

In most cases a self signed certificate is used for
Enterprise LDAP servers. Java do not like that by default,
so this server offers a convenience class to handle this
situation in a quite secure way.

To use that class
net.felsing.client_cert.utilities.RaSSLSocketFactory
in shiro.ini, following steps are required:

* echo | openssl s_client -connect <Your LDAP server>:636 -showcerts
* save CA certificate to file, e.g. myldap.ca.crt
* keytool -import -file myldap.ca.crt -alias <Your LDAP server> -keystore myldap.ca.jks

# shiro.ini

Set
contextFactory = org.apache.shiro.realm.ldap.JndiLdapContextFactory
to use that class.

# ejbca-new-ra.properties.xml

Set following properties:
    <entry key="shiroini">/etc/ejbca-ra-new-config/shiro.ini</entry>
    <entry key="ldap.truststore.file">/etc/ejbca-ra-new-config/myldap.ca.jks</entry>
    <entry key="ldap.truststore.password">changeit</entry>
Password must be same as used for keystore creation above.

# Security

This solution accepts all certificates signed by your Certification Authority used by your LDAP server.

You may find many recommendations to use a class which accepts any certificate, but that is a
 very bad idea.
 
By default we are using TLSv1.2, which represents Java 8 state of the art. This days TLSv1.3 may
approach, so it is held configurable. When Java, your EJBCA an LDAP servers are supporting
TLSv1.3 feel free to use it.

# Troubleshooting

If your LDAP server is not willing to authenticate, set log level to debug an check
carefully:

* net.felsing.client_cert.utilities.RaSSLSocketFactory found?
* Did you imported **really** the right CA certificate into LDAP truststore?
* Did you used unencrypted LDAP instead of encrypted LDAP?
 