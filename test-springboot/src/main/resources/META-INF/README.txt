1、环境
	安装JRE，下载路径：
	（Windows 64位）http://rj.baidu.com/soft/detail/17740.html
	（MAC）http://rj.baidu.com/soft/detail/25883.html?ald

2、运行
	解压IAmNotVirus.zip后，双击IAmNotVirus.jar

3、访问
	返回简单值：
	http://localhost:8080/?operand1=1.2&operand2=7
	返回JSON：
	http://localhost:8080/json?operand1=1.2&operand2=7

4、使用
	如果参数operand1和operand2均为数值，则返回和；
	如果至少有一个是非数值，则两个参数做字符串拼接，返回拼接后的字符串值；
	如果至少有一个是空，则返回信息“Parameters [operand1] and [operand2] can not be blank.”；
	
5、关闭
	直接关闭窗口。