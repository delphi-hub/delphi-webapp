module.exports = {
    configureWebpack: {
        devServer: {
            headers: {
                'Access-Control-Allow-Origin': '*',
                'Access-Control-Allow-Methods': 'GET, POST, PUT, DELETE, PATCH, OPTIONS',
                'Vary': 'Origin'
            }
        }
    },
    devServer: {
            proxy: 'https://repo1.maven.org'
    }
};
