Client side Javascript code for handle PKCS#10 and PKCS#12 stuff

# Prepare

npm install

loads everything needed and installs compiled files to
appropriate locations so building project creates runnable code.

# Why not PeculiarVentures PKI.js?

PKI.js creates PKCS#12 files accounding to PKCS#12, but browsers OpenSSL and
other subjects are not able to open that flavour of PKCS#12. We are
investigating that, but be have no influence of what Mozilla, PeculiarVentures
etc. are doing. So far we use digitalbazaars forge, even it still does not
support ECDSA, but their PKCS#12 are accepted by most libraries.

# License

This code is released under conditions of GNU AFFERO GENERAL PUBLIC LICENSE,
for details, see file web/license.txt

This repository uses following third party repositories:

* https://github.com/PeculiarVentures/PKI.js
* https://github.com/digitalbazaar/forge

See URLs above for licenses of those libraries

