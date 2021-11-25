from tactic_detect_backend import db


class Ping_echo(db.Model):
    id = db.Column(db.Integer, primary_key=True, autoincrement=True)
    user_email = db.Column(db.String(50), nullable=False)
    dataset_no = db.Column(db.Integer, nullable=False)
    method_type = db.Column(db.String(50), nullable=False)
    method_name = db.Column(db.String(50), nullable=False)

    def __init__(self, user_email, dataset_no, method_type, method_name):
        self.user_email = user_email
        self.dataset_no = dataset_no
        self.method_type = method_type
        self.method_name = method_name
