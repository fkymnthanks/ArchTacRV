## 数据库配置

在~/tactic_detect_backend/tactic_detect_backend/configs.py文件中配置，使用mysql数据库

## 数据库迁移

无需使用sql语句建表，在项目根目录下，命令行执行

python manage.py db init

python manage.py db migrate

python manage.py db upgrade

## 运行项目

在项目根目录下，命令行执行

python manage.py runserver