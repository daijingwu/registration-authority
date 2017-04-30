import rollupNodeResolve from "rollup-plugin-node-resolve";

export default [
	{
        entry: "es6.js",
		dest: "bundle.js",
		format: "iife",
        outro: `
window.createPKCS10 = createPKCS10;

function context(name, func) {}`,
        plugins: [
            rollupNodeResolve({ jsnext: true, main: true })
        ]
	}
];