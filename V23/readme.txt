实现二维码功能

实现步骤:
1:在resources/static/myweb下新建一个页面:createQR.html
  页面上定义一个form表单。action="/myweb/createQR"
  并添加一个输入框，用于让用户输入二维码上要显示的内容。

2:在com.webserver.controller包下新建一个类:ToolsController
  该类用于处理如生成二维码，验证码等工具的业务类。
  在其中定义一个生成二维码的业务方法:createQR()

3:在DispatcherServlet的service方法上再添加一个分支，判断path的值是否为表单action的值"/myweb/createQR"
  如果是，则实例化ToolsController并调用其createQR()方法处理生成二维码的业务

4:ToolsController的createQR()方法中的实现步骤
  4.1:通过request.getParameter()获取表单中用户输入的二维码上要显示的内容。
  4.2:设置响应头Content-Type的值为:image/jpeg
  4.3 使用QRCodeUtil.encode()方法将二维码生成出来并写入到响应对象的正文上(response.getOutputStream())

如此一来，就完成了二维码的完整流程。最后可在index.html页面上添加一个超链接，访问到生成二维码的页面。