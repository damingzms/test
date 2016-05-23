rem 首先需要新建C:\webroot目录，并添加web文件，比如html等
rem 两种运行方式：
rem 1、将此文件拷贝到src目录下，并于Eclipse外面执行此文件
rem 2、直接在Eclipse内运行Start类，并传入端口及webroot路径

java cn.sam.test.jdk.nio.sse.server.Start 8080 "C:\webroot"

pause