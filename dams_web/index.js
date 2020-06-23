//引用模块
const express = require('express');
const { createProxyMiddleware } = require('http-proxy-middleware');
const app  = express();
//静态资源直接访问
//如dist/js/jquery.js  访问路径为 http://localhost/js/jquery.js 在script标签中src="js/jquery.js"
app.use(express.static('dist'));

//api子目录下的都是用8080代理
// 在我的源代码后端是8080端口开启 其中有两个controller  UserController  TestController
//UserController requestMapping是"/api"  访问http://localhost/api/users 相当于访问 http://localhost:8080/api/users
//TestController requestMapping是"/test" 访问http://localhost/test/users不能访问

//管理员url代理
const apiProxy = createProxyMiddleware("/api",{ target: 'http://localhost:8080',changeOrigin: true });//将服务器代理到localhost:8080端口上[本地服务器为localhost:3000]
app.use('/api/*', apiProxy);

//登录url代理
const loginProxy = createProxyMiddleware("/login",{ target: 'http://localhost:8080',changeOrigin: true })
app.use('/login/*',loginProxy);

//学生url代理
const stuProxy = createProxyMiddleware("/stu",{ target: 'http://localhost:8080',changeOrigin: true })
app.use('/stu/*',stuProxy);

//文件url代理
const fileProxy = createProxyMiddleware("/file",{ target: 'http://localhost:8080',changeOrigin: true })
app.use('/file/*',fileProxy);

//问卷url代理
const questProxy = createProxyMiddleware("/quest",{ target: 'http://localhost:8080',changeOrigin: true })
app.use('/quest/*',questProxy);

//宿舍url代理
const dormProxy = createProxyMiddleware("/dorm",{ target: 'http://localhost:8080',changeOrigin: true })
app.use('/dorm/*',dormProxy);

//聚类分配
const autoProxy = createProxyMiddleware("/auto",{ target: 'http://localhost:8080',changeOrigin: true })
app.use('/auto/*',autoProxy);


app.get('/index.html', function (req, res) {
    res.sendFile( __dirname + "/src/" + "index.html" );
})

//登录界面代理
app.get('/login.html', function (req, res) {
    res.sendFile( __dirname + "/src/login/" + "login.html" );
})

app.get('/error', function (req, res) {
    res.sendFile( __dirname + "/src/" + "error.html" );
})

app.get('/noRole', function (req, res) {
    res.sendFile( __dirname + "/src/login/" + "noRole.html" );
})

app.get('/register', function (req, res) {
    res.sendFile( __dirname + "/src/login/" + "register.html" );
})

app.get('/loginOut', function (req, res) {
    res.sendFile( __dirname + "/src/login/" + "login.html" );
})

//主界面代理
app.get('/main', function (req, res) {
    res.sendFile( __dirname + "/src/main/" + "main.html" );
})

app.get('/hello', function (req, res) {
    res.sendFile( __dirname + "/src/main/" + "hello.html" );
})

app.get('/firstPage', function (req, res) {
    res.sendFile( __dirname + "/src/main/" + "firstPage.html" );
})

//用户信息管理代理
app.get('/userInfo', function (req, res) {
    res.sendFile( __dirname + "/src/userInfo/" + "userInfo.html" );
})

app.get('/userAuthorization', function (req, res) {
    res.sendFile( __dirname + "/src/userInfo/" + "userAuthorization.html" );
})

app.get('/authorization', function (req, res) {
    res.sendFile( __dirname + "/src/userInfo/" + "authorization.html" );
})

//学生信息代理
app.get('/stuInfo', function (req, res) {
    res.sendFile( __dirname + "/src/stuInfo/" + "stuInfo.html" );
})

app.get('/stuSelect', function (req, res) {
    res.sendFile( __dirname + "/src/stuInfo/" + "studentSelect.html" );
})

app.get('/stuImport', function (req, res) {
    res.sendFile( __dirname + "/src/stuInfo/" + "stuImport.html" );
})

//学生填写问卷代理
app.get('/stuLogin', function (req, res) {
    res.sendFile( __dirname + "/src/login/" + "stuLogin.html" );
})

//问卷代理
app.get('/question', function (req, res) {
    res.sendFile( __dirname + "/src/question/" + "question.html" );
})

app.get('/releaseQuest', function (req, res) {
    res.sendFile( __dirname + "/src/question/" + "releaseQuestion.html" );
})

app.get('/stuQuestion', function (req, res) {
    res.sendFile( __dirname + "/src/question/" + "stuQuestion.html" );
})

//问卷分配代理
app.get('/dorAllocation', function (req, res) {
    res.sendFile( __dirname + "/src/dorAllocation/" + "dorAllocation.html" );
})

//宿舍代理
app.get('/dormInfo', function (req, res) {
    res.sendFile( __dirname + "/src/dormitory/" + "dormitoryInfo.html");
})

app.get('/dormImport', function (req, res) {
    res.sendFile( __dirname + "/src/dormitory/" + "dormitoryImport.html");
})

//js文件代理
app.get('/main.js', function (req, res) {
    res.sendFile( __dirname + "/src/main/" + "main.js");
})

app.listen(80,function(){
    console.log('connect successfully')
})