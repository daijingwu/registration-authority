import rollupNodeResolve from "rollup-plugin-node-resolve";

export default [
	{
        entry: "es6.js",
		dest: "bundle.js",
		format: "iife",
        outro: `
window.passwordBasedIntegrity = passwordBasedIntegrity;
window.certificateBasedIntegrity = certificateBasedIntegrity;
window.noPrivacy = noPrivacy;
window.passwordPrivacy = passwordPrivacy;
window.certificatePrivacy = certificatePrivacy;
window.openSSLLike = openSSLLike;
window.parsePKCS12 = parsePKCS12;
window.handlePKCS12 = handlePKCS12;

function context(name, func) {}`,
        plugins: [
            rollupNodeResolve({ jsnext: true, main: true })
        ]
	}
];
