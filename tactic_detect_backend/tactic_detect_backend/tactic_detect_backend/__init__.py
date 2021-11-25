from flask import Flask
from tactic_detect_backend.exts import db
from tactic_detect_backend.views.CkpRbkView import ckprbk
from tactic_detect_backend.views.HeartBeatView import heartbeat
from tactic_detect_backend.views.Ping_EchoView import pingecho
from tactic_detect_backend.views.RedundancyView import redundancy
from tactic_detect_backend.views.VotingView import voting
import tactic_detect_backend.configs
import pymysql



def create_app():
    app = Flask(__name__)
    # 配置数据库
    pymysql.install_as_MySQLdb()
    app.config.from_object(tactic_detect_backend.configs)
    # 注册视图模块
    app.register_blueprint(ckprbk, url_prefix='/ckprbk')
    app.register_blueprint(heartbeat, url_prefix='/heartbeat')
    app.register_blueprint(pingecho, url_prefix='/pingecho')
    app.register_blueprint(redundancy, url_prefix='/redundancy')
    app.register_blueprint(voting, url_prefix='/voting')
    db.init_app(app)
    return app
