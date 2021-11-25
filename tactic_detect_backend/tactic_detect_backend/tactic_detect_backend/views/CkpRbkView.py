from flask import *
import traceback
import tactic_detect_backend.models as models
from tactic_detect_backend.exts import db

ckprbk = Blueprint("ckprbk", __name__)


@ckprbk.route('/restoremethod', methods=['POST'])
def restore_method():
    try:
        result = json.loads(request.get_data(as_text=True))
        user_email = result['user_email']
        # print(user_email)
        dataset_no = get_dataset_no()
        # print(dataset_no)
        data = result['data']
        for method in data:
            method_type = method['method_type']
            method_name = method['method_name']
            item = models.CkpRbk.CkpRbk(user_email, dataset_no, method_type, method_name)
            db.session.add(item)
            db.session.commit()
            # print("method_type: " + method_type + " method_name:　" + method_name)
        response = {'code': '200'}
        return json.dumps(response)
    except:
        traceback.print_exc()
        response = {'code': '400'}
        return json.dumps(response)


def get_dataset_no():
    sql = 'SELECT MAX(dataset_no) FROM `ckp_rbk`'
    result = db.session.execute(sql).fetchone()[0]
    if result is None:
        return 1
    else:
        return result + 1
