from flask_script import Manager
from tactic_detect_backend import create_app, db
from flask_migrate import Migrate, MigrateCommand
from tactic_detect_backend.models import CkpRbk, HeartBeat, Redundancy, Voting, Ping_Echo

app = create_app()
manager = Manager(app)
migrate = Migrate(app, db)

manager.add_command('db', MigrateCommand)

if __name__ == '__main__':
    manager.run()