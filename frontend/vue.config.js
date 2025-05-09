module.exports = {
  devServer: {
    port: 9091,
    open: true,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        pathRewrite: {
          '^/api': ''
        }
      }
    }
  },
  // 输出设置
  outputDir: 'dist',
  // 禁用生产环境的source map
  productionSourceMap: false,
  // 设置路径别名
  configureWebpack: {
    resolve: {
      alias: {
        '@': require('path').resolve(__dirname, 'src')
      }
    }
  }
} 