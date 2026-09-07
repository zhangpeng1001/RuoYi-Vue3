账号：admin、admin111

cd E:\project\vue\RuoYi-Vue3\RuoYi-Vue3
pnpm run dev


RuoYi-Vue/
├── ruoyi-admin/          # 🔑 启动模块（主程序入口）
│   ├── src/main/java/com/ruoyi/
│   │   └── RuoYiApplication.java    # 启动类
│   └── src/main/resources/
│       ├── application.yml           # 主配置文件
│       └── application-druid.yml     # 数据源配置
│
├── ruoyi-framework/      # 框架核心（Security、AOP、拦截器等）
├── ruoyi-system/         # 系统模块（用户、角色、菜单等业务逻辑）
├── ruoyi-common/         # 通用工具模块（常量、异常、工具类）
├── ruoyi-generator/      # 代码生成器模块
├── ruoyi-quartz/         # 定时任务模块
├── sql/                  # 📌 数据库初始化脚本（非常重要）
│   ├── ry_20xxxxx.sql    # 主业务表（用户、角色、菜单等）
│   └── quartz.sql        # 定时任务表
├── bin/                  # 打包/启动脚本
└── doc/                  # 使用手册文档

